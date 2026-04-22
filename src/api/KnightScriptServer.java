package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import compilador.ast.ErrorLexico;
import compilador.ast.ErrorSintactico;
import compilador.ast.SimboloEntry;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Servidor HTTP REST embebido para KnightScript.
 * No requiere dependencias externas: usa com.sun.net.httpserver del JDK.
 *
 * Endpoints:
 *   POST /compile  { "code": "..." }  → JSON con AST, errores y tabla de símbolos
 *   GET  /health                      → { "status": "ok" }
 */
public class KnightScriptServer {

    public static void start() throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Thread pool para manejar requests concurrentes
        server.setExecutor(Executors.newFixedThreadPool(10));

        server.createContext("/compile", new CompileHandler());
        server.createContext("/health",  new HealthHandler());

        server.start();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   KnightScript API Server — v1.0    ║");
        System.out.println("║   Escuchando en puerto: " + port + "         ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Handler: POST /compile
    // ─────────────────────────────────────────────────────────────────────────
    static class CompileHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            // Pre-flight CORS
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            try {
                // Leer body
                byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
                String body = new String(bodyBytes, StandardCharsets.UTF_8);

                String code = extractJsonString(body, "code");
                if (code == null || code.trim().isEmpty()) {
                    sendJson(exchange, 400, "{\"error\":\"Campo 'code' es requerido\"}");
                    return;
                }

                // Compilar
                CompilerEngine engine = new CompilerEngine();
                engine.compilarDesdeString(code);

                // Construir respuesta JSON estructurada
                String response = buildJsonResponse(engine);
                sendJson(exchange, 200, response);

            } catch (Exception e) {
                sendJson(exchange, 500,
                    "{\"error\":\"Error interno: " + escapeJson(e.getMessage()) + "\"}");
            }
        }

        /** Construye el JSON de respuesta con todos los resultados. */
        private String buildJsonResponse(CompilerEngine engine) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"success\":").append(!engine.tieneErrores()).append(",");
            sb.append("\"ast\":\"").append(escapeJson(engine.getAST())).append("\",");

            // Errores léxicos
            sb.append("\"erroresLexicos\":[");
            List<ErrorLexico> lex = engine.getErroresLexicos();
            for (int i = 0; i < lex.size(); i++) {
                ErrorLexico e = lex.get(i);
                sb.append("{\"linea\":").append(e.linea)
                  .append(",\"columna\":").append(e.columna)
                  .append(",\"descripcion\":\"").append(escapeJson(e.descripcion)).append("\"}");
                if (i < lex.size() - 1) sb.append(",");
            }
            sb.append("],");

            // Errores sintácticos
            sb.append("\"erroresSintacticos\":[");
            List<ErrorSintactico> syn = engine.getErroresSintacticos();
            for (int i = 0; i < syn.size(); i++) {
                ErrorSintactico e = syn.get(i);
                sb.append("{\"linea\":").append(e.linea)
                  .append(",\"columna\":").append(e.columna)
                  .append(",\"descripcion\":\"").append(escapeJson(e.descripcion)).append("\"}");
                if (i < syn.size() - 1) sb.append(",");
            }
            sb.append("],");

            // Tabla de símbolos
            sb.append("\"tablaSimbolos\":[");
            List<SimboloEntry> simbolos = engine.getSimbolos();
            for (int i = 0; i < simbolos.size(); i++) {
                SimboloEntry s = simbolos.get(i);
                sb.append("{\"identificador\":\"").append(escapeJson(s.identificador)).append("\"")
                  .append(",\"tipo\":\"").append(escapeJson(s.tipo)).append("\"")
                  .append(",\"valor\":\"").append(escapeJson(s.valor == null ? "null" : s.valor)).append("\"")
                  .append(",\"linea\":").append(s.linea)
                  .append(",\"columna\":").append(s.columna).append("}");
                if (i < simbolos.size() - 1) sb.append(",");
            }
            sb.append("]");

            sb.append("}");
            return sb.toString();
        }

        /**
         * Extrae el valor de un campo string de un JSON simple.
         * Maneja secuencias de escape básicas.
         */
        private String extractJsonString(String json, String field) {
            String key = "\"" + field + "\"";
            int keyIdx = json.indexOf(key);
            if (keyIdx == -1) return null;

            int colon = json.indexOf(":", keyIdx + key.length());
            if (colon == -1) return null;

            int startQ = json.indexOf("\"", colon + 1);
            if (startQ == -1) return null;

            StringBuilder sb = new StringBuilder();
            int i = startQ + 1;
            while (i < json.length()) {
                char c = json.charAt(i);
                if (c == '\\' && i + 1 < json.length()) {
                    char next = json.charAt(i + 1);
                    switch (next) {
                        case '"':  sb.append('"');  break;
                        case '\\': sb.append('\\'); break;
                        case 'n':  sb.append('\n'); break;
                        case 'r':  sb.append('\r'); break;
                        case 't':  sb.append('\t'); break;
                        default:   sb.append(next); break;
                    }
                    i += 2;
                } else if (c == '"') {
                    break;
                } else {
                    sb.append(c);
                    i++;
                }
            }
            return sb.toString();
        }

        private String escapeJson(String s) {
            if (s == null) return "";
            return s.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Handler: GET /health
    // ─────────────────────────────────────────────────────────────────────────
    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            sendJson(exchange, 200, "{\"status\":\"ok\",\"service\":\"KnightScript API\"}");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Utilidades
    // ─────────────────────────────────────────────────────────────────────────

    /** Añade cabeceras CORS permisivas (el frontend en Vercel necesita esto). */
    private static void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin",  "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
    }

    private static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

package api;

import compilador.ast.*;
import parser.Parser;
import parser.sym;
import Lexer.Lexer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Motor de compilación para KnightScript.
 * Encapsula toda la lógica de análisis léxico, sintáctico y semántico.
 * Thread-safe: usa archivos temporales únicos por request.
 */
public class CompilerEngine {

    private List<ErrorLexico> erroresLexicos;
    private List<ErrorSintactico> erroresSintacticos;
    private StringBuilder astBuffer;
    private List<SimboloEntry> simbolos;

    public CompilerEngine() {
        this.erroresLexicos = new ArrayList<>();
        this.erroresSintacticos = new ArrayList<>();
        this.astBuffer = new StringBuilder();
        this.simbolos = new ArrayList<>();
    }

    /**
     * Compila código desde un String.
     * Usa un archivo temporal con UUID para soportar requests concurrentes.
     */
    public void compilarDesdeString(String codigo) throws IOException {
        limpiar();
        // UUID garantiza que requests concurrentes no choquen entre sí
        String rutaTemporal = System.getProperty("java.io.tmpdir")
                + File.separator + "knight_" + UUID.randomUUID() + ".txt";
        try {
            try (FileWriter fw = new FileWriter(rutaTemporal)) {
                fw.write(codigo);
            }
            preScanLexical(rutaTemporal);
            parseCode(rutaTemporal);
        } catch (Exception e) {
            erroresSintacticos.add(new ErrorSintactico("Error fatal: " + e.getMessage(), 0, 0));
        } finally {
            new File(rutaTemporal).delete();
        }
    }

    /**
     * Pre-escaneo léxico para capturar todos los errores lexicales.
     */
    private void preScanLexical(String rutaArchivo) throws IOException {
        Lexer.limpiarErrores();
        Lexer lexerScan = new Lexer(new FileReader(rutaArchivo));
        while (true) {
            java_cup.runtime.Symbol t = lexerScan.next_token();
            if (t == null || t.sym == sym.EOF) break;
        }
        erroresLexicos = new ArrayList<>(Lexer.getErrores());
    }

    /**
     * Análisis léxico, sintáctico y semántico.
     */
    private void parseCode(String rutaArchivo) throws IOException {
        Lexer.limpiarErrores();

        java.io.ByteArrayOutputStream errBuffer = new java.io.ByteArrayOutputStream();
        java.io.PrintStream errOriginal = System.err;
        System.setErr(new java.io.PrintStream(errBuffer));

        try {
            Parser parser = new Parser(new Lexer(new FileReader(rutaArchivo)));
            Object result = parser.parse().value;

            if (result instanceof Programa) {
                Programa programa = (Programa) result;

                // Árbol sintáctico
                imprimirAST(programa, "");

                // Análisis semántico
                TablaSimbolos.limpiar();
                AnalizadorAST.analizar(programa);
                simbolos = new ArrayList<>(TablaSimbolos.getSimbolos());
            }
        } catch (Exception e) {
            String mensajesErr = errBuffer.toString();
            String[] lineas = mensajesErr.split("\n");
            for (String linea : lineas) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                int columna = 0;
                if (linea.contains("character")) {
                    try {
                        String[] partes = linea.split("character ");
                        if (partes.length > 1) {
                            columna = Integer.parseInt(partes[1].split(" ")[0].trim());
                        }
                    } catch (Exception ignored) {}
                }
                if (linea.contains("Syntax error") || linea.contains("expected")) {
                    erroresSintacticos.add(new ErrorSintactico("Error sintáctico: " + linea, 0, columna));
                }
            }
        } finally {
            System.setErr(errOriginal);
        }
    }

    /** Imprime el AST de forma recursiva al astBuffer. */
    private void imprimirAST(ASTNode node, String indent) {
        if (node == null) return;

        if (node instanceof Programa) {
            Programa p = (Programa) node;
            astBuffer.append(indent).append("Programa\n");
            for (int i = 0; i < p.instrucciones.size(); i++)
                imprimirAST(p.instrucciones.get(i), indent + "  ");
        } else if (node instanceof DeclaracionAsignacion) {
            DeclaracionAsignacion da = (DeclaracionAsignacion) node;
            astBuffer.append(indent).append("DeclaracionAsignacion [tipo=").append(da.tipo)
                    .append(", id=").append(da.identificador).append("]\n");
            imprimirAST(da.expresion, indent + "  ");
        } else if (node instanceof Declaracion) {
            Declaracion d = (Declaracion) node;
            astBuffer.append(indent).append("Declaracion [tipo=").append(d.tipo)
                    .append(", id=").append(d.identificador).append("]\n");
        } else if (node instanceof Asignacion) {
            Asignacion a = (Asignacion) node;
            astBuffer.append(indent).append("Asignacion [id=").append(a.identificador).append("]\n");
            imprimirAST(a.expresion, indent + "  ");
        } else if (node instanceof Print) {
            astBuffer.append(indent).append("Print\n");
            imprimirAST(((Print) node).expresion, indent + "  ");
        } else if (node instanceof Escuchar) {
            astBuffer.append(indent).append("Escuchar [id=").append(((Escuchar) node).identificador).append("]\n");
        } else if (node instanceof Si) {
            Si s = (Si) node;
            astBuffer.append(indent).append("Si\n");
            astBuffer.append(indent).append("  [condicion]\n");
            imprimirAST(s.condicion, indent + "    ");
            astBuffer.append(indent).append("  [entonces]\n");
            for (int i = 0; i < s.bloque.size(); i++) imprimirAST(s.bloque.get(i), indent + "    ");
            if (s.bloqueSombra != null) {
                astBuffer.append(indent).append("  [sombra]\n");
                for (int i = 0; i < s.bloqueSombra.size(); i++) imprimirAST(s.bloqueSombra.get(i), indent + "    ");
            }
        } else if (node instanceof Mientras) {
            Mientras m = (Mientras) node;
            astBuffer.append(indent).append("Mientras\n");
            astBuffer.append(indent).append("  [condicion]\n");
            imprimirAST(m.condicion, indent + "    ");
            astBuffer.append(indent).append("  [bloque]\n");
            for (int i = 0; i < m.bloque.size(); i++) imprimirAST(m.bloque.get(i), indent + "    ");
        } else if (node instanceof For) {
            For f = (For) node;
            astBuffer.append(indent).append("For\n");
            for (int i = 0; i < f.bloque.size(); i++) imprimirAST(f.bloque.get(i), indent + "  ");
        } else if (node instanceof Retornar) {
            Retornar r = (Retornar) node;
            astBuffer.append(indent).append("Retornar\n");
            if (r.expresion != null) imprimirAST(r.expresion, indent + "  ");
        } else if (node instanceof Binaria) {
            Binaria b = (Binaria) node;
            astBuffer.append(indent).append("Binaria [op=").append(b.operador).append("]\n");
            imprimirAST(b.izquierda, indent + "  ");
            imprimirAST(b.derecha, indent + "  ");
        } else if (node instanceof Unaria) {
            Unaria u = (Unaria) node;
            astBuffer.append(indent).append("Unaria [op=").append(u.operador).append("]\n");
            imprimirAST(u.expresion, indent + "  ");
        } else if (node instanceof Literal) {
            astBuffer.append(indent).append("Literal [valor=").append(((Literal) node).valor).append("]\n");
        } else if (node instanceof Identificador) {
            astBuffer.append(indent).append("Identificador [nombre=").append(((Identificador) node).nombre).append("]\n");
        }
    }

    private void limpiar() {
        erroresLexicos.clear();
        erroresSintacticos.clear();
        astBuffer = new StringBuilder();
        simbolos = new ArrayList<>();
    }

    // ─── Getters para el servidor HTTP ───────────────────────────────────────

    public boolean tieneErrores() {
        return !erroresLexicos.isEmpty() || !erroresSintacticos.isEmpty();
    }

    public String getAST() {
        return astBuffer.toString();
    }

    public List<ErrorLexico> getErroresLexicos() {
        return erroresLexicos;
    }

    public List<ErrorSintactico> getErroresSintacticos() {
        return erroresSintacticos;
    }

    public List<SimboloEntry> getSimbolos() {
        return simbolos;
    }
}

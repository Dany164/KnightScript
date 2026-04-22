package compilador.ast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TablaSimbolos {

    private static Map<String, SimboloEntry> tabla = new LinkedHashMap<>();

    public static void limpiar() {
        tabla.clear();
    }

    public static void declarar(String id, String tipo, String valor, int linea, int columna) {
        tabla.put(id, new SimboloEntry(id, tipo, valor, linea, columna));
    }

    public static void actualizar(String id, String valor) {
        if (tabla.containsKey(id)) {
            tabla.get(id).valor = valor;
        }
    }

    public static List<SimboloEntry> getSimbolos() {
        return new ArrayList<>(tabla.values());
    }

    public static String obtenerTablaFormateada() {
        List<SimboloEntry> simbolos = getSimbolos();
        StringBuilder sb = new StringBuilder();

        if (simbolos.isEmpty()) {
            sb.append("Sin simbolos registrados.\n");
        } else {
            sb.append(String.format("%-5s %-15s %-10s %-20s %-8s %-8s%n",
                    "No.", "Identificador", "Tipo", "Valor", "Linea", "Columna"));
            sb.append("------------------------------------------------------------------\n");
            for (int i = 0; i < simbolos.size(); i++) {
                SimboloEntry s = simbolos.get(i);
                sb.append(String.format("%-5d %-15s %-10s %-20s %-8d %-8d%n",
                        i + 1, s.identificador, s.tipo,
                        s.valor == null ? "null" : s.valor,
                        s.linea, s.columna));
            }
        }

        return sb.toString();
    }

    public static void imprimir() {
        System.out.println("\n=== TABLA DE SIMBOLOS ===");
        System.out.print(obtenerTablaFormateada());
    }
}
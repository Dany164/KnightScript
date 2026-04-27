package compilador.ast;

import java.util.HashMap;
import java.util.Map;

public class InterpreteAST {

    private StringBuilder salida;
    private Map<String, String> memoria;

    public InterpreteAST() {
        this.salida = new StringBuilder();
        this.memoria = new HashMap<>();
    }

    public void ejecutar(ASTNode node) throws RuntimeException {
        if (node == null) return;

        if (node instanceof Programa) {
            Programa p = (Programa) node;
            for (int i = 0; i < p.instrucciones.size(); i++) {
                ejecutar(p.instrucciones.get(i));
            }
        } else if (node instanceof Declaracion) {
            Declaracion d = (Declaracion) node;
            memoria.put(d.identificador, ""); // Default vacío
        } else if (node instanceof DeclaracionAsignacion) {
            DeclaracionAsignacion da = (DeclaracionAsignacion) node;
            String valor = evaluar(da.expresion);
            memoria.put(da.identificador, valor);
        } else if (node instanceof Asignacion) {
            Asignacion a = (Asignacion) node;
            String valor = evaluar(a.expresion);
            memoria.put(a.identificador, valor);
        } else if (node instanceof Si) {
            Si s = (Si) node;
            String cond = evaluar(s.condicion);
            if ("luz".equals(cond) || "true".equals(cond)) {
                for (int i = 0; i < s.bloque.size(); i++) ejecutar(s.bloque.get(i));
            } else if (s.bloqueSombra != null) {
                for (int i = 0; i < s.bloqueSombra.size(); i++) ejecutar(s.bloqueSombra.get(i));
            }
        } else if (node instanceof Mientras) {
            Mientras m = (Mientras) node;
            int iteraciones = 0;
            while (true) {
                String cond = evaluar(m.condicion);
                if (!"luz".equals(cond) && !"true".equals(cond)) {
                    break;
                }
                iteraciones++;
                if (iteraciones > 5000) {
                    salida.append("⚠ Error en Ejecución: Límite de 5000 iteraciones excedido (Posible ciclo infinito).\n");
                    break; // Cortar el ciclo
                }
                for (int i = 0; i < m.bloque.size(); i++) ejecutar(m.bloque.get(i));
            }
        } else if (node instanceof Print) {
            Print p = (Print) node;
            String valor = evaluar(p.expresion);
            salida.append(valor).append("\n");
        }
    }

    private String evaluar(ASTNode expr) {
        if (expr instanceof Literal) {
            Object v = ((Literal) expr).valor;
            if (v instanceof String) {
                String str = (String) v;
                if (str.startsWith("\"") && str.endsWith("\"")) {
                    return str.substring(1, str.length() - 1);
                }
                return str;
            }
            return String.valueOf(v);
        } else if (expr instanceof Identificador) {
            String nombre = ((Identificador) expr).nombre;
            return memoria.getOrDefault(nombre, "");
        } else if (expr instanceof Unaria) {
            Unaria u = (Unaria) expr;
            String val = evaluar(u.expresion);
            if (u.operador.equals("!")) {
                if ("luz".equals(val) || "true".equals(val)) return "vacio";
                else return "luz";
            }
            if (u.operador.equals("-")) {
                try {
                    int num = Integer.parseInt(val);
                    return String.valueOf(-num);
                } catch (NumberFormatException e) {
                    return val;
                }
            }
            return val;
        } else if (expr instanceof Binaria) {
            Binaria b = (Binaria) expr;
            String izqStr = evaluar(b.izquierda);
            String derStr = evaluar(b.derecha);

            if (b.operador.equals("+") || b.operador.equals("-") || b.operador.equals("*") || b.operador.equals("/")) {
                try {
                    int izq = Integer.parseInt(izqStr.trim());
                    int der = Integer.parseInt(derStr.trim());
                    if (b.operador.equals("+")) return String.valueOf(izq + der);
                    if (b.operador.equals("-")) return String.valueOf(izq - der);
                    if (b.operador.equals("*")) return String.valueOf(izq * der);
                    if (b.operador.equals("/")) {
                        if (der == 0) return "0";
                        return String.valueOf(izq / der);
                    }
                } catch (NumberFormatException e) {
                    if (b.operador.equals("+")) return izqStr + derStr;
                    return "0";
                }
            }

            if (b.operador.equals("==")) return izqStr.equals(derStr) ? "luz" : "vacio";
            if (b.operador.equals("!=")) return !izqStr.equals(derStr) ? "luz" : "vacio";

            if (b.operador.equals("&&")) {
                boolean i = izqStr.equals("luz") || izqStr.equals("true");
                boolean d = derStr.equals("luz") || derStr.equals("true");
                return (i && d) ? "luz" : "vacio";
            }
            if (b.operador.equals("||")) {
                boolean i = izqStr.equals("luz") || izqStr.equals("true");
                boolean d = derStr.equals("luz") || derStr.equals("true");
                return (i || d) ? "luz" : "vacio";
            }

            try {
                int izq = Integer.parseInt(izqStr.trim());
                int der = Integer.parseInt(derStr.trim());
                if (b.operador.equals("<")) return izq < der ? "luz" : "vacio";
                if (b.operador.equals(">")) return izq > der ? "luz" : "vacio";
                if (b.operador.equals("<=")) return izq <= der ? "luz" : "vacio";
                if (b.operador.equals(">=")) return izq >= der ? "luz" : "vacio";
            } catch (NumberFormatException e) {
                return "vacio";
            }
        }
        return "?";
    }

    public String getSalida() {
        return salida.toString();
    }
}

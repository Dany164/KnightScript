package compilador.ast;

import java.util.List;

public class AnalizadorAST {

    public static void analizar(ASTNode node) {
        if (node == null) return;

        if (node instanceof Programa) {
            Programa p = (Programa) node;
            for (int i = 0; i < p.instrucciones.size(); i++)
                analizar(p.instrucciones.get(i));

        } else if (node instanceof Declaracion) {
            Declaracion d = (Declaracion) node;
            TablaSimbolos.declarar(d.identificador, d.tipo, null, d.linea, d.columna);

        } else if (node instanceof DeclaracionAsignacion) {
            DeclaracionAsignacion da = (DeclaracionAsignacion) node;
            String valor = resolverValor(da.expresion);
            TablaSimbolos.declarar(da.identificador, da.tipo, valor, da.linea, da.columna);

        } else if (node instanceof Asignacion) {
            Asignacion a = (Asignacion) node;
            String valor = resolverValor(a.expresion);
            TablaSimbolos.actualizar(a.identificador, valor);

        } else if (node instanceof Si) {
            Si s = (Si) node;
            for (int i = 0; i < s.bloque.size(); i++)
                analizar(s.bloque.get(i));
            if (s.bloqueSombra != null)
                for (int i = 0; i < s.bloqueSombra.size(); i++)
                    analizar(s.bloqueSombra.get(i));

        } else if (node instanceof Mientras) {
            Mientras m = (Mientras) node;
            for (int i = 0; i < m.bloque.size(); i++)
                analizar(m.bloque.get(i));
        }
    }

    private static String resolverValor(ASTNode expr) {
        if (expr instanceof Literal) {
            return String.valueOf(((Literal) expr).valor);
        } else if (expr instanceof Identificador) {
            return ((Identificador) expr).nombre;
        } else if (expr instanceof Binaria) {
            Binaria b = (Binaria) expr;
            String izq = resolverValor(b.izquierda);
            String der = resolverValor(b.derecha);
            return izq + " " + b.operador + " " + der;
        } else if (expr instanceof Unaria) {
            Unaria u = (Unaria) expr;
            return u.operador + resolverValor(u.expresion);
        }
        return "?";
    }
}
import compilador.ast.*;

public class ASTPrinter {

    public static void printAST(ASTNode node, String indent) {
        if (node == null) return;

        System.out.println(indent + node.getClass().getSimpleName());

        if (node instanceof Programa) {
            Programa p = (Programa) node;
            for (ASTNode inst : p.instrucciones) {
                printAST(inst, indent + "  ");
            }
        }

        else if (node instanceof Declaracion) {
            Declaracion d = (Declaracion) node;
            System.out.println(indent + "  id: " + d.identificador);
        }

        else if (node instanceof Asignacion) {
            Asignacion a = (Asignacion) node;
            System.out.println(indent + "  id: " + a.identificador);
            printAST(a.expresion, indent + "  ");
        }

        else if (node instanceof Print) {
            Print p = (Print) node;
            printAST(p.expresion, indent + "  ");
        }

        else if (node instanceof Binaria) {
            Binaria b = (Binaria) node;
            System.out.println(indent + "  op: " + b.operador);
            printAST(b.izquierda, indent + "  ");
            printAST(b.derecha, indent + "  ");
        }

        else if (node instanceof Literal) {
            Literal l = (Literal) node;
            System.out.println(indent + "  valor: " + l.valor);
        }

        else if (node instanceof Identificador) {
            Identificador i = (Identificador) node;
            System.out.println(indent + "  nombre: " + i.nombre);
        }
    }
}

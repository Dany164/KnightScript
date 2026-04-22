package compilador.ast;

public class Asignacion extends ASTNode {
    public String identificador;
    public ASTNode expresion;

    public Asignacion(String identificador, ASTNode expresion) {
        this.identificador = identificador;
        this.expresion = expresion;
    }
}
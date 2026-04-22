package compilador.ast;

public class Retornar extends ASTNode {
    public ASTNode expresion;

    public Retornar(ASTNode expresion) {
        this.expresion = expresion;
    }
}
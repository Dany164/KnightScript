package compilador.ast;

public class Print extends ASTNode {
    public ASTNode expresion;

    public Print(ASTNode expresion) {
        this.expresion = expresion;
    }
}
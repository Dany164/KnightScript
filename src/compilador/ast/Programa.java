package compilador.ast;

import java.util.List;

public class Programa extends ASTNode {
    public List<ASTNode> instrucciones;

    public Programa(List<ASTNode> instrucciones) {
        this.instrucciones = instrucciones;
    }
}
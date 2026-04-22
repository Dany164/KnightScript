package compilador.ast;

import java.util.List;

public class Mientras extends ASTNode {
    public ASTNode condicion;
    public List<ASTNode> bloque;

    public Mientras(ASTNode condicion, List<ASTNode> bloque) {
        this.condicion = condicion;
        this.bloque = bloque;
    }
}

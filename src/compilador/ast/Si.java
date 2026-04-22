package compilador.ast;

import java.util.List;

public class Si extends ASTNode {
    public ASTNode condicion;
    public List<ASTNode> bloque;
    public List<ASTNode> bloqueSombra;

    public Si(ASTNode condicion, List<ASTNode> bloque, List<ASTNode> bloqueSombra) {
        this.condicion = condicion;
        this.bloque = bloque;
        this.bloqueSombra = bloqueSombra;
    }
}



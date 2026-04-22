package compilador.ast;

import java.util.List;

public class For extends ASTNode {
    public ASTNode inicial;
    public ASTNode condicion;
    public ASTNode incremento;
    public List<ASTNode> bloque;

    public For(ASTNode inicial, ASTNode condicion, ASTNode incremento, List<ASTNode> bloque) {
        this.inicial = inicial;
        this.condicion = condicion;
        this.incremento = incremento;
        this.bloque = bloque;
    }
}

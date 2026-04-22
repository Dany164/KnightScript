package compilador.ast;

public class Declaracion extends ASTNode {
    public String tipo;
    public String identificador;

    public Declaracion(String tipo, String identificador) {
        this.tipo = tipo;
        this.identificador = identificador;
    }
}
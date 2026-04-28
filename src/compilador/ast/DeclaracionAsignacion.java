package compilador.ast;

public class DeclaracionAsignacion extends ASTNode {
    public String tipo;
    public String identificador;
    public ASTNode expresion;

    public DeclaracionAsignacion(String tipo, String identificador, ASTNode expresion, int linea, int columna) {
        this.tipo = tipo;
        this.identificador = identificador;
        this.expresion = expresion;
        this.linea = linea;
        this.columna = columna;
    }
}
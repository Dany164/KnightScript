package compilador.ast;

public class Asignacion extends ASTNode {
    public String identificador;
    public ASTNode expresion;

    public Asignacion(String identificador, ASTNode expresion, int linea, int columna) {
        this.identificador = identificador;
        this.expresion = expresion;
        this.linea = linea;
        this.columna = columna;
    }
}
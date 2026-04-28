package compilador.ast;

public class Declaracion extends ASTNode {
    public String tipo;
    public String identificador;

    public Declaracion(String tipo, String identificador, int linea, int columna) {
        this.tipo = tipo;
        this.identificador = identificador;
        this.linea = linea;
        this.columna = columna;
    }
}
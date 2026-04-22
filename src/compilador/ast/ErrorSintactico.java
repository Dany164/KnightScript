package compilador.ast;

public class ErrorSintactico {
    public String descripcion;
    public int linea;
    public int columna;

    public ErrorSintactico(String descripcion, int linea, int columna) {
        this.descripcion = descripcion;
        this.linea = linea;
        this.columna = columna;
    }
}
package compilador.ast;

public class SimboloEntry {
    public String identificador;
    public String tipo;
    public String valor;
    public int linea;
    public int columna;

    public SimboloEntry(String identificador, String tipo, String valor, int linea, int columna) {
        this.identificador = identificador;
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
    }
}
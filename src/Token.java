public class Token {

    public enum Tipo {
        // Palabras reservadas
        INICIO, FIN,
        GEO, TEXTO, ESENCIA,
        SI, SOMBRA, ENTONCES,
        MIENTRAS, RECORRER,
        INVOCAR, ESCUCHAR,
        LUZ, VACIO, RETORNAR,

        // Identificadores y valores
        IDENTIFICADOR,
        NUMERO,
        CADENA,

        // Operadores
        SUMA, RESTA, MULT, DIV,
        MAYOR, MENOR, MAYOR_IGUAL, MENOR_IGUAL,
        IGUAL_IGUAL, DIFERENTE,
        ASIGNACION,

        // Símbolos
        PUNTO_COMA,
        PARENTESIS_ABRE, PARENTESIS_CIERRA,
        LLAVE_ABRE, LLAVE_CIERRA,

        // Especiales
        EOF,
        ERROR
    }

    private Tipo tipo;
    private String lexema;
    private int linea;
    private int columna;

    public Token(Tipo tipo, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tipo=" + tipo +
                ", lexema='" + lexema + '\'' +
                ", linea=" + linea +
                ", columna=" + columna +
                '}';
    }
}
package compilador.ast;

public class Binaria extends Expresion {
    public Expresion izquierda;
    public String operador;
    public Expresion derecha;

    public Binaria(Expresion izquierda, String operador, Expresion derecha) {
        this.izquierda = izquierda;
        this.operador = operador;
        this.derecha = derecha;
    }
}


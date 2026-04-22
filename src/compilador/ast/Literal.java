package compilador.ast;

public class Literal extends Expresion {
    public Object valor;

    public Literal(Object valor) {
        this.valor = valor;
    }
}
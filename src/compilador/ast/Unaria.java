package compilador.ast;

public class Unaria extends Expresion {
    public String operador;
    public Expresion expresion;

    public Unaria(String operador, Expresion expresion) {
        this.operador = operador;
        this.expresion = expresion;
    }
}
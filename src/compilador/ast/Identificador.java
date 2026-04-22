package compilador.ast;

public class Identificador extends Expresion {
    public String nombre;

    public Identificador(String nombre) {
        this.nombre = nombre;
    }
}
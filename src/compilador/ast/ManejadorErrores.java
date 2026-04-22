package compilador.ast;

import java.util.ArrayList;
import java.util.List;

public class ManejadorErrores {
    public static List<ErrorSintactico> erroresSintacticos = new ArrayList<>();

    public static void agregar(String mensaje, int linea, int columna) {
        erroresSintacticos.add(new ErrorSintactico(mensaje, linea, columna));
    }

    public static void limpiar() {
        erroresSintacticos.clear();
    }
}
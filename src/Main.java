import api.KnightScriptServer;

/**
 * Punto de entrada de KnightScript.
 * Lanza el servidor HTTP REST que expone la API del compilador.
 * El puerto se configura con la variable de entorno PORT (default: 8080).
 */
public class Main {

    public static void main(String[] args) {
        try {
            KnightScriptServer.start();
        } catch (Exception e) {
            System.err.println("Error fatal al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
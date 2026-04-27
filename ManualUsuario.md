# Manual de Usuario - KnightScript

¡Bienvenido a **KnightScript**! 

KnightScript es un entorno web interactivo diseñado para que aprendas los conceptos básicos de la programación a través de un lenguaje único, inspirado en el mundo y los personajes del videojuego *Hollow Knight*.

Este manual está pensado para **cualquier persona**, incluso si es tu primera vez programando. Aquí aprenderás a escribir tus primeros "hechizos" (instrucciones) y a hacer que la computadora los entienda.

---

## 1. Conociendo el Entorno de Trabajo

Nuestra página web está dividida en dos partes principales para hacerte la vida más fácil:

*   **El Editor de Código (Panel Izquierdo):** Esta es tu área de trabajo principal. Piensa en ella como un pergamino en blanco donde escribirás las instrucciones que quieres que la computadora ejecute.
*   **El Panel de Resultados (Panel Derecho):** Aquí verás la magia ocurrir. Este panel tiene varias pestañas:
    *   *Salida (Output):* Aquí aparecerán los mensajes o resultados matemáticos que tu programa genere.
    *   *Errores:* Si te equivocas al escribir una palabra o te falta un símbolo, aquí aparecerá una pista de qué salió mal.
    *   *Símbolos / AST:* (Opcional) Estas pestañas te muestran cómo la computadora "desarma" tu código por dentro para entenderlo. ¡No tienes que preocuparte por ellas si recién empiezas!
*   **El botón "▶ Compilar":** Una vez que termines de escribir, presionas este botón para enviarle las instrucciones a la computadora.

---

## 2. Tu Primer Programa ("Hola Hallownest")

La mejor forma de aprender es haciéndolo. Vamos a escribir tu primer programa.

1.  Borra cualquier texto que esté en el panel izquierdo (Editor de Código).
2.  Copia exactamente el siguiente bloque de texto y pégalo en el editor:

```knightscript
inicio
  invocar("¡Hola Hallownest!");
fin
```

3.  Haz clic en el botón **▶ Compilar**.
4.  Observa el panel derecho (pestaña de Salida). Deberías ver el mensaje: `¡Hola Hallownest!`

**¿Qué pasó aquí?**
*   `inicio` y `fin`: Le indican a la computadora dónde empieza y dónde termina tu programa. ¡Siempre deben estar presentes!
*   `invocar(...)`: Es una instrucción especial que le dice a la pantalla que muestre el texto que está entre las comillas.
*   `;` (Punto y coma): Al final de la instrucción, funciona como un "punto final" en una oración. Le avisa a la computadora que terminaste esa orden.

---

## 3. Guía Básica del Lenguaje (Para principiantes)

Para hacer programas más complejos, necesitas guardar información y tomar decisiones. KnightScript usa palabras especiales para esto:

### A) Guardar información (Variables)
Piensa en las variables como "cajas" donde puedes guardar datos. Dependiendo de lo que quieras guardar, usas una caja diferente:

| ¿Qué quieres guardar? | Palabra a usar | Ejemplo |
| :--- | :--- | :--- |
| **Números** (enteros) | `geo` | `geo monedas = 100;` |
| **Palabras / Texto** | `texto` | `texto nombre = "Caballero";` |
| **Verdadero / Falso** | `esencia` | `esencia tieneEspada = luz;` *(Usa `luz` para verdadero y `vacio` para falso)* |

> **💡 Tip:** Observa cómo siempre terminamos la línea con un punto y coma `;`.

### B) Tomar decisiones (Condicionales)
A veces quieres que tu programa haga algo solo **si** ocurre algo específico.

```knightscript
si (monedas > 50) entonces {
    invocar("Puedes comprar el mapa.");
} sombra {
    invocar("No tienes suficiente geo.");
}
```
*   `si (...) entonces`: Verifica si la condición es verdadera.
*   `sombra`: Significa "de lo contrario" (si la condición no se cumplió).

### C) Repetir acciones (Ciclos)
Si quieres que algo se repita varias veces, usas la palabra `mientras`.

```knightscript
geo golpes = 0;
mientras (golpes < 3) {
    invocar("Atacando...");
    golpes = golpes + 1;
}
```

---

## 4. Ejemplos Prácticos

Aquí tienes un par de programas completos que puedes copiar, pegar y modificar para experimentar.

**Ejemplo 1: Calculadora de Vida**
```knightscript
inicio
  // Guardamos los valores de vida y daño
  geo escudo = 5;
  geo dano = 2;
  
  // Calculamos la vida restante
  geo vida = escudo - dano;
  
  // Mostramos el resultado
  invocar("Tu vida restante es:");
  invocar(vida);
fin
```

**Ejemplo 2: ¿Puedo curarme?**
```knightscript
inicio
  geo alma = 100;
  esencia puedeCurar = luz;

  si (alma >= 33 && puedeCurar == luz) entonces {
    invocar("Te has curado 1 mascara!");
  } sombra {
    invocar("Necesitas mas alma para curarte.");
  }
fin
```

---

## 5. Solución de Problemas (Errores comunes)

Si presionas "Compilar" y el indicador se pone en rojo (✗ Con errores), ¡no te preocupes! Es normal equivocarse. Revisa la pestaña de **Errores** en el panel derecho.

Aquí tienes los problemas más comunes:

1.  **"Caracter no permitido" o "Syntax Error":**
    *   *¿Qué revisar?* Asegúrate de no haber olvidado un punto y coma `;` al final de alguna línea.
    *   *¿Qué revisar?* Verifica que las palabras estén bien escritas (ej. escribir `geoo` en lugar de `geo`).
2.  **Te falta el `inicio` o `fin`:** Todo tu código debe estar siempre encerrado entre estas dos palabras.
3.  **Error de Conexión ("No se pudo conectar con el servidor"):** Esto significa que el "cerebro" de la aplicación (el backend) no está encendido o tu internet está fallando. Si estás en la web pública, intenta recargar la página.

¡Disfruta tu viaje por Hallownest y feliz programación!

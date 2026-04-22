import java_cup.runtime.Symbol;
import compilador.ast.ErrorLexico;
import java.util.ArrayList;
import java.util.List;

%%

%class Lexer
%implements java_cup.runtime.Scanner
%function next_token
%public
%unicode
%line
%column
%type java_cup.runtime.Symbol

%{
private static List<ErrorLexico> errores = new ArrayList<>();

public static List<ErrorLexico> getErrores() {
    return errores;
}

public static void limpiarErrores() {
    errores.clear();
}

private Symbol symbol(int tipo) {
    return new Symbol(tipo, yyline + 1, yycolumn + 1);
}

private Symbol symbol(int tipo, Object value) {
    return new Symbol(tipo, yyline + 1, yycolumn + 1, value);
}
%}

/* Expresiones regulares */
DIGIT   = [0-9]
ID      = [a-zA-Z_][a-zA-Z0-9_]*
CADENA  = \"([^\"\\]|\\.)*\"

%%

/* Palabras reservadas */
"inicio"      { return symbol(sym.INICIO); }
"fin"         { return symbol(sym.FIN); }
"geo"         { return symbol(sym.GEO); }
"texto"       { return symbol(sym.TEXTO); }
"esencia"     { return symbol(sym.ESENCIA); }
"si"          { return symbol(sym.SI); }
"sombra"      { return symbol(sym.SOMBRA); }
"entonces"    { return symbol(sym.ENTONCES); }
"mientras"    { return symbol(sym.MIENTRAS); }
"recorrer"    { return symbol(sym.RECORRER); }
"invocar"     { return symbol(sym.INVOCAR); }
"escuchar"    { return symbol(sym.ESCUCHAR); }
"luz"         { return symbol(sym.LUZ); }
"vacio"       { return symbol(sym.VACIO); }
"retornar"    { return symbol(sym.RETORNAR); }

/* Operadores aritmeticos */
"+"           { return symbol(sym.SUMA); }
"-"           { return symbol(sym.RESTA); }
"*"           { return symbol(sym.MULT); }
"/"           { return symbol(sym.DIV); }

/* Operadores de comparacion */
"=="          { return symbol(sym.IGUAL); }
"!="          { return symbol(sym.DIFERENTE); }
"<"           { return symbol(sym.MENOR); }
">"           { return symbol(sym.MAYOR); }
"<="          { return symbol(sym.MENOR_IGUAL); }
">="          { return symbol(sym.MAYOR_IGUAL); }

/* Operadores logicos */
"&&"          { return symbol(sym.AND); }
"||"          { return symbol(sym.OR); }
"!"           { return symbol(sym.NOT); }

/* Asignacion */
"="           { return symbol(sym.ASIGNACION); }

/* Simbolos */
";"           { return symbol(sym.PUNTO_COMA); }
","           { return symbol(sym.COMA); }
"("           { return symbol(sym.PARENTESIS_ABRE); }
")"           { return symbol(sym.PARENTESIS_CIERRA); }
"{"           { return symbol(sym.LLAVE_ABRE); }
"}"           { return symbol(sym.LLAVE_CIERRA); }

/* Cadenas */
{CADENA}      { return symbol(sym.CADENA, yytext()); }

/* Numeros */
{DIGIT}+      { return symbol(sym.NUMERO, yytext()); }

/* Identificadores */
{ID}          { return symbol(sym.IDENTIFICADOR, yytext()); }

/* Comentarios de linea */
"//"[^\n]*    { }

/* Comentarios de bloque */
"/*"~"*/"     { }

/* Espacios */
[ \t\r\n]+    { }

/* Error lexico */
.             {
    errores.add(new ErrorLexico(
        "Caracter no permitido: '" + yytext() + "'",
        yyline + 1,
        yycolumn + 1
    ));
}
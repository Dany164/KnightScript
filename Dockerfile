# ── Etapa 1: Compilación ──────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Crear directorio de salida y compilar todos los .java
RUN mkdir -p out && \
    find src -name "*.java" > sources.txt && \
    javac \
      -cp "java-cup-11b-runtime.jar" \
      -sourcepath src \
      -d out \
      @sources.txt

# Crear el JAR ejecutable
RUN printf 'Main-Class: Main\nClass-Path: java-cup-11b-runtime.jar\n\n' > MANIFEST.MF && \
    jar cfm KnightScript.jar MANIFEST.MF -C out .

# ── Etapa 2: Imagen de producción (solo JRE) ──────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Solo lo necesario para ejecutar
COPY --from=builder /app/KnightScript.jar .
COPY java-cup-11b-runtime.jar .

# Railway inyecta PORT automáticamente
EXPOSE 8080

CMD ["java", "-cp", "KnightScript.jar:java-cup-11b-runtime.jar", "Main"]

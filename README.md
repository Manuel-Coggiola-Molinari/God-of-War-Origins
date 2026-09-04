# God of War Origins

Proyecto final de **Programación sobre Redes** desarrollado en **Java** utilizando **LibGDX**.

## Descripción

**God of War Origins** es un videojuego cooperativo de supervivencia en 2D con perspectiva cenital, inspirado en la mitología nórdica y ambientado en el período previo a los acontecimientos de *God of War (2018)*.

Los jugadores controlan a **Kratos** y **Laufey**, quienes deberán explorar Midgard, recolectar recursos, construir estructuras, enfrentarse a enemigos y colaborar para fortalecer una barrera mágica que protege su hogar. El juego podrá disfrutarse tanto en modo individual como cooperativo mediante una red local, siendo la implementación de la comunicación en red uno de los ejes principales del proyecto.

---

## Estado del proyecto

### Prototipo jugable (Pre-entrega N°2)

El proyecto cuenta actualmente con un prototipo jugable en modo individual, con las siguientes características implementadas:

- Movimiento del personaje con detección de colisiones contra el mapa y las estructuras.
- Animaciones del personaje (idle, caminar, atacar, morir) basadas en spritesheets.
- Combate en tiempo real contra un enemigo con inteligencia artificial básica.
- Sistema de construcción de estructuras.
- Mapa cargado desde Tiled.
- Cámara que sigue al jugador, adaptada a distintos tamaños de ventana.
- HUD con barra de vida propia y del enemigo.
- Gestión de pantallas: menú inicial, juego, pausa y game over.

### Planificado (próximas etapas)

- Modo cooperativo mediante arquitectura cliente-servidor (TCP/UDP).
- Segundo personaje jugable (Laufey).
- Inventario y comercio.
- Progresión basada en objetivos.
- Música y efectos de sonido.

---

## Controles

| Acción | Tecla |
|---|---|
| Mover personaje | W A S D |
| Atacar | Click izquierdo |
| Construir | E |
| Pausar / Reanudar | ESC |
| Navegar menú | W / S o flechas |
| Confirmar opción de menú | ENTER |

---

## Tecnologías y Plataforma

- Java 21 (JDK 21)
- Plataforma Objetivo: Escritorio (Desktop - Windows, macOS, Linux)
- LibGDX 1.13.5 (módulo Lwjgl3)
- Gradle
- Git / GitHub
- IntelliJ IDEA

---

## Integrantes

- Manuel Coggiola Molinari
- Felipe Pavon Martorelli

---

## Instalación

### Requisitos

- Java JDK 21
- IntelliJ IDEA (recomendado)

### Clonar el repositorio

```bash
git clone https://github.com/Manuel-Coggiola-Molinari/God-of-War-Origins.git
```

### Ejecutar

**Opción 1 (recomendada) — desde IntelliJ:**

1. Abrir el proyecto con IntelliJ IDEA.
2. Esperar a que Gradle descargue las dependencias.
3. Buscar el archivo `lwjgl3/src/main/java/com/goworigins/lwjgl3/Lwjgl3Launcher.java`.
4. Click derecho sobre el archivo → **Run 'Lwjgl3Launcher.main()'**.

**Opción 2 — desde la terminal:**

```bash
./gradlew lwjgl3:run
```

En Windows:

```bash
gradlew.bat lwjgl3:run
```

---

## Video de demostración

[https://drive.google.com/file/d/1TfikYq2Kgt0qR6N8fp6HI-rI3opJKiTl/view?usp=sharing]

---

## Documentación

La documentación del proyecto se encuentra en la [**Wiki**](https://github.com/Manuel-Coggiola-Molinari/God-of-War-Origins/wiki) del repositorio.

---

## Licencia

Proyecto desarrollado con fines educativos para la materia **Programación sobre Redes**.

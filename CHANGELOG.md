# Changelog

Todos los cambios importantes de este proyecto serán documentados en este archivo.
El formato utilizado está basado en Keep a Changelog y el versionado sigue el criterio de Semantic Versioning.

## [0.2.0] - 2026-08-30

### Agregado

- Sistema de entrada de usuario dedicado (`GameInput`) con manejo de teclado (WASD) y mouse (click para atacar).
- Animaciones basadas en spritesheets para el personaje (idle, caminar, atacar, morir) mediante `AnimationManager` y `PlayerAnimations`.
- Carga y renderizado del mapa de juego mediante Tiled (`GoWOriginsMap.tmx`).
- Sistema de cámara con `Viewport` (FitViewport) que sigue al jugador con margen configurable.
- Sistema de colisiones entre el jugador, el mapa y las construcciones (`CollisionManager`).
- Sistema de combate en tiempo real (`CombatSystem`) con daño, cooldown y área de ataque.
- Enemigo con inteligencia artificial básica (`EnemyAI`): persecución, ataque y estados (`EnemyState`).
- Sistema de construcción de estructuras (`BuildingSystem`, `Building`, `BuildingType`).
- HUD con barra de vida del jugador y del enemigo (`GameHUD`).
- Sistema de gestión de pantallas mediante `Game` y `Screen` de LibGDX:
    - `MenuScreen`: pantalla inicial con opciones navegables (Jugar / Salir).
    - `GameScreen`: pantalla principal de juego (contiene toda la lógica anterior).
    - `PauseScreen`: pantalla de pausa (tecla ESC), con opción de reanudar o volver al menú.
    - `GameOverScreen`: pantalla de fin de partida al morir el jugador.
- Mejoras visuales en el menú principal: fondo con degradé, sprite de Kratos, título con efecto de sombra.

### Corregido

- Viewport que quedaba mal calculado (ventana pequeña en la esquina) al maximizar o cambiar el tamaño de la ventana en cualquiera de las pantallas.

### Cambiado

- `GodOfWarOrigins` pasó de `ApplicationAdapter` a `Game`, delegando toda la lógica de juego a `GameScreen`.

## [0.1.0] - 2026-07-09

### Agregado

- Creación del proyecto utilizando LibGDX 1.13.5.
- Configuración inicial con Gradle.
- Estructura base del proyecto.
- Configuración del repositorio Git.
- Incorporación del archivo README.md.
- Creación e inicialización del archivo CHANGELOG.md.
- Publicación de la propuesta del proyecto en la Wiki del repositorio.

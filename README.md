# God of War Origins

Proyecto final de **Laboratorio y Programación** desarrollado en **Java** utilizando **LibGDX**.

## Descripción

**God of War Origins** es un videojuego cooperativo de supervivencia en 2D con perspectiva cenital, inspirado en la mitología nórdica y ambientado en el período previo a los acontecimientos de *God of War (2018)*.

Los jugadores controlan a **Kratos** y **Laufey**, quienes deberán explorar Midgard, recolectar recursos, construir estructuras, enfrentarse a enemigos y colaborar para fortalecer una barrera mágica que protege su hogar. El juego podrá disfrutarse tanto en modo individual como cooperativo mediante una red local, siendo la implementación de la comunicación en red uno de los ejes principales del proyecto. :contentReference[oaicite:1]{index=1}

---

## Características principales

- Modo individual y cooperativo.
- Arquitectura cliente-servidor.
- Comunicación mediante TCP y UDP.
- Exploración de un mundo 2D.
- Sistema de combate en tiempo real.
- Recolección de recursos.
- Construcción de estructuras.
- Inventario y comercio.
- Inteligencia artificial para enemigos.
- Progresión basada en objetivos.

---

## Tecnologías

- Java 21 LTS
- LibGDX 1.13.5
- Gradle
- Git
- GitHub
- IntelliJ IDEA

---

## Integrantes

- Manuel Coggiola Molinari
- Felipe Pavon Martorelli

---

## Estado del proyecto

### En desarrollo.

Actualmente el repositorio contiene la estructura inicial del proyecto generada con LibGDX. Durante las siguientes etapas se incorporarán progresivamente los sistemas de movimiento, combate, construcción, inteligencia artificial, comunicación en red y progresión principal. :contentReference[oaicite:2]{index=2}

---

## Instalación

### Requisitos

- Java JDK 21 o superior
- IntelliJ IDEA (recomendado)

### Clonar el repositorio

```bash
git clone https://github.com/Manuel-Coggiola-Molinari/God-of-War-Origins.git
```

### Ejecutar

1. Abrir el proyecto con IntelliJ IDEA.
2. Esperar a que Gradle descargue las dependencias.
3. Ejecutar la clase:

```
lwjgl3/src/main/java/com/goworigins/lwjgl3/Lwjgl3Launcher.java
```

También puede ejecutarse desde la terminal con:

```bash
./gradlew lwjgl3:run
```

En Windows:

```bash
gradlew.bat lwjgl3:run
```

---

## Documentación

La documentación del proyecto se encuentra en la **Wiki** del repositorio.

> *(El enlace se agregará una vez creada la Wiki.)*

---

## Licencia

Proyecto desarrollado con fines educativos para la materia **Laboratorio y Programación**.

# Changelog - Timōria

Todos los cambios notorios de este proyecto serán documentados en este archivo.

Este formato está basado en [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), y este proyecto adhiere a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unrealeased]
- Solucionar bug de bucle de sonido en personaje.java
- Revisar clase enemigo
- Creación de clase InputManager e implementarla en todos los Screens
- Eliminación de createMultiplexer
- Cambiar el tipo de clase importada de juego, de Principal a Game
- Incluir en el stage todos los Texture y Sprite que se impriman por Batch
- Pasar el cambio de inputProcesor al constructor de EscenaBase
- Añadir animación de salto
- Añadir tienda
- Añadir inventario
- Añadir mecánicas cooperativas
- Crear más niveles
- Hacer que la cámara siga al personaje en un nivel más grande
- Sacar imports innecesarios de la clase Principal

##[v0.1.1] - 2025-10-3
### Changed
- Refactorización de personaje
- Creación de clase crearCuerpo, llamada ne el constructor
- 

## [v0.1.0] - 2025-08-24
### Added

- Implementar InputProcessor
- Crear un jugador y ser capaz de moverlo con las teclas (a-d) y que pueda saltar con la tecla (w)
- Añadir animaciones
- Añadir fondo al nivel
- Añadir fondo al menu de inicio
- Cámara que siga al jugador
- Implementar viewports
- Añadir menú de inicio
- Añadir menú de pausa 
- Añadir pantalla de gameover con frases que varian aleatoriamente
- Diseñar hitbox de personaje y plataformas
- Implementar interacción con botones mediante colision
- Enemigo que ataca y sigue al jugador
- Añadir música de fondo
- Añadir efectos de sonido de daño
- Implementar controles para el sonido/música (se encuentran en los menus)
- Añadir una forma de ganar
- Diseñar nivel con plataforma

## [v0.1.0] - 2025-05-24
### Added
- Proyecto de libGDX inicializado, con todas las extensiones necesarias 
- README.md con la información necesaria y los detalles del proyecto
- Creación de este archivo `CHANGELOG.md`, siguiendo el formato "Keep a Changelog".
- Configuración inicial de la Wiki del proyecto en GitHub con la propuesta formal completa.
- Configuración del repositorio remoto en GitHub para colaboración y control de versiones.

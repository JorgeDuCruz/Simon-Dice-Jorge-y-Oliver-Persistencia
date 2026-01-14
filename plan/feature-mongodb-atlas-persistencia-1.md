---
goal: Integración de MongoDB Atlas para la persistencia de datos del juego Simon Dice, manteniendo la funcionalidad actual de Room/SQLite
version: 1.0
date_created: 2026-01-14
last_updated: 2026-01-14
owner: Equipo de Desarrollo
status: 'Planned'
tags: ['feature', 'database', 'mongodb', 'cloud', 'persistence', 'architecture']
---

# Introducción

![Estado: Planned](https://img.shields.io/badge/status-Planned-blue)

Este plan detalla la integración de **MongoDB Atlas** como sistema de persistencia en la nube para la aplicación "Simon Dice", manteniendo la funcionalidad actual de Room/SQLite como opción de almacenamiento local. La solución permitirá a los usuarios sincronizar sus récords en la nube mediante MongoDB Atlas, ofreciendo una experiencia mejorada sin comprometer la estabilidad existente.

## 1. Requisitos y Restricciones

### Requisitos Funcionales
- **REQ-001**: Implementar un controlador MongoDB que implemente la interfaz `HandlerRecord` existente
- **REQ-002**: Permitir almacenamiento de récords en MongoDB Atlas en la nube
- **REQ-003**: Mantener compatibilidad total con la funcionalidad actual de Room/SQLite
- **REQ-004**: Implementar un patrón de estrategia para permitir cambiar entre Room y MongoDB
- **REQ-005**: Sincronizar récords locales con MongoDB Atlas de forma asíncrona
- **REQ-006**: Mostrar el récord actual desde la fuente de datos activa (local o remota)

### Requisitos No Funcionales
- **PERF-001**: Las operaciones de lectura/escritura en MongoDB no deben bloquear el hilo principal
- **PERF-002**: La latencia de sincronización debe ser transparente para el usuario
- **PERF-003**: Soportar funcionamiento offline con sincronización cuando la red esté disponible
- **SEC-001**: Las credenciales de MongoDB Atlas deben almacenarse de forma segura
- **SEC-002**: Usar HTTPS para todas las comunicaciones con MongoDB Atlas
- **ARCH-001**: La arquitectura debe seguir el patrón MVVM existente
- **ARCH-002**: Usar coroutines de Kotlin para operaciones asíncronas

### Restricciones
- **RES-001**: No modificar la interfaz `HandlerRecord` existente
- **RES-002**: No eliminar la funcionalidad de Room/SQLite
- **RES-003**: Mantener compatibilidad con Android API 30+
- **RES-004**: No introducir dependencias conflictivas con las existentes

### Patrones a Seguir
- **PAT-001**: Implementar el patrón Strategy para la selección de base de datos
- **PAT-002**: Usar RepositoryPattern para abstraer la fuente de datos
- **PAT-003**: Implementar ViewModel con LiveData para reactividad
- **PAT-004**: Usar Kotlin Coroutines para operaciones asíncronas

### Guías
- **GUD-001**: Documentar el proceso de configuración de MongoDB Atlas
- **GUD-002**: Proporcionar ejemplos de uso del nuevo controlador

## 2. Pasos de Implementación

### Fase 1: Configuración de Dependencias y Preparación de la Arquitectura

**GOAL-001**: Establecer las dependencias necesarias para MongoDB y configurar la estructura base para la integración sin afectar el código existente.

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-001 | Agregar dependencia del MongoDB Driver para Kotlin (`mongodb-driver-coroutine`) en `app/build.gradle.kts` | | |
| TASK-002 | Agregar dependencia de Retrofit o HttpClient para comunicación REST si se usa Realm o REST API de MongoDB | | |
| TASK-003 | Crear directorio `/app/src/main/java/gz/dam/simondicejorgeoliver/Model/MongoDB/` para albergar clases MongoDB | | |
| TASK-004 | Crear clase `MongoDBRecordEntity.kt` como modelo de datos compatible con MongoDB (sin anotaciones de Room) | | |
| TASK-005 | Actualizar `AndroidManifest.xml` con permisos de Internet si no existen: `android.permission.INTERNET` | | |

### Fase 2: Implementación del Controlador MongoDB

**GOAL-002**: Crear la clase controladora que implemente `HandlerRecord` para gestionar operaciones con MongoDB Atlas.

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-006 | Crear clase `ControllerMongoDBAtlas.kt` que implemente `HandlerRecord` con métodos `setRecord()` y `getRecord()` | | |
| TASK-007 | Implementar método `setRecord()` para insertar/actualizar récords en MongoDB Atlas usando coroutines | | |
| TASK-008 | Implementar método `getRecord()` para obtener el récord máximo de MongoDB Atlas | | |
| TASK-009 | Implementar manejo de excepciones y logging para operaciones MongoDB | | |
| TASK-010 | Crear clase `MongoDBClient.kt` para encapsular la conexión y lógica de comunicación con MongoDB Atlas | | |
| TASK-011 | Implementar configuración de credenciales MongoDB (URI de conexión) de forma segura | | |

### Fase 3: Creación del Patrón Strategy para Selección de Base de Datos

**GOAL-003**: Implementar un patrón que permita cambiar dinámicamente entre Room y MongoDB sin afectar el resto de la aplicación.

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-012 | Crear clase `RecordRepository.kt` que implemente el patrón Repository | | |
| TASK-013 | Implementar método para obtener la instancia de `HandlerRecord` según la configuración (local o remota) | | |
| TASK-014 | Crear clase `DatabaseConfig.kt` con opciones de configuración para seleccionar la estrategia de almacenamiento | | |
| TASK-015 | Crear archivo de configuración (SharedPreferences o archivo de propiedades) para seleccionar la BD activa | | |

### Fase 4: Integración con la UI y ViewModel

**GOAL-004**: Actualizar la capa de presentación para utilizar el nuevo sistema de persistencia de forma reactiva.

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-016 | Actualizar ViewModels para usar `RecordRepository` en lugar de acceso directo al controlador | | |
| TASK-017 | Implementar manejo de estados (loading, success, error) en ViewModel para operaciones MongoDB | | |
| TASK-018 | Actualizar composables para mostrar estados de carga y manejo de errores | | |
| TASK-019 | Implementar sincronización de datos locales a MongoDB cuando el usuario lo requiera | | |

### Fase 5: Gestión de Sincronización y Offline-First

**GOAL-005**: Implementar capacidad de funcionamiento offline con sincronización automática cuando la red esté disponible.

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-020 | Crear clase `SyncManager.kt` para gestionar la sincronización entre Room y MongoDB | | |
| TASK-021 | Implementar cola de sincronización para guardar cambios locales cuando no hay conexión | | |
| TASK-022 | Crear clase `NetworkObserver.kt` para detectar cambios en la disponibilidad de red | | |
| TASK-023 | Implementar sincronización automática cuando se restablece la conexión | | |

### Fase 6: Documentación y Guías de Configuración

**GOAL-006**: Proporcionar documentación completa para la configuración y uso del nuevo sistema de MongoDB Atlas.

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-024 | Crear `MONGODB_SETUP.md` con guía paso a paso para configurar MongoDB Atlas | | |
| TASK-025 | Crear `API_CONFIGURATION.md` documentando las credenciales y variables de entorno necesarias | | |
| TASK-026 | Crear archivo de ejemplo `.env.example` con variables de configuración requeridas | | |
| TASK-027 | Actualizar `README.md` con sección sobre persistencia en MongoDB Atlas | | |

### Fase 7: Testing y Validación

**GOAL-007**: Crear pruebas unitarias e integración para garantizar el funcionamiento correcto de la nueva funcionalidad.

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-028 | Crear pruebas unitarias para `ControllerMongoDBAtlas.kt` | | |
| TASK-029 | Crear pruebas de integración para `RecordRepository.kt` | | |
| TASK-030 | Crear pruebas para `SyncManager.kt` | | |
| TASK-031 | Realizar pruebas de funcionamiento offline | | |
| TASK-032 | Validar sincronización de datos entre Room y MongoDB | | |

## 3. Alternativas

- **ALT-001**: Usar Firebase Firestore en lugar de MongoDB Atlas
  - **Justificación de rechazo**: MongoDB Atlas ofrece mayor flexibilidad y control sobre el almacenamiento, además de ser la opción solicitada específicamente.

- **ALT-002**: Implementar una API REST propia en servidor en lugar de conectar directamente a MongoDB
  - **Justificación de rechazo**: Aunque es más seguro para producción, añade complejidad innecesaria en esta fase. Puede implementarse posteriormente como mejora.

- **ALT-003**: Usar Room como única fuente de verdad y sincronizar en background
  - **Justificación de rechazo**: La arquitectura propuesta permite mayor flexibilidad y soporta futuros cambios.

## 4. Dependencias

### Librerías Externas
- **DEP-001**: `mongodb-driver-coroutine:4.10.0+` - Driver oficial de MongoDB para Kotlin con soporte de coroutines
- **DEP-002**: `org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0+` - Serialización JSON para modelos MongoDB
- **DEP-003**: `com.squareup.okhttp3:okhttp:4.10.0+` - HTTP client para conectar con MongoDB Atlas

### Servicios Externos
- **DEP-004**: MongoDB Atlas (https://www.mongodb.com/cloud/atlas) - Base de datos en la nube

### Dependencias Internas Existentes
- **DEP-005**: `androidx.room:room-runtime:2.8.4` - Mantener para persistencia local
- **DEP-006**: `kotlinx-coroutines` - Ya incluida, será reutilizada
- **DEP-007**: `androidx.lifecycle:lifecycle-runtime-ktx` - Ya incluida, será reutilizada

## 5. Archivos

### Archivos a Crear
- **FILE-001**: `/app/src/main/java/gz/dam/simondicejorgeoliver/Model/MongoDB/MongoDBRecordEntity.kt` - Modelo de datos para MongoDB
- **FILE-002**: `/app/src/main/java/gz/dam/simondicejorgeoliver/Model/MongoDB/MongoDBClient.kt` - Cliente de conexión a MongoDB Atlas
- **FILE-003**: `/app/src/main/java/gz/dam/simondicejorgeoliver/Controller/ControllerMongoDBAtlas.kt` - Controlador para operaciones MongoDB
- **FILE-004**: `/app/src/main/java/gz/dam/simondicejorgeoliver/Controller/RecordRepository.kt` - Patrón Repository para abstracción de datos
- **FILE-005**: `/app/src/main/java/gz/dam/simondicejorgeoliver/Controller/DatabaseConfig.kt` - Configuración de estrategia de BD
- **FILE-006**: `/app/src/main/java/gz/dam/simondicejorgeoliver/Model/SyncManager.kt` - Gestor de sincronización
- **FILE-007**: `/app/src/main/java/gz/dam/simondicejorgeoliver/Utility/NetworkObserver.kt` - Observador de conectividad
- **FILE-008**: `/plan/MONGODB_SETUP.md` - Guía de configuración de MongoDB Atlas
- **FILE-009**: `/plan/API_CONFIGURATION.md` - Documentación de configuración de API
- **FILE-010**: `/.env.example` - Archivo de ejemplo para variables de entorno

### Archivos a Modificar
- **FILE-011**: `/app/build.gradle.kts` - Agregar dependencias de MongoDB
- **FILE-012**: `/app/src/main/AndroidManifest.xml` - Verificar permisos de Internet
- **FILE-013**: `/README.md` - Actualizar documentación de persistencia
- **FILE-014**: Archivos ViewModel existentes - Integrar `RecordRepository`
- **FILE-015**: Composables de UI - Manejar estados de carga y error

## 6. Pruebas (Testing)

### Pruebas Unitarias
- **TEST-001**: Prueba que `ControllerMongoDBAtlas.setRecord()` inserta correctamente un récord en MongoDB
- **TEST-002**: Prueba que `ControllerMongoDBAtlas.getRecord()` recupera el récord máximo correctamente
- **TEST-003**: Prueba que `RecordRepository` cambia entre Room y MongoDB según configuración
- **TEST-004**: Prueba que `MongoDBClient` maneja excepciones de conexión
- **TEST-005**: Prueba que `SyncManager` sincroniza datos correctamente entre Room y MongoDB

### Pruebas de Integración
- **TEST-006**: Prueba flujo completo: guardar récord en MongoDB y recuperarlo
- **TEST-007**: Prueba funcionamiento offline: guardar en Room y sincronizar cuando hay conexión
- **TEST-008**: Prueba cambio de estrategia de BD durante ejecución de la aplicación
- **TEST-009**: Prueba manejo de errores cuando MongoDB no está disponible

### Pruebas de UI
- **TEST-010**: Verificar que la UI muestra estado de carga durante sincronización
- **TEST-011**: Verificar que la UI muestra errores cuando falla la conexión a MongoDB
- **TEST-012**: Verificar que el récord mostrado es correcto después de insertar

## 7. Riesgos y Suposiciones

### Riesgos
- **RISK-001**: Latencia de red puede causar UI bloqueada - **Mitigación**: Usar coroutines y threads separados para operaciones MongoDB
- **RISK-002**: Credenciales de MongoDB expuestas - **Mitigación**: Almacenar en variables de entorno segurizadas, nunca en código fuente
- **RISK-003**: Conflictos de datos entre Room y MongoDB - **Mitigación**: Implementar `SyncManager` robusto con timestamps
- **RISK-004**: Compatibilidad con versiones antiguas de Android - **Mitigación**: Mantener minSdk en 30+
- **RISK-005**: Consumo excesivo de datos por sincronización - **Mitigación**: Implementar sincronización inteligente solo cuando sea necesario

### Suposiciones
- **ASSUMPTION-001**: Se asume que el usuario tendrá conexión a Internet para usar funcionalidad de MongoDB (offline-first mitiga esto)
- **ASSUMPTION-002**: Se asume que MongoDB Atlas estará disponible y funcional
- **ASSUMPTION-003**: Se asume que Room seguirá siendo la opción predeterminada para no romper experiencia actual
- **ASSUMPTION-004**: Se asume que la aplicación continuará usada principalmente en una sola sesión, simplificando sincronización

## 8. Especificaciones Relacionadas / Lecturas Adicionales

- [MongoDB Atlas Official Documentation](https://docs.atlas.mongodb.com/)
- [MongoDB Kotlin Driver Documentation](https://www.mongodb.com/docs/drivers/kotlin/)
- [Android Architecture Components - Repository Pattern](https://developer.android.com/topic/architecture/data-layer)
- [Kotlin Coroutines - Best Practices](https://kotlinlang.org/docs/coroutines-basics.html)
- [Jetpack Compose - State Management](https://developer.android.com/jetpack/compose/state)
- [Room Database Documentation](https://developer.android.com/training/data-storage/room)
- [MVVM Architecture Pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)


---
goal: Integración de MongoDB Atlas para persistencia remota de datos de juego, manteniendo SQLite y Shared Preferences como almacenamiento local
version: 1.0
date_created: 2026-01-16
last_updated: 2026-01-16
owner: Equipo de Desarrollo Simon-Dice
status: 'Planned'
tags: ['feature', 'database', 'architecture', 'cloud', 'mongodb', 'sync', 'offline-first']
---

# Introducción

![Estado: Planned](https://img.shields.io/badge/status-Planned-blue)

Este plan de implementación describe la integración de MongoDB Atlas como base de datos remota para la aplicación Simon-Dice. El objetivo es habilitar sincronización bidireccional de datos entre el almacenamiento local (SQLite y Shared Preferences) y MongoDB Atlas en la nube, manteniendo la funcionalidad offline-first y sin afectar la arquitectura actual de MVVM basada en Kotlin y Jetpack Compose.

La integración seguirá el patrón Repository y reutilizará la interfaz `HandlerRecord` existente para mantener la abstracción de persistencia.

## 1. Requisitos y Restricciones

### Requisitos Funcionales

- **REQ-001**: Implementar un controlador para MongoDB Atlas que implemente la interfaz `HandlerRecord`
- **REQ-002**: Sincronizar automáticamente los datos de récords entre SQLite local y MongoDB Atlas
- **REQ-003**: Mantener la funcionalidad offline-first: la aplicación debe funcionar sin conexión a internet
- **REQ-004**: Proporcionar un repositorio centralizado que maneje la lógica de sincronización
- **REQ-005**: Implementar autenticación segura contra MongoDB Atlas (credenciales cifradas)
- **REQ-006**: Permitir consultas en línea a MongoDB para comparar récords globales (opcional en fase 1)
- **REQ-007**: Mantener compatibilidad con Shared Preferences como almacenamiento de configuración local

### Requisitos No-Funcionales

- **REQ-008**: Las operaciones de lectura/escritura en MongoDB no deben bloquear la interfaz de usuario (usar corrutinas)
- **REQ-009**: La sincronización debe completarse en máximo 5 segundos cuando hay conexión
- **REQ-010**: La aplicación debe detectar automáticamente cambios de conectividad
- **REQ-011**: Los datos deben estar encriptados en tránsito (HTTPS/TLS)

### Restricciones de Arquitectura

- **RES-001**: Mantener la estructura actual del proyecto (carpetas: Controller, Model, Utility, Main, UI)
- **RES-002**: No modificar la interfaz `HandlerRecord` existente (garantizar retro-compatibilidad)
- **RES-003**: La arquitectura debe ser escalable para futuros proveedores de persistencia (Firebase, etc.)
- **RES-004**: Seguir el patrón MVVM sin cambios sustanciales en MyViewModel
- **RES-005**: Usar Kotlin y Jetpack Compose según la versión actual del proyecto

### Restricciones de Seguridad

- **SEC-001**: No almacenar credenciales de MongoDB en código fuente (usar variables de entorno o configuración segura)
- **SEC-002**: Implementar validación de datos antes de enviar a MongoDB
- **SEC-003**: Usar conexiones encriptadas a MongoDB Atlas
- **SEC-004**: Implementar manejo de errores que no exponga información sensible

### Guías y Patrones

- **GUD-001**: Usar corrutinas (kotlinx.coroutines) para todas las operaciones asincrónicas
- **GUD-002**: Implementar ViewModel como punto de entrada para acceso a datos
- **GUD-003**: Usar MutableStateFlow para exponer estado reactivo
- **GUD-004**: Aplicar inyección de dependencias manualmente o con Hilt (opcional)
- **GUD-005**: Documentar todas las funciones públicas con KDoc

### Dependencias Externas Requeridas

- **PAT-001**: Usar Realm Kotlin SDK o MongoDB Kotlin Driver como cliente MongoDB
- **PAT-002**: Usar WorkManager para programar sincronización en segundo plano
- **PAT-003**: Usar ConnectivityManager para detectar cambios de conectividad
- **PAT-004**: Usar DataStore o SharedPreferences para almacenar tokens/configuración de conexión

## 2. Pasos de Implementación

### Fase 1: Configuración y Setup de Dependencias

- **GOAL-001**: Preparar el entorno del proyecto con todas las dependencias necesarias para MongoDB y sincronización

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-001 | Agregar dependencia de MongoDB Kotlin Driver (org.mongodb:mongodb-driver-kotlin-coroutine) en app/build.gradle.kts | | |
| TASK-002 | Agregar dependencia de WorkManager (androidx.work:work-runtime-ktx) para sincronización en background | | |
| TASK-003 | Agregar dependencia de ConnectivityManager (parte de framework Android) y LiveData para detectar conectividad | | |
| TASK-004 | Crear archivo de configuración local.properties para almacenar MONGODB_URI (con instrucciones de setup en README) | | |
| TASK-005 | Crear clase BuildConfig.kt para gestionar variables de compilación (MONGODB_URI, SYNC_INTERVAL) | | |
| TASK-006 | Validar que todas las dependencias se resuelven correctamente ejecutando `./gradlew assembleDebug` | | |

### Fase 2: Modelado de Datos y Entidades MongoDB

- **GOAL-002**: Crear las entidades y modelos necesarios para representar datos en MongoDB mientras se mantiene compatibilidad con SQLite

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-007 | Crear clase `RecordMongo.kt` en app/src/main/java/gz/dam/simondicejorgeoliver/Model/ con campos: id (ObjectId), userId, puntuacion, fecha, dispositivo, version | | |
| TASK-008 | Agregar anotaciones @Serializable (kotlinx.serialization) a RecordMongo para facilitar serialización/deserialización JSON | | |
| TASK-009 | Crear clase `SyncStatus.kt` para rastrear estado de sincronización: PENDING, SYNCED, FAILED, IN_PROGRESS | | |
| TASK-010 | Crear data class `SyncRecord.kt` que combine campos locales y remotos para seguimiento de sincronización | | |
| TASK-011 | Agregar campo de timestamp sincronización a RecordEntity (SQLite) mediante migración de Room | | |
| TASK-012 | Crear mapeadores (mappers) entre RecordEntity (Room), RecordMongo (MongoDB) y Record (lógica de negocio) | | |

### Fase 3: Implementar Controlador de MongoDB

- **GOAL-003**: Crear un controlador que implemente `HandlerRecord` para gestionar operaciones con MongoDB

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-013 | Crear clase `ControllerMongoDBAtlas.kt` en app/src/main/java/gz/dam/simondicejorgeoliver/Controller/ | | |
| TASK-014 | Implementar interfaz `HandlerRecord` en ControllerMongoDBAtlas con métodos: addRecord(), getRecord(), getAllRecords(), deleteRecord(), updateRecord() | | |
| TASK-015 | Crear inicializador de cliente MongoDB en ControllerMongoDBAtlas.init() usando MONGODB_URI de configuración | | |
| TASK-016 | Implementar getRecord() para consultar un récord por ID en MongoDB | | |
| TASK-017 | Implementar getAllRecords() para obtener todos los récords del usuario actual desde MongoDB | | |
| TASK-018 | Implementar addRecord() para insertar nuevos récords en MongoDB (asincrónico con corrutinas) | | |
| TASK-019 | Implementar updateRecord() para actualizar récords existentes en MongoDB | | |
| TASK-020 | Implementar deleteRecord() para eliminar récords de MongoDB | | |
| TASK-021 | Agregar manejo de errores con try-catch y logging detallado usando Log.e() | | |

### Fase 4: Detectar y Gestionar Conectividad

- **GOAL-004**: Implementar detección automática de cambios en la conectividad de red para habilitar sincronización offline-first

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-022 | Crear clase `ConnectivityObserver.kt` en app/src/main/java/gz/dam/simondicejorgeoliver/Utility/ | | |
| TASK-023 | Implementar ConnectivityObserver usando ConnectivityManager para detectar cambios de red | | |
| TASK-024 | Exponer estado de conectividad mediante MutableStateFlow<Boolean> | | |
| TASK-025 | Crear servicio que escuche cambios de conectividad y ejecute sincronización cuando haya conexión | | |
| TASK-026 | Agregar permiso INTERNET y CHANGE_NETWORK_STATE en AndroidManifest.xml | | |

### Fase 5: Implementar Repositorio de Sincronización

- **GOAL-005**: Crear un repositorio centralizado que orqueste la sincronización entre SQLite local y MongoDB remoto

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-027 | Crear clase `RecordRepository.kt` en app/src/main/java/gz/dam/simondicejorgeoliver/Controller/ | | |
| TASK-028 | Implementar método syncLocalToRemote() que: obtiene registros locales sin sincronizar → envía a MongoDB → marca como sincronizados | | |
| TASK-029 | Implementar método syncRemoteToLocal() que: obtiene registros nuevos de MongoDB → inserta en SQLite local → marca como sincronizados | | |
| TASK-030 | Implementar método bidirectionalSync() que ejecuta ambos sincronizaciones con manejo de conflictos | | |
| TASK-031 | Agregar lógica de resolución de conflictos: usar timestamp más reciente como fuente de verdad | | |
| TASK-032 | Crear método scheduleBackgroundSync() usando WorkManager para sincronización periódica cada 30 minutos | | |
| TASK-033 | Implementar sincronización manual triggerada por botón en UI (opcional en fase 1) | | |

### Fase 6: Integración en ViewModel y UI

- **GOAL-006**: Integrar el repositorio de sincronización en el ViewModel existente sin romper funcionalidad actual

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-034 | Modificar MyViewModel.kt para inyectar RecordRepository como dependencia | | |
| TASK-035 | Agregar MutableStateFlow<SyncStatus> en MyViewModel para exponer estado de sincronización a la UI | | |
| TASK-036 | Llamar a repository.bidirectionalSync() cuando se completa una partida (en método derrota()) | | |
| TASK-037 | Implementar observador de conectividad en MyViewModel que escuche cambios de red | | |
| TASK-038 | Llamar a scheduleBackgroundSync() en onCreate() del MainActivity para iniciar sincronización periódica | | |
| TASK-039 | Modificar UI.kt para mostrar indicador visual de estado de sincronización (sincronizando/sincronizado/error) | | |
| TASK-040 | Agregar try-catch en MyViewModel para capturar excepciones de sincronización y evitar crashes | | |

### Fase 7: Configuración de MongoDB Atlas

- **GOAL-007**: Realizar configuración manual en MongoDB Atlas para el acceso desde la aplicación

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-041 | Crear cuenta en MongoDB Atlas (https://www.mongodb.com/cloud/atlas) | | |
| TASK-042 | Crear cluster M0 (gratuito) con nombre "simon-dice-db" | | |
| TASK-043 | Crear base de datos "simonDiceDB" y colección "records" | | |
| TASK-044 | Crear usuario de base de datos con permisos específicos (no usar admin) en Atlas | | |
| TASK-045 | Obtener MONGODB_URI de Atlas en formato: mongodb+srv://usuario:password@cluster.mongodb.net/simonDiceDB | | |
| TASK-046 | Configurar IP whitelist en Atlas para permitir acceso desde cualquier IP (0.0.0.0/0) para desarrollo | | |
| TASK-047 | Documentar proceso de setup en README.md con instrucciones paso a paso | | |

### Fase 8: Implementar Cifrado y Seguridad

- **GOAL-008**: Asegurar que credenciales y datos en tránsito estén protegidos

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-048 | Crear clase `EncryptionUtils.kt` en app/src/main/java/gz/dam/simondicejorgeoliver/Utility/ | | |
| TASK-049 | Implementar cifrado de MONGODB_URI usando EncryptedSharedPreferences (androidx.security:security-crypto) | | |
| TASK-050 | Almacenar MONGODB_URI cifrado en SharedPreferences en lugar de código fuente | | |
| TASK-051 | Validar URL de MongoDB y detectar anomalías de seguridad (HTTPS, validar certificado) | | |
| TASK-052 | Implementar validación de entrada en todos los métodos que reciben datos de MongoDB | | |

### Fase 9: Testing

- **GOAL-009**: Implementar pruebas unitarias e instrumentadas para validar sincronización

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-053 | Crear pruebas unitarias en app/src/test/java/gz/dam/simondicejorgeoliver/RecordRepositoryTest.kt | | |
| TASK-054 | Implementar test para bidirectionalSync() con datos mock | | |
| TASK-055 | Implementar test para resolución de conflictos (timestamp más reciente gana) | | |
| TASK-056 | Implementar test para ConnectivityObserver | | |
| TASK-057 | Crear pruebas instrumentadas en app/src/androidTest/ para validar integración completa | | |
| TASK-058 | Implementar test offline: verificar que aplicación funciona sin conexión | | |
| TASK-059 | Implementar test online: verificar que sincronización se ejecuta cuando hay conexión | | |

### Fase 10: Documentación y Finalización

- **GOAL-010**: Documentar la implementación y preparar para deployment

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-060 | Crear documentación en docs/ con diagrama de arquitectura de sincronización | | |
| TASK-061 | Documentar flujo de sincronización: offline → online → remoto → local | | |
| TASK-062 | Crear archivo SETUP_MONGODB.md con guía paso a paso para configurar MongoDB Atlas | | |
| TASK-063 | Actualizar README.md con instrucciones de uso de nueva funcionalidad MongoDB | | |
| TASK-064 | Crear archivo TROUBLESHOOTING.md con soluciones a problemas comunes | | |
| TASK-065 | Verificar que la aplicación compila sin warnings: `./gradlew build` | | |
| TASK-066 | Ejecutar todas las pruebas: `./gradlew test connectedAndroidTest` | | |
| TASK-067 | Crear release tag v1.1.0 con nuevas funcionalidades de MongoDB | | |

## 3. Alternativas

- **ALT-001**: Usar Firebase Realtime Database en lugar de MongoDB Atlas - Razón rechazo: MongoDB proporciona más control, mejor para datos estructurados complejos, y mejor para educación
- **ALT-002**: Usar sincronización manual (solo triggered por usuario) - Razón rechazo: Sincronización automática en background mejora experiencia de usuario
- **ALT-003**: Almacenar MONGODB_URI en strings.xml - Razón rechazo: Menos seguro que EncryptedSharedPreferences y podría exponerse en APK
- **ALT-004**: Usar Room con Cloud Sync - Razón rechazo: No disponible en versión gratuita, MongoDB es más flexible
- **ALT-005**: Eliminar SQLite y usar solo MongoDB - Razón rechazo: Requisito explícito mantener funcionalidad offline-first

## 4. Dependencias

### Dependencias de Bibliotecas

- **DEP-001**: MongoDB Kotlin Driver (org.mongodb:mongodb-driver-kotlin-coroutine:latest) - Comunicación con MongoDB Atlas
- **DEP-002**: WorkManager (androidx.work:work-runtime-ktx:latest) - Sincronización en segundo plano
- **DEP-003**: EncryptedSharedPreferences (androidx.security:security-crypto:latest) - Almacenamiento seguro de credenciales
- **DEP-004**: Kotlinx Coroutines (org.jetbrains.kotlinx:kotlinx-coroutines-android:latest) - Ya instalado, sincronización asincrónica
- **DEP-005**: Room (androidx.room:room-*:2.8.4) - Ya instalado, persistencia local
- **DEP-006**: Jetpack Compose Material3 (androidx.compose.material3:*) - Ya instalado

### Dependencias de Configuración

- **DEP-007**: MongoDB Atlas account con credenciales configuradas
- **DEP-008**: Variables de entorno o local.properties con MONGODB_URI
- **DEP-009**: Android SDK 30+ (minSdk = 30 en proyecto actual)

### Dependencias de Conocimiento

- **DEP-010**: Documentación de MongoDB Atlas setup
- **DEP-011**: Documentación de MongoDB Kotlin Driver
- **DEP-012**: Documentación de WorkManager

## 5. Archivos

### Archivos Nuevos a Crear

- **FILE-001**: `app/src/main/java/gz/dam/simondicejorgeoliver/Model/RecordMongo.kt` - Entidad para MongoDB
- **FILE-002**: `app/src/main/java/gz/dam/simondicejorgeoliver/Model/SyncStatus.kt` - Enum de estado de sincronización
- **FILE-003**: `app/src/main/java/gz/dam/simondicejorgeoliver/Model/SyncRecord.kt` - Data class para seguimiento de sincronización
- **FILE-004**: `app/src/main/java/gz/dam/simondicejorgeoliver/Controller/ControllerMongoDBAtlas.kt` - Controlador MongoDB
- **FILE-005**: `app/src/main/java/gz/dam/simondicejorgeoliver/Controller/RecordRepository.kt` - Repositorio de sincronización
- **FILE-006**: `app/src/main/java/gz/dam/simondicejorgeoliver/Utility/ConnectivityObserver.kt` - Monitor de conectividad
- **FILE-007**: `app/src/main/java/gz/dam/simondicejorgeoliver/Utility/EncryptionUtils.kt` - Utilidades de cifrado
- **FILE-008**: `app/src/main/java/gz/dam/simondicejorgeoliver/Utility/MongoDBMapper.kt` - Mapeadores de datos
- **FILE-009**: `app/src/main/java/gz/dam/simondicejorgeoliver/Utility/SyncWorker.kt` - Worker para sincronización en background
- **FILE-010**: `app/src/test/java/gz/dam/simondicejorgeoliver/RecordRepositoryTest.kt` - Pruebas unitarias
- **FILE-011**: `docs/ARCHITECTURE_MONGODB.md` - Documentación de arquitectura
- **FILE-012**: `docs/SETUP_MONGODB.md` - Guía de configuración MongoDB Atlas

### Archivos Existentes a Modificar

- **FILE-013**: `app/build.gradle.kts` - Agregar dependencias: MongoDB Kotlin Driver, WorkManager, EncryptedSharedPreferences
- **FILE-014**: `app/src/main/AndroidManifest.xml` - Agregar permisos: INTERNET, CHANGE_NETWORK_STATE
- **FILE-015**: `app/src/main/java/gz/dam/simondicejorgeoliver/MyViewModel.kt` - Inyectar RecordRepository, agregar estado de sincronización
- **FILE-016**: `app/src/main/java/gz/dam/simondicejorgeoliver/MainActivity.kt` - Inicializar sincronización en background
- **FILE-017**: `app/src/main/java/gz/dam/simondicejorgeoliver/UI.kt` - Agregar indicador visual de sincronización
- **FILE-018**: `app/src/main/java/gz/dam/simondicejorgeoliver/Model/RecordEntity.kt` - Agregar timestamp_sincronizacion
- **FILE-019**: `README.md` - Agregar instrucciones MongoDB
- **FILE-020**: `local.properties` - Agregar configuración MONGODB_URI (no comitear)

## 6. Pruebas (Testing)

### Pruebas Unitarias

- **TEST-001**: `test_addRecord_success()` - Verificar que addRecord() inserta correctamente en MongoDB
- **TEST-002**: `test_getRecord_success()` - Verificar que getRecord() recupera récord de MongoDB por ID
- **TEST-003**: `test_getAllRecords_success()` - Verificar que getAllRecords() retorna lista completa
- **TEST-004**: `test_bidirectionalSync_no_conflicts()` - Sincronización sin conflictos
- **TEST-005**: `test_bidirectionalSync_with_conflicts()` - Sincronización con conflictos se resuelve por timestamp
- **TEST-006**: `test_connectivity_observer_detects_online()` - Observer detecta cambio a online
- **TEST-007**: `test_connectivity_observer_detects_offline()` - Observer detecta cambio a offline
- **TEST-008**: `test_scheduleBackgroundSync()` - Verificar que WorkManager se programa correctamente

### Pruebas Instrumentadas (Android Device/Emulator)

- **TEST-009**: `test_offline_mode_local_only()` - Sin conexión, operaciones usan SQLite local
- **TEST-010**: `test_online_sync_executes()` - Con conexión, sincronización se ejecuta automáticamente
- **TEST-011**: `test_sync_completes_within_5_seconds()` - Rendimiento: sincronización < 5 segundos
- **TEST-012**: `test_app_doesnt_crash_on_network_timeout()` - Manejo robusto de timeouts
- **TEST-013**: `test_sync_status_ui_updates()` - UI actualiza estado de sincronización
- **TEST-014**: `test_encryption_utils_encrypt_decrypt()` - Cifrado de credenciales funciona
- **TEST-015**: `test_mapper_recordentity_to_recordmongo()` - Mapeo correcto entre tipos

## 7. Riesgos y Suposiciones

### Riesgos

- **RISK-001**: Exposición de credenciales MongoDB - Mitigación: Usar EncryptedSharedPreferences y variables de entorno
- **RISK-002**: Pérdida de sincronización durante apagado de app - Mitigación: Usar WorkManager con garantía de ejecución
- **RISK-003**: Exceso de solicitudes a MongoDB (costo) - Mitigación: Implementar rate limiting y sincronización periódica
- **RISK-004**: Conflictos de datos en sincronización bidireccional - Mitigación: Usar timestamp como criterio de resolución
- **RISK-005**: Latencia de red alta en usuarios con conexión lenta - Mitigación: Implementar timeouts y reintentos exponenciales
- **RISK-006**: Incompatibilidad con versiones futuras de Android - Mitigación: Usar APIs de Jetpack (APIs estables)
- **RISK-007**: Falta de capacidad de almacenamiento en MongoDB Atlas gratuito - Mitigación: Implementar políticas de retención/archivado

### Suposiciones

- **ASSUMPTION-001**: El usuario tiene acceso a internet ocasionalmente para sincronizar (offline-first pero requiere conexión periódica)
- **ASSUMPTION-002**: Los récords de usuarios tienen identificadores únicos (userId) que se pueden obtener del dispositivo
- **ASSUMPTION-003**: La latencia de MongoDB Atlas es aceptable (< 2 segundos por solicitud)
- **ASSUMPTION-004**: El cluster M0 gratuito de MongoDB es suficiente para fase inicial
- **ASSUMPTION-005**: Los datos de récords no son información altamente sensible pero requieren cifrado en tránsito
- **ASSUMPTION-006**: La aplicación solo sincroniza cuando está en foreground o usando WorkManager (backgrounding)

## 8. Especificaciones Relacionadas / Lecturas Adicionales

- [MongoDB Atlas Official Documentation](https://www.mongodb.com/docs/atlas/)
- [MongoDB Kotlin Driver Documentation](https://www.mongodb.com/docs/kotlin/current/)
- [Android WorkManager Documentation](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Android Security and Privacy - Encryption](https://developer.android.com/privacy-and-security/keystore)
- [Android Connectivity Monitoring](https://developer.android.com/training/monitoring-device-state/connectivity-status-type)
- [Jetpack Compose State Management](https://developer.android.com/jetpack/compose/state)
- [Kotlin Coroutines Best Practices](https://kotlinlang.org/docs/coroutines-basics.html)
- [MVVM Architecture Pattern Android](https://developer.android.com/jetpack/guide)


# Configuración de GitHub - Proyecto Simón Dice

Este directorio contiene la configuración, flujos de trabajo y herramientas que facilitan la colaboración, automatización y gestión del proyecto Simón Dice en GitHub.

---

## 📁 Estructura del Directorio

```
.github/
├── README.md                          # Este archivo
├── git-commit-instructions.md         # Guía de convenciones de commits
├── workflows/                         # GitHub Actions Workflows
│   └── gradle.yml                     # Pipeline de construcción y testing
├── prompts/                           # Plantillas de prompts para IA
│   ├── crear-Issue.prompt.md          # Plantilla para crear issues
│   ├── crear-readme.prompt.md         # Plantilla para crear READMEs
│   └── create-implementation-plan.prompt.md  # Plantilla para planes de implementación
```

---

## 🔧 Configuraciones Principales

### 1. **git-commit-instructions.md**

Archivo que define las convenciones de commits para el proyecto:

- **Idioma**: Todos los commits deben escribirse en **gallego** (idioma cooficial de Galicia)
- **Formato**: 
  - Línea 1: Mensaje corto y directo
  - Línea 2: Salto de línea (separación)
  - Línea 3+: Descripción más extensa y detallada
  - **Numeración**: Los commits se numeran de forma secuencial (1, 2, 3, ...) en la primera línea

**Ejemplo**:
```
1. Engadir compoñentes de Jetpack Compose

Implementouse os compoñentes necesarios para a interfaz de usuario:
- BotonesColores: compoñente para os 4 botones principais
- PantallaInicio: pantalla inicial con botón START
- Indicador de puntuación en tempo real
```

---

### 2. **workflows/gradle.yml**

Pipeline de CI/CD (Integración Continua / Despliegue Continuo) que automatiza la construcción y testing del proyecto.

#### Características:

- **Trigger**: Se ejecuta automáticamente en cada `push` a la rama `PruebaSharedPreference`
- **Sistema Operativo**: Ubuntu Latest (runners de GitHub)
- **Java Version**: OpenJDK 17 (compatible con el proyecto)
- **Pasos principales**:
  1. **Checkout**: Descarga el código del repositorio
  2. **Setup Java 17**: Configura el JDK necesario
  3. **Setup Gradle**: Prepara Gradle y cachea dependencias para optimizar tiempos
  4. **Build**: Ejecuta `./gradlew build` para compilar y testear el proyecto
  5. **Dependency Submission**: Genera un grafo de dependencias para alertas de Dependabot

#### Beneficios:

- ✅ **Compilación automática**: Cada push se compila automáticamente
- ✅ **Detección de errores temprana**: Identifica problemas de compilación rápidamente
- ✅ **Testing automático**: Ejecuta las pruebas unitarias e instrumentadas
- ✅ **Seguridad**: Monitoriza dependencias en busca de vulnerabilidades conocidas

---

### 3. **prompts/ - Plantillas para Automatización con IA**

Directorio que contiene plantillas de prompts diseñadas para facilitar la creación de contenido utilizando asistentes de IA (como GitHub Copilot o Claude).

#### 3.1 **crear-Issue.prompt.md**

Plantilla para crear issues de GitHub de forma estructurada y consistente.

**Características**:
- Define una estructura clara para la creación de issues
- Asegura que cada issue tenga título y descripción detallados
- Establece la necesidad de asignar etiquetas (labels) apropiadas
- Evita crear duplicados de issues ya existentes en planes
- Enfoque: Solo crear issues, sin contenido adicional

**Uso**: Copiar esta plantilla y usarla como prompt para generar issues de forma automática

#### 3.2 **crear-readme.prompt.md**

Plantilla para generar archivos README de forma automática.

**Propósito**: Facilitar la creación de documentación consistente y bien estructurada en diferentes partes del proyecto

#### 3.3 **create-implementation-plan.prompt.md**

Plantilla para crear planes de implementación detallados.

**Propósito**: Estructurar la planificación de nuevas funcionalidades con requisitos, fases, tareas y estimaciones

---

## 📋 Plan de Implementación MongoDB Atlas

Como parte de la evolución del proyecto, se ha planificado una **integración de MongoDB Atlas para persistencia remota de datos**. Este plan, documentado en `plan/feature-mongodb-atlas-persistencia-1.md`, incluye 10 fases de implementación con 67 tareas principales.

### Resumen de Issues por Fases

Los issues creados en GitHub están organizados según las 10 fases del plan. A continuación se presenta un resumen de cada grupo:

#### **Fase 1: Configuración y Setup de Dependencias** (TASK-001 a TASK-006)

Preparación del entorno del proyecto con todas las dependencias necesarias para MongoDB y sincronización.

- Agregar dependencias de MongoDB Kotlin Driver, WorkManager y EncryptedSharedPreferences
- Configurar variables de compilación (BuildConfig)
- Validar resolución de dependencias
- **Etiqueta**: `setup`, `dependencies`

#### **Fase 2: Modelado de Datos y Entidades MongoDB** (TASK-007 a TASK-012)

Creación de entidades y modelos para representar datos en MongoDB manteniendo compatibilidad con SQLite.

- Crear entidades `RecordMongo`, `SyncStatus`, `SyncRecord`
- Agregar anotaciones de serialización
- Crear mapeadores entre tipos de datos
- **Etiqueta**: `model`, `mongodb`, `database`

#### **Fase 3: Implementar Controlador de MongoDB** (TASK-013 a TASK-021)

Implementación de un controlador que gestione operaciones CRUD con MongoDB.

- Crear `ControllerMongoDBAtlas` implementando `HandlerRecord`
- Implementar métodos: `addRecord()`, `getRecord()`, `getAllRecords()`, `deleteRecord()`, `updateRecord()`
- Manejo robusto de errores y logging
- **Etiqueta**: `controller`, `mongodb`, `backend`

#### **Fase 4: Detectar y Gestionar Conectividad** (TASK-022 a TASK-026)

Sistema automático de detección de cambios en la conectividad de red.

- Crear `ConnectivityObserver` para monitorizar estado de red
- Detectar cambios entre online/offline
- Iniciar sincronización automáticamente cuando hay conexión
- **Etiqueta**: `networking`, `connectivity`, `sync`

#### **Fase 5: Implementar Repositorio de Sincronización** (TASK-027 a TASK-033)

Orquestación centralizada de sincronización bidireccional entre SQLite y MongoDB.

- Crear `RecordRepository` con lógica de sincronización
- Implementar `syncLocalToRemote()` y `syncRemoteToLocal()`
- Resolver conflictos usando timestamp como criterio
- Programar sincronización periódica con WorkManager
- **Etiqueta**: `repository`, `sync`, `architecture`

#### **Fase 6: Integración en ViewModel y UI** (TASK-034 a TASK-040)

Integración del repositorio en la arquitectura MVVM existente.

- Inyectar `RecordRepository` en `MyViewModel`
- Exponer estado de sincronización mediante `MutableStateFlow`
- Actualizar UI con indicador visual de sincronización
- Manejo de excepciones para evitar crashes
- **Etiqueta**: `viewmodel`, `ui`, `integration`

#### **Fase 7: Configuración de MongoDB Atlas** (TASK-041 a TASK-047)

Configuración manual en MongoDB Atlas (tareas de infraestructura en la nube).

- Crear cuenta y cluster en MongoDB Atlas
- Crear base de datos "simonDiceDB" y colección "records"
- Obtener credenciales de conexión (MONGODB_URI)
- Configurar whitelist de IPs
- Documentar proceso en README
- **Etiqueta**: `mongodb-atlas`, `infrastructure`, `setup`

#### **Fase 8: Implementar Cifrado y Seguridad** (TASK-048 a TASK-052)

Asegurar que credenciales y datos en tránsito estén protegidos.

- Crear `EncryptionUtils` para cifrado de credenciales
- Usar `EncryptedSharedPreferences` para almacenamiento seguro
- Validar entrada de datos y detectar anomalías
- Implementar HTTPS/TLS en conexiones
- **Etiqueta**: `security`, `encryption`, `credentials`

#### **Fase 9: Testing** (TASK-053 a TASK-059)

Pruebas unitarias e instrumentadas para validar sincronización.

- Crear suite de pruebas unitarias para `RecordRepository`
- Tests de sincronización bidireccional y resolución de conflictos
- Tests de `ConnectivityObserver`
- Pruebas offline/online
- Validar rendimiento (< 5 segundos)
- **Etiqueta**: `testing`, `unit-tests`, `qa`

#### **Fase 10: Documentación y Finalización** (TASK-060 a TASK-067)

Documentación completa y preparación para deployment.

- Crear diagrama de arquitectura de sincronización
- Guía de setup de MongoDB Atlas (`SETUP_MONGODB.md`)
- Archivo de troubleshooting
- Verificar compilación sin warnings
- Ejecutar todas las pruebas
- Crear release tag v1.1.0
- **Etiqueta**: `documentation`, `release`, `final`

---

## 🚀 Cómo Usar Esta Configuración

### Para Desarrolladores

1. **Leer las guías de commits**: Consulta `git-commit-instructions.md` antes de hacer commits
2. **Monitorizar el pipeline**: Los cambios se verifican automáticamente en GitHub Actions
3. **Consultar el plan**: Revisa `plan/feature-mongodb-atlas-persistencia-1.md` para entender el roadmap
4. **Seguir los issues**: Cada tarea tiene un issue asociado en GitHub

### Para Gestión del Proyecto

1. **Monitorizar el progreso**: Los issues están organizados por fases y labeled apropiadamente
2. **Usar proyectos de GitHub**: [Enlace al proyecto](https://github.com/users/oliver-miguez/projects/4)
3. **Generar reportes**: Los workflows proporcionan datos sobre salud del proyecto

### Para CI/CD

- El workflow `gradle.yml` se ejecuta automáticamente en cada push
- Los errores de compilación se reportan inmediatamente
- Las vulnerabilidades de dependencias se detectan con Dependabot

---

## 📚 Recursos Relacionados

- **Plan de Implementación**: `plan/feature-mongodb-atlas-persistencia-1.md`
- **Documentación MongoDB**: https://www.mongodb.com/docs/atlas/
- **Android WorkManager**: https://developer.android.com/topic/libraries/architecture/workmanager
- **GitHub Actions**: https://docs.github.com/en/actions
- **Kotlin Coroutines**: https://kotlinlang.org/docs/coroutines-basics.html

---

## 📞 Contacto y Soporte

Para preguntas sobre la configuración de GitHub o el plan de implementación, contacta con el equipo de desarrollo o consulta los issues existentes en el repositorio.

---

**Última actualización**: 19 de enero de 2026  
**Versión**: 1.0

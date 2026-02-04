---
agent: 'agent'
description: 'Crear un nuevo archivo de plan de implementación para nuevas funcionalidades, refactorización de código existente o actualización de paquetes, diseño, arquitectura o infraestructura.'
tools: ['changes', 'search/codebase', 'edit/editFiles', 'extensions', 'fetch', 'githubRepo', 'openSimpleBrowser', 'problems', 'runTasks', 'search', 'search/searchResults', 'runCommands/terminalLastCommand', 'runCommands/terminalSelection', 'testFailure', 'usages', 'vscodeAPI']
---

# Crear Plan de Implementación

## Directiva Primaria (Importante)

Tu objetivo es crear un nuevo archivo de plan de implementación para `${input:PlanPurpose}`. Tu salida debe ser legible por máquinas, determinista y estructurada para la ejecución autónoma por parte de otros sistemas de IA o humanos.
Usa el idioma "Español" para redactar el plan.
El plan debe seguir la estrucutura del proyecto actula de MVVM con Kotlin y Jetpack Compose para Android.
Siempre se asumira que el plan implica modificar el proyecto manteniendo las cualidades anteriores, siempre añadiendo o modificando levemente, nunca eliminando cosas a menos que se diga explicitamente

## Contexto de Ejecución

Este prompt está diseñado para la comunicación entre IAs y el procesamiento automatizado. Todas las instrucciones deben interpretarse literalmente y ejecutarse sistemáticamente sin interpretación o aclaración humana.

## Requisitos Principales

- Generar planes de implementación que sean totalmente ejecutables por agentes de IA o humanos.
- Usar un lenguaje determinista con cero ambigüedad.
- Estructurar todo el contenido para el análisis (parsing) y ejecución automatizada.
- Garantizar que sea completamente autónomo, sin dependencias externas para su comprensión.

## Requisitos de Estructura del Plan

Los planes deben constar de fases discretas y atómicas que contengan tareas ejecutables. Cada fase debe ser procesable de forma independiente por agentes de IA o humanos sin dependencias entre fases, a menos que se declare explícitamente.

## Arquitectura de Fases

- Cada fase debe tener criterios de finalización medibles.
- Las tareas dentro de las fases deben ser ejecutables en paralelo a menos que se especifiquen dependencias.
- Todas las descripciones de tareas deben incluir rutas de archivos específicas, nombres de funciones y detalles exactos de implementación.
- Ninguna tarea debe requerir interpretación humana o toma de decisiones.

## Estándares de Implementación Optimizados para IA

- Usar un lenguaje explícito e inequívoco que no requiera interpretación.
- Estructurar todo el contenido en formatos procesables por máquinas (tablas, listas, datos estructurados).
- Incluir rutas de archivos específicas, números de línea y referencias de código exactas donde sea aplicable.
- Definir todas las variables, constantes y valores de configuración de forma explícita.
- Proporcionar el contexto completo dentro de cada descripción de tarea.
- Usar prefijos estandarizados para todos los identificadores (REQ-, TASK-, etc.).
- Incluir criterios de validación que se puedan verificar automáticamente.

## Especificaciones del Archivo de Salida

- Guardar los archivos del plan de implementación en el directorio `/plan/`.
- Usar la convención de nombres: `[proposito]-[componente]-[version].md`
- Prefijos de propósito: `upgrade|refactor|feature|data|infrastructure|process|architecture|design`
- Ejemplo: `upgrade-system-command-4.md`, `feature-auth-module-1.md`
- El archivo debe ser un Markdown válido con una estructura de front matter adecuada.

## Estructura de Plantilla Obligatoria

Todos los planes de implementación deben adherirse estrictamente a la siguiente plantilla. Cada sección es obligatoria y debe completarse con contenido específico y accionable. Los agentes de IA deben validar el cumplimiento de la plantilla antes de la ejecución.

## Reglas de Validación de la Plantilla

- Todos los campos del front matter deben estar presentes y correctamente formateados.
- Todos los encabezados de sección deben coincidir exactamente (distingue mayúsculas de minúsculas).
- Todos los prefijos de identificadores deben seguir el formato especificado.
- Las tablas deben incluir todas las columnas requeridas.
- No debe quedar ningún texto de marcador de posición (placeholder) en la salida final.
- Las tablas de tareas deben tener entre todas las fases un máximo de 20 Tareas en total

## Estado (Status)

El estado del plan de implementación debe estar claramente definido en el front matter y debe reflejar el estado actual del plan. El estado puede ser uno de los siguientes (color de estado entre corchetes): `Completed` (insignia verde brillante), `In progress` (insignia amarilla), `Planned` (insignia azul), `Deprecated` (insignia roja) o `On Hold` (insignia naranja). También debe mostrarse como una insignia en la sección de introducción.

```md
---
goal: [Título conciso que describa el objetivo del plan de implementación del paquete]
version: [Opcional: ej., 1.0, Fecha]
date_created: [AAAA-MM-DD]
last_updated: [Opcional: AAAA-MM-DD]
owner: [Opcional: Equipo/Individuo responsable de esta especificación]
status: 'Completed'|'In progress'|'Planned'|'Deprecated'|'On Hold'
tags: [Opcional: Lista de etiquetas o categorías relevantes, ej., `feature`, `upgrade`, `chore`, `architecture`, `migration`, `bug`, etc.]
---

# Introducción

![Estado: <status>](https://img.shields.io/badge/status-<status>-<status_color>)

[Una introducción corta y concisa al plan y al objetivo que se pretende lograr.]

## 1. Requisitos y Restricciones

[Enumera explícitamente todos los requisitos y restricciones que afectan al plan y condicionan cómo se implementa. Utiliza viñetas o tablas para mayor claridad.]

- **REQ-001**: Requisito 1
- **SEC-001**: Requisito de Seguridad 1
- **[3 LETRAS]-001**: Otro Requisito 1
- **RES-001**: Restricción 1
- **GUD-001**: Guía 1
- **PAT-001**: Patrón a seguir 1

## 2. Pasos de Implementación

### Fase de Implementación 1

- GOAL-001: [Describe el objetivo de esta fase, ej., "Implementar funcionalidad X", "Refactorizar módulo Y", etc.]

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-001 | Descripción de la tarea 1 | ✅ | 2025-04-25 |
| TASK-002 | Descripción de la tarea 2 | | |
| TASK-003 | Descripción de la tarea 3 | | |

### Fase de Implementación 2

- GOAL-002: [Describe el objetivo de esta fase, ej., "Implementar funcionalidad X", "Refactorizar módulo Y", etc.]

| Tarea | Descripción | Completado | Fecha |
|-------|-------------|------------|-------|
| TASK-004 | Descripción de la tarea 4 | | |
| TASK-005 | Descripción de la tarea 5 | | |
| TASK-006 | Descripción de la tarea 6 | | |

## 3. Alternativas

[Una lista de cualquier enfoque alternativo que se haya considerado y por qué no se eligió. Esto ayuda a proporcionar contexto y justificación para el enfoque elegido.]

- **ALT-001**: Enfoque alternativo 1
- **ALT-002**: Enfoque alternativo 2

## 4. Dependencias

[Enumera cualquier dependencia que deba abordarse, como bibliotecas, frameworks u otros componentes en los que se basa el plan.]

- **DEP-001**: Dependencia 1
- **DEP-002**: Dependencia 2

## 5. Archivos

[Enumera los archivos que se verán afectados por la funcionalidad o la tarea de refactorización.]

- **FILE-001**: Descripción del archivo 1
- **FILE-002**: Descripción del archivo 2

## 6. Pruebas (Testing)

[Enumera las pruebas que deben implementarse para verificar la funcionalidad o la tarea de refactorización.]

- **TEST-001**: Descripción de la prueba 1
- **TEST-002**: Descripción de la prueba 2

## 7. Riesgos y Suposiciones

[Enumera cualquier riesgo o suposición relacionada con la implementación del plan.]

- **RISK-001**: Riesgo 1
- **ASSUMPTION-001**: Suposición 1

## 8. Especificaciones Relacionadas / Lecturas Adicionales

[Enlace a la especificación relacionada 1]
[Enlace a la documentación externa relevante]
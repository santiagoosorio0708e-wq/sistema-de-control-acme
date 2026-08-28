<![CDATA[<div align="center">

# 🏢 SICA — Sistema Integrado de Control de Acceso

### Zona Acme · Complejo Empresarial

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-3.46-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Swing](https://img.shields.io/badge/Java_Swing-GUI-007396?style=for-the-badge&logo=java&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

*Aplicación de escritorio nativa para la gestión integral de acceso al complejo empresarial Zona Acme.*

---

</div>

## 📋 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Características Principales](#-características-principales)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Roles y Permisos (RBAC)](#-roles-y-permisos-rbac)
- [Flujo de Estados de Visitas](#-flujo-de-estados-de-visitas)
- [Patrones de Diseño](#-patrones-de-diseño)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Cuentas de Prueba](#-cuentas-de-prueba)
- [Base de Datos](#-base-de-datos)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Licencia](#-licencia)

---

## 🎯 Descripción General

**SICA** es un sistema de escritorio diseñado para controlar y registrar el acceso de personas (trabajadores y visitantes) al complejo empresarial **Zona Acme**. Gestiona todo el ciclo de vida de una visita: desde el pre-registro por parte del funcionario de la empresa destino, pasando por la verificación en portería por el guarda de seguridad, hasta el registro de salida.

El sistema implementa **Arquitectura Hexagonal (Ports & Adapters)** para mantener la lógica de negocio completamente independiente de la interfaz gráfica y la persistencia de datos, garantizando mantenibilidad y testabilidad.

---

## ✨ Características Principales

| Módulo | Funcionalidad |
|--------|---------------|
| 🔐 **Autenticación** | Login con credenciales (hashing SHA-256), sesión por ThreadLocal |
| 👥 **Gestión de Personas** | CRUD de trabajadores y visitantes del complejo |
| 🏗️ **Gestión de Empresas** | Administración de las empresas del complejo Zona Acme |
| 📋 **Pre-registro de Visitas** | Los funcionarios pueden pre-registrar visitas que quedan aprobadas automáticamente |
| 🚪 **Control de Acceso** | Check-in / Check-out por guardas de seguridad |
| ✅ **Aprobación de Visitas** | Funcionarios aprueban o rechazan visitantes no anunciados |
| ⚠️ **Gestión de Incidentes** | Registro y seguimiento de incidentes de seguridad |
| 📊 **Reportes** | Estadísticas de visitas, incidentes y actividad |
| 📝 **Auditoría Inmutable** | Bitácora de todas las acciones realizadas en el sistema |
| 🔔 **Notificaciones** | Sistema de eventos Observer para alertas en tiempo real |

---

## 🏗️ Arquitectura del Sistema

El proyecto sigue la **Arquitectura Hexagonal (Ports & Adapters)**, donde cada módulo de negocio se organiza en tres capas:

```
┌─────────────────────────────────────────────────────────────────┐
│                    CAPA DE INFRAESTRUCTURA                      │
│  ┌───────────────────┐           ┌───────────────────────────┐  │
│  │   GUI (Swing)     │           │   Persistencia (SQLite)   │  │
│  │  • LoginPanel     │           │   • VisitaRepoImpl        │  │
│  │  • DashboardPanel │           │   • PersonaRepoImpl       │  │
│  │  • AccesosPanel   │           │   • EmpresaRepoImpl       │  │
│  │  • MainFrame      │           │   • UsuarioRepoImpl       │  │
│  └────────┬──────────┘           └──────────┬────────────────┘  │
│           │                                 │                   │
├───────────┼─────────────────────────────────┼───────────────────┤
│           │      CAPA DE APLICACIÓN         │                   │
│           ▼                                 ▼                   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Servicios de Aplicación (Casos de Uso)                  │   │
│  │  • ControlAccesoService   • PersonaService               │   │
│  │  • AuthService            • EmpresaService               │   │
│  │  • IncidenteService       • AuditoriaService             │   │
│  │  • NotificacionService    • ReporteService               │   │
│  └────────────────────────┬─────────────────────────────────┘   │
│                           │                                     │
├───────────────────────────┼─────────────────────────────────────┤
│                           ▼                                     │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │              CAPA DE DOMINIO (Núcleo)                     │   │
│  │  • Modelos: Visita, Persona, Empresa, Usuario, Incidente │   │
│  │  • Puertos: VisitaRepository, PersonaRepository, etc.    │   │
│  │  • Eventos: AccesoEvent, AccesoEventListener             │   │
│  │  • Excepciones: AccesoDenegado, EntidadNoEncontrada      │   │
│  └──────────────────────────────────────────────────────────┘   │
│                    CAPA DE DOMINIO                               │
└─────────────────────────────────────────────────────────────────┘
```

**Principio clave:** Las dependencias siempre apuntan hacia adentro. El dominio no conoce la infraestructura.

---

## 📁 Estructura del Proyecto

```
src/main/java/com/acme/sica/
│
├── SicaApplication.java              # Punto de entrada principal
│
├── acceso/                            # Módulo de Control de Acceso
│   ├── application/
│   │   ├── ControlAccesoService.java  # Casos de uso: registro, check-in/out, aprobación
│   │   ├── NotificacionService.java   # Observer: gestión de eventos
│   │   └── strategy/                  # Estrategias de acceso (Strategy Pattern)
│   │       ├── EstrategiaAcceso.java          # Interfaz
│   │       ├── AccesoPreRegistradoStrategy.java
│   │       ├── AccesoNoAnunciadoStrategy.java
│   │       ├── AccesoPaseTemporalStrategy.java
│   │       └── AccesoSalidaOlvidadaStrategy.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Visita.java            # Entidad principal de acceso
│   │   │   └── enums/EstadoVisita.java
│   │   ├── port/
│   │   │   └── VisitaRepository.java  # Puerto (interfaz) del repositorio
│   │   └── event/
│   │       ├── AccesoEvent.java       # Evento de dominio
│   │       └── AccesoEventListener.java
│   └── infrastructure/
│       ├── gui/
│       │   ├── DashboardPanel.java    # Panel principal con estadísticas
│       │   └── AccesosPanel.java      # Panel de gestión de accesos
│       └── persistence/
│           └── VisitaRepositoryImpl.java  # Adaptador SQLite
│
├── usuario/                           # Módulo de Usuarios y Autenticación
│   ├── application/
│   │   ├── AuthService.java           # Login y autenticación
│   │   └── UsuarioService.java        # CRUD de usuarios
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Usuario.java
│   │   │   ├── Rol.java
│   │   │   └── Permiso.java
│   │   └── port/
│   │       ├── UsuarioRepository.java
│   │       ├── RolRepository.java
│   │       └── PermisoRepository.java
│   └── infrastructure/
│       ├── gui/LoginPanel.java
│       └── persistence/
│           ├── UsuarioRepositoryImpl.java
│           ├── RolRepositoryImpl.java
│           └── PermisoRepositoryImpl.java
│
├── persona/                           # Módulo de Personas
│   ├── application/PersonaService.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Persona.java
│   │   │   └── enums/ (TipoDocumento, TipoPersona, EstadoAcceso)
│   │   └── port/PersonaRepository.java
│   └── infrastructure/persistence/PersonaRepositoryImpl.java
│
├── empresa/                           # Módulo de Empresas
│   ├── application/EmpresaService.java
│   ├── domain/
│   │   ├── model/Empresa.java
│   │   └── port/EmpresaRepository.java
│   └── infrastructure/persistence/EmpresaRepositoryImpl.java
│
├── incidente/                         # Módulo de Incidentes
│   ├── application/IncidenteService.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Incidente.java
│   │   │   └── enums/Severidad.java
│   │   └── port/IncidenteRepository.java
│   └── infrastructure/persistence/IncidenteRepositoryImpl.java
│
├── auditoria/                         # Módulo de Auditoría
│   ├── application/AuditoriaService.java
│   ├── domain/
│   │   ├── model/BitacoraAuditoria.java
│   │   └── port/BitacoraRepository.java
│   └── infrastructure/persistence/BitacoraRepositoryImpl.java
│
├── reporte/                           # Módulo de Reportes
│   └── application/ReporteService.java
│
└── shared/                            # Componentes Compartidos
    ├── domain/exception/
    │   ├── AccesoDenegadoException.java
    │   └── EntidadNoEncontradaException.java
    ├── infrastructure/
    │   ├── DatabaseConnection.java     # Singleton de conexión SQLite
    │   ├── DatabaseInitializer.java    # Carga schema.sql y data.sql
    │   └── gui/MainFrame.java         # Ventana principal con CardLayout
    └── security/
        ├── AutorizacionService.java    # Verificación de permisos RBAC
        └── SesionActual.java           # ThreadLocal del usuario autenticado

src/main/resources/
├── schema.sql                         # DDL: creación de tablas e índices
└── data.sql                           # Datos de prueba (seed data)
```

---

## 🔑 Roles y Permisos (RBAC)

El sistema implementa **Control de Acceso Basado en Roles** con permisos granulares por módulo:

### Roles del Sistema

| Rol | Descripción | Alcance |
|-----|-------------|---------|
| **ADMINISTRADOR** | Acceso total al sistema | Todos los módulos y permisos |
| **GUARDA_SEGURIDAD** | Control de acceso físico | Check-in/out, registro de incidentes |
| **FUNCIONARIO_EMPRESA** | Gestión de visitas a su empresa | Pre-registro, aprobación/rechazo |

### Matriz de Permisos

| Permiso | Admin | Guarda | Funcionario |
|---------|:-----:|:------:|:-----------:|
| Crear/Editar Usuarios | ✅ | ❌ | ❌ |
| Crear Persona | ✅ | ✅ | ✅ |
| Registrar Visita (No Anunciada) | ✅ | ✅ | ❌ |
| Pre-registrar Visita | ✅ | ❌ | ✅ |
| Aprobar/Rechazar Visita | ✅ | ❌ | ✅ |
| Check-In / Check-Out | ✅ | ✅ | ❌ |
| Ver Todas las Visitas | ✅ | ✅ | ❌ |
| Ver Mis Visitas | ✅ | ❌ | ✅ |
| Registrar Incidente | ✅ | ✅ | ❌ |
| Ver Reportes Completos | ✅ | ❌ | ❌ |
| Ver Reportes Básicos | ✅ | ✅ | ✅ |
| Ver Auditoría | ✅ | ❌ | ❌ |
| Gestionar Empresas | ✅ | ❌ | ❌ |

---

## 🔄 Flujo de Estados de Visitas

```
                    ┌─────────────┐
                    │  PENDIENTE  │◄──── Visitante No Anunciado
                    └──────┬──────┘      (registrado por Guarda)
                           │
              ┌────────────┼────────────┐
              ▼                         ▼
     ┌────────────────┐       ┌─────────────────┐
     │    APROBADO     │       │    RECHAZADO     │
     └────────┬───────┘       └─────────────────┘
              │                (Estado final)
              │ ◄──── Pre-registro por Funcionario
              │        (entra directo como APROBADO)
              │
              ▼
     ┌────────────────┐
     │     DENTRO      │◄──── Check-In por Guarda
     └────────┬───────┘
              │
              ▼
     ┌────────────────┐
     │    CERRADA      │◄──── Check-Out por Guarda
     └────────────────┘
              
     ┌──────────────────────┐
     │  CERRADA_POR_SISTEMA │◄──── Cierre automático
     └──────────────────────┘      (salida olvidada)
```

---

## 🧩 Patrones de Diseño

| Patrón | Implementación | Propósito |
|--------|----------------|-----------|
| **Hexagonal (Ports & Adapters)** | Paquetes `domain/port` + `infrastructure/persistence` | Desacoplar dominio de infraestructura |
| **Strategy** | `EstrategiaAcceso` + implementaciones concretas | Diferentes flujos de acceso según tipo |
| **Observer** | `NotificacionService` + `AccesoEventListener` | Notificaciones de eventos de acceso |
| **Repository** | Interfaces en `domain/port` + impls en `infrastructure` | Abstracción de persistencia de datos |
| **Singleton (Thread-Safe)** | `SesionActual` con `ThreadLocal` | Sesión del usuario autenticado |
| **RBAC** | `AutorizacionService` + tablas `roles`/`permisos` | Control de acceso basado en roles |

---

## 📌 Requisitos Previos

| Requisito | Versión Mínima | Notas |
|-----------|:--------------:|-------|
| **Java (JDK)** | 21 | OpenJDK o cualquier distribución compatible |
| **Maven** | 3.9+ | Para gestión de dependencias y compilación |
| **IDE** *(Opcional)* | — | IntelliJ IDEA, Eclipse, VS Code o cualquier IDE con soporte Java |

> **Nota:** SQLite no requiere instalación separada. El driver JDBC se descarga automáticamente como dependencia Maven.

---

## 🚀 Instalación y Ejecución

### Opción 1: Desde un IDE (Recomendado)

1. **Clona el repositorio:**
   ```bash
   git clone https://github.com/santiagoosorio0708e-wq/sistema-de-control-acme.git
   cd sistema-de-control-acme
   ```

2. **Abre el proyecto** en tu IDE favorito (IntelliJ IDEA, Eclipse, VS Code).

3. **Espera** a que el IDE detecte el `pom.xml` e instale las dependencias Maven automáticamente.

4. **Ejecuta** la clase principal:
   ```
   src/main/java/com/acme/sica/SicaApplication.java
   ```

5. **¡Listo!** Se abrirá la ventana de login en tu pantalla.

### Opción 2: Desde la Terminal

```bash
# Clonar el repositorio
git clone https://github.com/santiagoosorio0708e-wq/sistema-de-control-acme.git
cd sistema-de-control-acme

# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn exec:java
```

### Opción 3: Generar JAR Ejecutable

```bash
# Compilar y empaquetar
mvn clean package

# Ejecutar el JAR
java -jar target/sica-1.0.0.jar
```

---

## 🔑 Cuentas de Prueba

La base de datos se inicializa automáticamente con datos de prueba la primera vez que se ejecuta. En la pantalla de login hay botones de acceso rápido para las siguientes cuentas:

| Rol | Usuario | Contraseña | Nombre |
|-----|---------|------------|--------|
| 🔴 **Administrador** | `admin` | `admin123` | Carlos Administrador López |
| 🟡 **Guarda de Seguridad** | `guarda1` | `guarda123` | Pedro Martínez Guarda |
| 🟡 **Guarda de Seguridad** | `guarda2` | `guarda456` | Ana Gómez Seguridad |
| 🟢 **Funcionario de Empresa** | `funcionario1` | `func123` | María Rodríguez Funcionaria |
| 🟢 **Funcionario de Empresa** | `funcionario2` | `func456` | José García Funcionario |

---

## 🗃️ Base de Datos

### Motor
El sistema utiliza **SQLite** como base de datos embebida. No requiere servidor ni instalación adicional.

### Esquema

| Tabla | Descripción | Registros de prueba |
|-------|-------------|:-------------------:|
| `roles` | Roles del sistema (RBAC) | 3 |
| `permisos` | Permisos granulares por módulo | 33 |
| `rol_permiso` | Asignación de permisos a roles | ~50 |
| `usuarios` | Usuarios con credenciales y rol | 5 |
| `empresas` | Empresas del complejo Zona Acme | 5 |
| `personas` | Trabajadores y visitantes | 18 |
| `visitas` | Registros de acceso con estados | 13 |
| `incidentes` | Incidentes de seguridad | 3 |
| `bitacora_auditoria` | Registro inmutable de acciones | ~12 |

### Archivos SQL
- **[`schema.sql`](src/main/resources/schema.sql)** — DDL: creación de tablas, constraints e índices.
- **[`data.sql`](src/main/resources/data.sql)** — Datos de prueba para todos los módulos.

> La base de datos se crea automáticamente en el directorio raíz del proyecto como `sica.db`.

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Uso |
|------------|-----|
| **Java 21** | Lenguaje principal, records, pattern matching |
| **Java Swing** | Interfaz gráfica de escritorio nativa |
| **SQLite 3.46** | Base de datos embebida, sin servidor |
| **Maven 3.9+** | Gestión de dependencias y build |
| **JDBC** | Acceso a base de datos |
| **SHA-256** | Hashing de contraseñas |

---

## 📄 Licencia

Este proyecto es de uso académico. Desarrollado como parte del sistema de control de acceso para el complejo empresarial Zona Acme.

---

<div align="center">

**Desarrollado con ☕ Java · Arquitectura Hexagonal · SOLID Principles**

</div>
]]>

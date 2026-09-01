<div align="center">

# 🏢 SICA — Sistema Integrado de Control de Acceso
### Zona Acme · Complejo Empresarial

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-flat-square&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-flat-square&logo=apachemaven&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-3.46-003B57?style=for-flat-square&logo=sqlite&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java_Swing-007396?style=for-flat-square&logo=java&logoColor=white)

*Aplicación de escritorio nativa para gestionar y auditar de forma segura el acceso de trabajadores y visitantes al complejo.*

</div>

---

## 🎯 ¿Qué es SICA?

**SICA** permite gestionar de manera integral la seguridad y los ingresos a **Zona Acme**. 
El sistema conecta a los **Funcionarios** (quienes pre-registran visitantes), los **Guardas de Seguridad** (quienes validan los ingresos y salidas) y los **Administradores** mediante un control de acceso centralizado, roles estrictos y auditoría inmutable. Todo esto construido bajo **Arquitectura Hexagonal**.

## ✨ Funciones Clave

- 👥 **Gestión Completa:** Crea y administra visitantes, trabajadores y empresas.
- 🚪 **Check-In / Check-Out:** Control rápido en portería por parte de los guardas.
- 📋 **Pre-registro de Visitas:** Funcionarios autorizan ingresos anticipadamente.
- 🛡️ **Seguridad (RBAC):** Permisos específicos dependiendo de tu rol.
- ⚠️ **Incidentes:** Registro de novedades y reportes centralizados.

---

## 🚀 Cómo Ejecutar el Proyecto

No necesitas instalar bases de datos externas; el proyecto usa SQLite integrado. 

### Opción 1: Desde tu IDE (Recomendado)
1. Abre el proyecto en IntelliJ IDEA, VS Code o Eclipse.
2. Espera a que Maven descargue las dependencias.
3. Ejecuta la clase principal: `src/main/java/com/acme/sica/SicaApplication.java`

### Opción 2: Desde la Terminal
```bash
# Compilar el código
mvn clean compile

# Iniciar la aplicación
mvn exec:java
```

---

## 🔑 Cuentas de Prueba

La base de datos se inicializa automáticamente con información de prueba en el primer arranque. 

Usa estas credenciales para probar los distintos flujos:

| Rol | Usuario | Contraseña | ¿Qué puede hacer? |
|-----|---------|------------|-------------------|
| 🔴 **Admin** | `admin` | `admin123` | Acceso total al sistema y configuraciones. |
| 🟡 **Guarda** | `guarda1` | `guarda123` | Registra entradas (Check-In) y salidas (Check-Out). |
| 🟢 **Funcionario** | `funcionario1` | `func123` | Pre-registra y aprueba visitas para su empresa. |

---

## Modelo de la Base de Datos

El sistema implementa una base de datos relacional (SQLite) normalizada. Las tablas principales incluyen:
- **usuarios:** Almacena credenciales y referencias al rol.
- **roles / permisos:** Conforman el control de acceso basado en roles (RBAC). La tabla intermedia `rol_permisos` vincula ambos.
- **personas / empresas:** Representan entidades del mundo real interactuando en el complejo.
- **visitas:** Entidad central que registra los eventos de acceso, estado, hora de check-in y check-out, y referencias al anfitrión (funcionario).
- **incidentes:** Registro de problemas o reportes asociados a personas.
- **bitacora_auditoria:** Tabla inmutable donde se almacena todo el historial de cambios, logins y operaciones críticas del sistema.

### Diagrama Entidad-Relación (Conceptual)
```
[ROL] 1--* [USUARIO] 1--1 [PERSONA]
[ROL] 1--* [ROL_PERMISOS] *--1 [PERMISO]
[EMPRESA] 1--* [PERSONA]
[PERSONA (Visitante)] 1--* [VISITA] *--1 [PERSONA (Funcionario/Anfitrión)]
[VISITA] *--1 [EMPRESA]
```

## Decisiones de Diseño y Arquitectura

El requerimiento original planteaba el uso de una arquitectura MVC (Model-View-Controller). Sin embargo, con el objetivo de presentar una solución de grado profesional, el SICA fue desarrollado siguiendo **Arquitectura Hexagonal (Ports and Adapters)**. Esta decisión arquitectónica es una evolución moderna del MVC tradicional que aísla completamente la lógica de negocio (Dominio) de las interfaces de usuario (Swing) y de las bases de datos (SQLite), lo cual garantiza un código mucho más robusto, testeable y alineado a los principios SOLID:

- **Single Responsibility Principle (SRP):** Cada clase tiene una responsabilidad única. Por ejemplo, `AutorizacionService` maneja solo validación de roles, mientras `ControlAccesoService` orquesta la lógica de negocio.
- **Dependency Inversion Principle (DIP):** Los servicios de aplicación dependen de abstracciones (interfaces como `VisitaRepository`) y no de detalles de implementación de base de datos.

### 5 Patrones de Diseño Implementados

Para resolver los distintos retos técnicos del proyecto, se aplicaron rigurosamente los siguientes 5 patrones de diseño:

1. **Patrón Strategy:** Se utilizó la interfaz `EstrategiaAcceso` para desacoplar las complejas reglas de negocio de los distintos flujos de ingreso (Invitado No Anunciado, Pase Temporal, Salida Olvidada). Esto permite extender las reglas en el futuro sin modificar el `ControlAccesoService` (cumpliendo el Open/Closed Principle).
2. **Patrón Singleton:** Implementado en `DatabaseConnection.getInstance()` para garantizar una única instancia global de la conexión a la base de datos (SQLite) durante todo el ciclo de vida de la aplicación de escritorio.
3. **Patrón Repository (DAO):** Aisla la capa de dominio de la persistencia de datos. Interfaces como `VisitaRepository` dictan el contrato, mientras que `VisitaRepositoryImpl` se encarga de la lógica SQL específica, permitiendo un fácil intercambio de bases de datos.
4. **Dependency Injection (Inyección de Dependencias):** Se evitó instanciar dependencias fuertemente acopladas dentro de los servicios. En su lugar, todos los repositorios y servicios transversales (como `AuditoriaService`) se inyectan a través de los constructores (ej. en `MainFrame.java`).
5. **Patrón Observer (Event-Driven):** La aplicación implementa un sistema asíncrono de eventos a través de `NotificacionService` (Publisher) y `AccesoEvent`. Esto permite que la interfaz gráfica y otros módulos reaccionen a los cambios de estado (como un check-in) en tiempo real, sin estar fuertemente acoplados.

---

<div align="center">
Desarrollado con ☕ Java · Arquitectura Hexagonal · SOLID Principles
</div>

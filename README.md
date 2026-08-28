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

<div align="center">
Desarrollado con ☕ Java · Arquitectura Hexagonal · SOLID Principles
</div>

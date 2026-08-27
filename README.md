# SICA - Sistema Integrado de Control de Acceso (Zona Acme)

SICA es una aplicación de escritorio nativa diseñada para gestionar el acceso al complejo empresarial Zona Acme. 
Utiliza **Arquitectura Hexagonal**, asegurando que la lógica de negocio se mantenga independiente de la interfaz visual. Está construido con Java 21, Java Swing (para la interfaz de escritorio) y SQLite (para la persistencia de datos).

## Características Principales

*   **Interfaz Gráfica Nativa (Java Swing):** Ventanas y paneles nativos de escritorio, sin dependencias de navegadores web.
*   **Modularidad Hexagonal:** Los controladores de interfaz gráfica interactúan limpiamente con los Servicios de Aplicación sin mezclar responsabilidades.
*   **Roles y Permisos (RBAC):** Flujos y vistas adaptables dinámicamente si eres Administrador, Guarda o Funcionario.
*   **Gestión de Visitas:** Pre-registro, solicitudes no anunciadas, y pase temporal.
*   **Auditoría Inmutable:** Registro de todas las acciones en la base de datos de manera inmodificable.

## Requisitos

*   Java 21 (OpenJDK o similar)
*   Un IDE como IntelliJ IDEA, Eclipse o VS Code.

## Instalación y Ejecución

La forma más rápida y recomendada de ejecutar este proyecto es a través de un **Entorno de Desarrollo Integrado (IDE)**.

1. Abre tu IDE favorito (IntelliJ IDEA, Eclipse, etc.).
2. Abre la carpeta de este proyecto (`proyecto-SICA`).
3. El IDE detectará que es un proyecto Maven e instalará las dependencias necesarias automáticamente (SQLite).
4. Busca el archivo principal: `src/main/java/com/acme/sica/SicaApplication.java`.
5. Ejecuta (Run) la clase `SicaApplication`.
6. ¡Se abrirá la ventana principal de la aplicación en tu pantalla!

## Cuentas de Prueba

La base de datos se inicializa automáticamente con datos de prueba la primera vez que se ejecuta. En la pantalla de login, encontrarás atajos para usar estas cuentas, o puedes digitarlas manualmente:

*   **Administrador:** `admin` / `admin123`
*   **Guarda:** `guarda1` / `guarda123`
*   **Funcionario:** `funcionario1` / `func123`

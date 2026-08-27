git init
git remote add origin https://github.com/santiagoosorio0708e-wq/sistema-de-control-acme.git
git add pom.xml README.md .gitignore
git commit -m "chore: setup inicial con dependencias y config básica"
git add src/main/java/com/acme/sica/shared/infrastructure/DatabaseConnection.java src/main/java/com/acme/sica/shared/infrastructure/DatabaseInitializer.java
git commit -m "feat(shared): conectamos la app a la base de datos sqlite y agregamos script init"
git add src/main/java/com/acme/sica/shared/domain/exception/
git commit -m "feat(shared): manejo de excepciones estándar (AccesoDenegado, NoEncontrada)"
git add src/main/java/com/acme/sica/shared/security/
git commit -m "feat(security): control de sesión actual y servicio de autorización de usuarios"
git add src/main/java/com/acme/sica/persona/domain/model/enums/
git commit -m "feat(persona): catálogos y enums base para tipos y estados de persona"
git add src/main/java/com/acme/sica/persona/domain/model/Persona.java
git commit -m "feat(persona): modelo principal de la entidad persona"
git add src/main/java/com/acme/sica/persona/domain/port/ src/main/java/com/acme/sica/persona/infrastructure/
git commit -m "feat(persona): implementación del repositorio de personas"
git add src/main/java/com/acme/sica/persona/application/
git commit -m "feat(persona): reglas de negocio para registrar y validar personas"
git add src/main/java/com/acme/sica/empresa/
git commit -m "feat(empresa): módulo de gestión de empresas con sus persistencias y lógicas"
git add src/main/java/com/acme/sica/usuario/domain/
git commit -m "feat(usuario): estructura de dominio para roles, permisos y usuarios"
git add src/main/java/com/acme/sica/usuario/infrastructure/persistence/ src/main/java/com/acme/sica/usuario/application/
git commit -m "feat(usuario): lógica de autenticación y acceso a base de datos de usuarios"
git add src/main/java/com/acme/sica/acceso/domain/model/
git commit -m "feat(acceso): modelo core para registro de visitas y sus estados"
git add src/main/java/com/acme/sica/acceso/domain/event/ src/main/java/com/acme/sica/acceso/domain/port/
git commit -m "feat(acceso): puertos de datos y eventos en tiempo real de acceso"
git add src/main/java/com/acme/sica/acceso/application/strategy/
git commit -m "feat(acceso): implementamos strategies para tipos de acceso (PreRegistrado, Temporal, etc)"
git add src/main/java/com/acme/sica/acceso/application/ControlAccesoService.java src/main/java/com/acme/sica/acceso/application/NotificacionService.java src/main/java/com/acme/sica/acceso/infrastructure/persistence/
git commit -m "feat(acceso): validación final y notificación de accesos en el sistema"
git add src/main/java/com/acme/sica/incidente/
git commit -m "feat(incidente): módulo para registrar problemas e incidentes de seguridad"
git add src/main/java/com/acme/sica/auditoria/
git commit -m "feat(auditoria): todo el logging y bitácora de operaciones del sistema"
git add src/main/java/com/acme/sica/reporte/
git commit -m "feat(reporte): exportación y servicios para reportes de actividad"
git add src/main/java/com/acme/sica/shared/infrastructure/gui/ src/main/java/com/acme/sica/usuario/infrastructure/gui/ src/main/java/com/acme/sica/acceso/infrastructure/gui/
git commit -m "feat(ui): pantallas principales, login y dashboard con swing"
git add src/main/java/com/acme/sica/SicaApplication.java src/main/resources/
git commit -m "feat(core): clase principal y schemas sql para levantar la base de datos"
git add .
git commit -m "chore: archivos misceláneos finales y limpieza"
git branch -M main
git push -u origin main

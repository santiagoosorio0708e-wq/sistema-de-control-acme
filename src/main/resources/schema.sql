-- ============================================================
-- SICA - Sistema Integrado de Control de Acceso
-- Schema de Base de Datos (SQLite)
-- ============================================================

-- Habilitar claves foráneas en SQLite
PRAGMA foreign_keys = ON;

-- ============================================================
-- TABLA: roles
-- Almacena los roles del sistema (RBAC)
-- ============================================================
CREATE TABLE IF NOT EXISTS roles (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre      TEXT    NOT NULL UNIQUE,
    descripcion TEXT
);

-- ============================================================
-- TABLA: permisos
-- Permisos granulares del sistema
-- ============================================================
CREATE TABLE IF NOT EXISTS permisos (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    clave       TEXT    NOT NULL UNIQUE,
    descripcion TEXT,
    modulo      TEXT    NOT NULL
);

-- ============================================================
-- TABLA: rol_permiso
-- Relación muchos-a-muchos entre roles y permisos
-- ============================================================
CREATE TABLE IF NOT EXISTS rol_permiso (
    rol_id     INTEGER NOT NULL,
    permiso_id INTEGER NOT NULL,
    PRIMARY KEY (rol_id, permiso_id),
    FOREIGN KEY (rol_id)     REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permiso_id) REFERENCES permisos(id) ON DELETE CASCADE
);

-- ============================================================
-- TABLA: usuarios
-- Usuarios del sistema con credenciales y rol asignado
-- ============================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    username        TEXT    NOT NULL UNIQUE,
    password_hash   TEXT    NOT NULL,
    nombre_completo TEXT    NOT NULL,
    rol_id          INTEGER NOT NULL,
    activo          INTEGER NOT NULL DEFAULT 1,
    fecha_creacion  TEXT    NOT NULL DEFAULT (datetime('now', 'localtime')),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- ============================================================
-- TABLA: empresas
-- Empresas del complejo Zona Acme
-- ============================================================
CREATE TABLE IF NOT EXISTS empresas (
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre            TEXT    NOT NULL,
    nit               TEXT    NOT NULL UNIQUE,
    sector            TEXT,
    telefono_contacto TEXT,
    email_contacto    TEXT,
    activa            INTEGER NOT NULL DEFAULT 1
);

-- ============================================================
-- TABLA: personas
-- Trabajadores y visitantes del complejo
-- ============================================================
CREATE TABLE IF NOT EXISTS personas (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    documento       TEXT    NOT NULL UNIQUE,
    tipo_documento  TEXT    NOT NULL CHECK(tipo_documento IN ('CC','TI','CE','PASAPORTE')),
    nombre          TEXT    NOT NULL,
    apellido        TEXT    NOT NULL,
    tipo_persona    TEXT    NOT NULL CHECK(tipo_persona IN ('TRABAJADOR','VISITANTE')),
    empresa_id      INTEGER,
    foto_url        TEXT,
    estado_acceso   TEXT    NOT NULL DEFAULT 'ACTIVO' CHECK(estado_acceso IN ('ACTIVO','BLOQUEADO','RESTRINGIDO')),
    telefono        TEXT,
    email           TEXT,
    FOREIGN KEY (empresa_id) REFERENCES empresas(id)
);

-- ============================================================
-- TABLA: visitas
-- Registros de entrada/salida al complejo
-- ============================================================
CREATE TABLE IF NOT EXISTS visitas (
    id                      INTEGER PRIMARY KEY AUTOINCREMENT,
    persona_id              INTEGER NOT NULL,
    empresa_destino_id      INTEGER,
    funcionario_autoriza_id INTEGER,
    motivo                  TEXT,
    fecha_hora_entrada      TEXT,
    fecha_hora_salida       TEXT,
    estado                  TEXT    NOT NULL DEFAULT 'PENDIENTE'
        CHECK(estado IN ('APROBADO','PENDIENTE','RECHAZADO','DENTRO','CERRADA','CERRADA_POR_SISTEMA')),
    observaciones           TEXT,
    fecha_creacion          TEXT    NOT NULL DEFAULT (datetime('now', 'localtime')),
    FOREIGN KEY (persona_id)              REFERENCES personas(id),
    FOREIGN KEY (empresa_destino_id)      REFERENCES empresas(id),
    FOREIGN KEY (funcionario_autoriza_id) REFERENCES usuarios(id)
);

-- ============================================================
-- TABLA: incidentes
-- Registro de incidentes de seguridad
-- ============================================================
CREATE TABLE IF NOT EXISTS incidentes (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    persona_id      INTEGER,
    visita_id       INTEGER,
    tipo            TEXT    NOT NULL,
    descripcion     TEXT    NOT NULL,
    severidad       TEXT    NOT NULL DEFAULT 'BAJA' CHECK(severidad IN ('BAJA','MEDIA','ALTA','CRITICA')),
    reportado_por_id INTEGER NOT NULL,
    fecha_hora      TEXT    NOT NULL DEFAULT (datetime('now', 'localtime')),
    estado          TEXT    NOT NULL DEFAULT 'ABIERTO' CHECK(estado IN ('ABIERTO','EN_INVESTIGACION','CERRADO')),
    FOREIGN KEY (persona_id)      REFERENCES personas(id),
    FOREIGN KEY (visita_id)       REFERENCES visitas(id),
    FOREIGN KEY (reportado_por_id) REFERENCES usuarios(id)
);

-- ============================================================
-- TABLA: bitacora_auditoria
-- Registro inmutable de todas las acciones del sistema
-- ============================================================
CREATE TABLE IF NOT EXISTS bitacora_auditoria (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER,
    accion     TEXT NOT NULL,
    entidad    TEXT,
    entidad_id INTEGER,
    detalle    TEXT,
    fecha_hora TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- ============================================================
-- ÍNDICES para optimizar consultas frecuentes
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_personas_documento ON personas(documento);
CREATE INDEX IF NOT EXISTS idx_visitas_persona ON visitas(persona_id);
CREATE INDEX IF NOT EXISTS idx_visitas_estado ON visitas(estado);
CREATE INDEX IF NOT EXISTS idx_visitas_fecha ON visitas(fecha_creacion);
CREATE INDEX IF NOT EXISTS idx_bitacora_fecha ON bitacora_auditoria(fecha_hora);
CREATE INDEX IF NOT EXISTS idx_bitacora_usuario ON bitacora_auditoria(usuario_id);
CREATE INDEX IF NOT EXISTS idx_incidentes_persona ON incidentes(persona_id);
CREATE INDEX IF NOT EXISTS idx_usuarios_username ON usuarios(username);

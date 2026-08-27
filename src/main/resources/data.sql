-- ============================================================
-- SICA - Sistema Integrado de Control de Acceso
-- Datos de Prueba (Seed Data)
-- ============================================================

PRAGMA foreign_keys = ON;

-- ============================================================
-- ROLES
-- ============================================================
INSERT INTO roles (id, nombre, descripcion) VALUES
(1, 'ADMINISTRADOR',       'Acceso total al sistema. Gestiona usuarios, configuración y todos los módulos.'),
(2, 'GUARDA_SEGURIDAD',    'Controla el acceso físico. Realiza check-in/check-out y registra incidentes.'),
(3, 'FUNCIONARIO_EMPRESA', 'Funcionario de empresa. Pre-registra y aprueba/rechaza visitas.');

-- ============================================================
-- PERMISOS (Granulares por módulo)
-- ============================================================

-- Módulo USUARIO
INSERT INTO permisos (id, clave, descripcion, modulo) VALUES
(1,  'crear_usuario',         'Crear nuevos usuarios en el sistema',           'USUARIO'),
(2,  'editar_usuario',        'Modificar datos de usuarios existentes',        'USUARIO'),
(3,  'eliminar_usuario',      'Desactivar usuarios del sistema',               'USUARIO'),
(4,  'ver_usuarios',          'Consultar lista de usuarios',                   'USUARIO'),
(5,  'cambiar_rol',           'Cambiar el rol asignado a un usuario',          'USUARIO');

-- Módulo PERSONA
INSERT INTO permisos (id, clave, descripcion, modulo) VALUES
(6,  'crear_persona',         'Registrar nuevas personas',                     'PERSONA'),
(7,  'editar_persona',        'Modificar datos de personas',                   'PERSONA'),
(8,  'eliminar_persona',      'Eliminar personas del sistema',                 'PERSONA'),
(9,  'ver_personas',          'Consultar lista de personas',                   'PERSONA'),
(10, 'buscar_persona',        'Buscar personas por documento',                 'PERSONA'),
(11, 'cambiar_estado_acceso', 'Cambiar estado de acceso de una persona',       'PERSONA'),
(12, 'bloquear_persona',      'Bloquear el acceso de una persona al complejo', 'PERSONA');

-- Módulo ACCESO
INSERT INTO permisos (id, clave, descripcion, modulo) VALUES
(13, 'registrar_visita',      'Registrar una nueva visita en portería',        'ACCESO'),
(14, 'pre_registrar_visita',  'Pre-registrar una visita futura',               'ACCESO'),
(15, 'aprobar_visita',        'Aprobar una visita pendiente',                  'ACCESO'),
(16, 'rechazar_visita',       'Rechazar una visita pendiente',                 'ACCESO'),
(17, 'check_in',              'Registrar entrada de una persona',              'ACCESO'),
(18, 'check_out',             'Registrar salida de una persona',               'ACCESO'),
(19, 'ver_visitas',           'Ver todas las visitas del sistema',             'ACCESO'),
(20, 'ver_mis_visitas',       'Ver visitas propias o de la empresa',           'ACCESO');

-- Módulo EMPRESA
INSERT INTO permisos (id, clave, descripcion, modulo) VALUES
(21, 'crear_empresa',         'Registrar nuevas empresas',                     'EMPRESA'),
(22, 'editar_empresa',        'Modificar datos de empresas',                   'EMPRESA'),
(23, 'eliminar_empresa',      'Desactivar empresas',                           'EMPRESA'),
(24, 'ver_empresas',          'Consultar lista de empresas',                   'EMPRESA');

-- Módulo INCIDENTE
INSERT INTO permisos (id, clave, descripcion, modulo) VALUES
(25, 'registrar_incidente',   'Registrar un nuevo incidente de seguridad',     'INCIDENTE'),
(26, 'editar_incidente',      'Modificar datos de un incidente',               'INCIDENTE'),
(27, 'ver_incidentes',        'Consultar incidentes registrados',              'INCIDENTE'),
(28, 'cerrar_incidente',      'Cerrar un incidente resuelto',                  'INCIDENTE');

-- Módulo REPORTE
INSERT INTO permisos (id, clave, descripcion, modulo) VALUES
(29, 'ver_reportes',          'Acceder a todos los reportes del sistema',      'REPORTE'),
(30, 'ver_reportes_basicos',  'Acceder a reportes básicos',                    'REPORTE'),
(31, 'generar_reporte_completo', 'Generar reportes detallados y exportar',     'REPORTE');

-- Módulo AUDITORIA
INSERT INTO permisos (id, clave, descripcion, modulo) VALUES
(32, 'ver_auditoria',         'Consultar la bitácora de auditoría completa',   'AUDITORIA'),
(33, 'ver_bitacora',          'Ver registros de bitácora',                     'AUDITORIA');

-- ============================================================
-- ASIGNACIÓN ROL-PERMISO
-- ============================================================

-- ADMINISTRADOR: Todos los permisos (1 al 33)
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),
(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),
(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),
(1,31),(1,32),(1,33);

-- GUARDA_SEGURIDAD: Permisos operativos de acceso e incidentes
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES
(2,6),   -- crear_persona
(2,9),   -- ver_personas
(2,10),  -- buscar_persona
(2,13),  -- registrar_visita
(2,17),  -- check_in
(2,18),  -- check_out
(2,19),  -- ver_visitas
(2,25),  -- registrar_incidente
(2,27),  -- ver_incidentes
(2,30);  -- ver_reportes_basicos

-- FUNCIONARIO_EMPRESA: Permisos de gestión de visitas
INSERT INTO rol_permiso (rol_id, permiso_id) VALUES
(3,6),   -- crear_persona
(3,9),   -- ver_personas
(3,10),  -- buscar_persona
(3,14),  -- pre_registrar_visita
(3,15),  -- aprobar_visita
(3,16),  -- rechazar_visita
(3,20),  -- ver_mis_visitas
(3,24),  -- ver_empresas
(3,30);  -- ver_reportes_basicos

-- ============================================================
-- USUARIOS
-- Contraseñas hasheadas con SHA-256
-- ============================================================
INSERT INTO usuarios (id, username, password_hash, nombre_completo, rol_id, activo) VALUES
(1, 'admin',        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Carlos Administrador López',  1, 1),
(2, 'guarda1',      '13791440eec56d20730b2c42e51e36f51d793d2f0e81483bbf6130f316661510', 'Pedro Martínez Guarda',       2, 1),
(3, 'guarda2',      'd0e930c4160461c2f7b16c8b840751af2ab103150a183653fa7afa428c017c86', 'Ana Gómez Seguridad',         2, 1),
(4, 'funcionario1', '98bedb5e9f0218ff6dd0e95e914fd25f21f3fa8ef599b7a09a900e580f464e7e', 'María Rodríguez Funcionaria', 3, 1),
(5, 'funcionario2', 'da010d6e080040be192858900a703200c3b6ee2f87b0739558366f7dfc08bfb2', 'José García Funcionario',     3, 1);

-- ============================================================
-- EMPRESAS del Complejo Zona Acme
-- ============================================================
INSERT INTO empresas (id, nombre, nit, sector, telefono_contacto, email_contacto, activa) VALUES
(1, 'TechNova Solutions',    '900123456-1', 'Tecnología',       '3001234567', 'contacto@technova.com',    1),
(2, 'Acme Financiera S.A.',  '900234567-2', 'Finanzas',         '3002345678', 'info@acmefinanciera.com',  1),
(3, 'BioSalud Corp',         '900345678-3', 'Salud',            '3003456789', 'admin@biosalud.com',       1),
(4, 'Constructora Andina',   '900456789-4', 'Construcción',     '3004567890', 'gerencia@candina.com',     1),
(5, 'LogiExpress Colombia',  '900567890-5', 'Logística',        '3005678901', 'ops@logiexpress.com',      1);

-- ============================================================
-- PERSONAS (Trabajadores y Visitantes)
-- ============================================================

-- Trabajadores de TechNova Solutions (Empresa 1)
INSERT INTO personas (id, documento, tipo_documento, nombre, apellido, tipo_persona, empresa_id, foto_url, estado_acceso, telefono, email) VALUES
(1,  '1001234567', 'CC', 'Andrés',   'Valencia',   'TRABAJADOR', 1, 'https://i.pravatar.cc/150?img=1',  'ACTIVO',     '3101234567', 'avalencia@technova.com'),
(2,  '1001234568', 'CC', 'Laura',    'Mendoza',    'TRABAJADOR', 1, 'https://i.pravatar.cc/150?img=5',  'ACTIVO',     '3101234568', 'lmendoza@technova.com'),
(3,  '1001234569', 'CC', 'Ricardo',  'Soto',       'TRABAJADOR', 1, 'https://i.pravatar.cc/150?img=3',  'ACTIVO',     '3101234569', 'rsoto@technova.com');

-- Trabajadores de Acme Financiera (Empresa 2)
INSERT INTO personas (id, documento, tipo_documento, nombre, apellido, tipo_persona, empresa_id, foto_url, estado_acceso, telefono, email) VALUES
(4,  '1002345670', 'CC', 'Carolina', 'Herrera',    'TRABAJADOR', 2, 'https://i.pravatar.cc/150?img=9',  'ACTIVO',     '3202345670', 'cherrera@acmefin.com'),
(5,  '1002345671', 'CC', 'Miguel',   'Torres',     'TRABAJADOR', 2, 'https://i.pravatar.cc/150?img=7',  'ACTIVO',     '3202345671', 'mtorres@acmefin.com');

-- Trabajadores de BioSalud (Empresa 3)
INSERT INTO personas (id, documento, tipo_documento, nombre, apellido, tipo_persona, empresa_id, foto_url, estado_acceso, telefono, email) VALUES
(6,  '1003456780', 'CC', 'Sofía',    'Castro',     'TRABAJADOR', 3, 'https://i.pravatar.cc/150?img=10', 'ACTIVO',     '3303456780', 'scastro@biosalud.com'),
(7,  '1003456781', 'CC', 'Felipe',   'Morales',    'TRABAJADOR', 3, 'https://i.pravatar.cc/150?img=11', 'BLOQUEADO',  '3303456781', 'fmorales@biosalud.com');

-- Trabajador de Constructora Andina (Empresa 4)
INSERT INTO personas (id, documento, tipo_documento, nombre, apellido, tipo_persona, empresa_id, foto_url, estado_acceso, telefono, email) VALUES
(8,  '1004567890', 'CC', 'Diego',    'Ramírez',    'TRABAJADOR', 4, 'https://i.pravatar.cc/150?img=12', 'ACTIVO',     '3404567890', 'dramirez@candina.com');

-- Visitantes (sin empresa asignada permanentemente)
INSERT INTO personas (id, documento, tipo_documento, nombre, apellido, tipo_persona, empresa_id, foto_url, estado_acceso, telefono, email) VALUES
(9,  '1005678901', 'CC',        'Valentina', 'Ospina',    'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=20', 'ACTIVO',      '3105678901', 'vospina@gmail.com'),
(10, '1005678902', 'CC',        'Camilo',    'Ríos',      'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=14', 'ACTIVO',      '3105678902', 'crios@gmail.com'),
(11, '1005678903', 'CE',        'Jean',      'Dupont',    'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=15', 'ACTIVO',      '3105678903', 'jdupont@mail.com'),
(12, 'AB1234567',  'PASAPORTE', 'Sarah',     'Johnson',   'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=16', 'ACTIVO',      '3105678904', 'sjohnson@mail.com'),
(13, '1005678905', 'CC',        'Nicolás',   'Bermúdez',  'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=17', 'RESTRINGIDO', '3105678905', 'nbermudez@mail.com'),
(14, '1005678906', 'CC',        'Isabella',  'Castaño',   'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=23', 'ACTIVO',      '3105678906', 'icastano@mail.com'),
(15, '1005678907', 'TI',        'Samuel',    'Peña',      'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=18', 'ACTIVO',      '3105678907', 'spena@mail.com');

-- ============================================================
-- VISITAS (diferentes estados para pruebas)
-- ============================================================

-- Visita 1: Cerrada exitosamente (flujo completo)
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(1, 9, 1, 4, 'Reunión de proyecto con TechNova', '2026-08-25 08:30:00', '2026-08-25 12:00:00', 'CERRADA', 'Visitante atendida correctamente', '2026-08-24 16:00:00');

-- Visita 2: Pre-registrada y aprobada, pendiente de llegada
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(2, 10, 2, 5, 'Auditoría financiera trimestral', NULL, NULL, 'APROBADO', 'Traer documentos de identificación', '2026-08-26 07:00:00');

-- Visita 3: Persona actualmente DENTRO del complejo
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(3, 11, 1, 4, 'Soporte técnico internacional', '2026-08-26 07:15:00', NULL, 'DENTRO', NULL, '2026-08-25 14:00:00');

-- Visita 4: Pendiente de aprobación (invitado no anunciado)
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(4, 12, 3, NULL, 'Entrevista de trabajo área médica', NULL, NULL, 'PENDIENTE', 'Invitada no anunciada - esperando aprobación', '2026-08-26 07:20:00');

-- Visita 5: Rechazada
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(5, 13, 2, 5, 'Visita comercial', NULL, NULL, 'RECHAZADO', 'Persona con restricción de acceso', '2026-08-25 10:00:00');

-- Visita 6: Cerrada por sistema (salida olvidada)
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(6, 14, 4, 4, 'Entrega de documentos', '2026-08-24 09:00:00', NULL, 'CERRADA_POR_SISTEMA', 'Salida no registrada - cerrada automáticamente por el sistema', '2026-08-24 08:30:00');

-- Visita 7: Trabajador - entrada normal
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(7, 1, 1, NULL, 'Jornada laboral', '2026-08-26 07:00:00', NULL, 'DENTRO', NULL, '2026-08-26 07:00:00');

-- Visita 8: Trabajador - entrada y salida normal
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(8, 4, 2, NULL, 'Jornada laboral', '2026-08-25 07:30:00', '2026-08-25 17:30:00', 'CERRADA', NULL, '2026-08-25 07:30:00');

-- Visita 9: Pase temporal (carnet olvidado) - pendiente
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(9, 5, 2, NULL, 'Pase temporal - carnet olvidado', NULL, NULL, 'PENDIENTE', 'Trabajador sin carnet - pendiente aprobación funcionario', '2026-08-26 07:25:00');

-- Visita 10: Otra visita cerrada para historial
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(10, 15, 5, 5, 'Entrega de paquete', '2026-08-23 14:00:00', '2026-08-23 14:30:00', 'CERRADA', 'Entrega rápida sin novedad', '2026-08-23 13:00:00');

-- Más Visitantes (Agregados para pruebas)
INSERT INTO personas (id, documento, tipo_documento, nombre, apellido, tipo_persona, empresa_id, foto_url, estado_acceso, telefono, email) VALUES
(16, '1005678910', 'CC', 'Pedro', 'Páez', 'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=32', 'ACTIVO', '3105678910', 'ppaez@mail.com'),
(17, '1005678911', 'CC', 'Lucía', 'Gómez', 'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=33', 'ACTIVO', '3105678911', 'lgomez@mail.com'),
(18, '1005678912', 'CC', 'Mario', 'Vargas', 'VISITANTE', NULL, 'https://i.pravatar.cc/150?img=11', 'ACTIVO', '3105678912', 'mvargas@mail.com');

-- Visita 11: Pendiente de aprobación para BioSalud (Empresa 3 - funcionario1)
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(11, 16, 3, NULL, 'Reunión de ventas con BioSalud', NULL, NULL, 'PENDIENTE', 'Esperando aprobación del funcionario', '2026-08-27 08:00:00');

-- Visita 12: Pendiente de aprobación para BioSalud (Empresa 3 - funcionario1)
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(12, 17, 3, NULL, 'Soporte técnico', NULL, NULL, 'PENDIENTE', 'Esperando aprobación del funcionario', '2026-08-27 08:30:00');

-- Visita 13: Pendiente de aprobación para TechNova (Empresa 1)
INSERT INTO visitas (id, persona_id, empresa_destino_id, funcionario_autoriza_id, motivo, fecha_hora_entrada, fecha_hora_salida, estado, observaciones, fecha_creacion) VALUES
(13, 18, 1, NULL, 'Entrevista de trabajo', NULL, NULL, 'PENDIENTE', 'Esperando aprobación del funcionario', '2026-08-27 09:00:00');

-- ============================================================
-- INCIDENTES
-- ============================================================
INSERT INTO incidentes (id, persona_id, visita_id, tipo, descripcion, severidad, reportado_por_id, fecha_hora, estado) VALUES
(1, 13, 5,    'ACCESO_NO_AUTORIZADO',  'Persona con restricción intentó ingresar al complejo presentando documento de tercero.', 'ALTA',    2, '2026-08-25 10:15:00', 'EN_INVESTIGACION'),
(2, 7,  NULL, 'COMPORTAMIENTO_SOSPECHOSO', 'Trabajador reportado por comportamiento irregular en zona de parqueaderos.',          'MEDIA',   3, '2026-08-24 16:30:00', 'ABIERTO'),
(3, 14, 6,    'SALIDA_NO_REGISTRADA',  'Visitante no registró salida. Se cerró automáticamente por sistema.',                     'BAJA',    2, '2026-08-25 07:00:00', 'CERRADO');

-- ============================================================
-- BITÁCORA DE AUDITORÍA (registros iniciales)
-- ============================================================
INSERT INTO bitacora_auditoria (usuario_id, accion, entidad, entidad_id, detalle, fecha_hora) VALUES
(1, 'LOGIN_EXITOSO',    'USUARIO',  1, 'Inicio de sesión del administrador',                            '2026-08-25 07:00:00'),
(1, 'CREAR_EMPRESA',    'EMPRESA',  1, 'Registrada empresa TechNova Solutions',                         '2026-08-25 07:05:00'),
(1, 'CREAR_EMPRESA',    'EMPRESA',  2, 'Registrada empresa Acme Financiera S.A.',                       '2026-08-25 07:06:00'),
(1, 'CREAR_EMPRESA',    'EMPRESA',  3, 'Registrada empresa BioSalud Corp',                              '2026-08-25 07:07:00'),
(1, 'CREAR_PERSONA',    'PERSONA',  1, 'Registrado trabajador Andrés Valencia',                         '2026-08-25 07:10:00'),
(2, 'LOGIN_EXITOSO',    'USUARIO',  2, 'Inicio de sesión del guarda Pedro Martínez',                    '2026-08-25 08:00:00'),
(2, 'CHECK_IN',         'VISITA',   1, 'Check-in de visitante Valentina Ospina a TechNova',             '2026-08-25 08:30:00'),
(2, 'CHECK_OUT',        'VISITA',   1, 'Check-out de visitante Valentina Ospina',                       '2026-08-25 12:00:00'),
(4, 'LOGIN_EXITOSO',    'USUARIO',  4, 'Inicio de sesión de funcionaria María Rodríguez',               '2026-08-25 14:00:00'),
(4, 'PRE_REGISTRO',     'VISITA',   3, 'Pre-registro de visita de Jean Dupont a TechNova',              '2026-08-25 14:05:00'),
(2, 'REGISTRAR_INCIDENTE', 'INCIDENTE', 1, 'Incidente registrado: acceso no autorizado de Nicolás Bermúdez', '2026-08-25 10:15:00'),
(1, 'CAMBIAR_ESTADO',   'PERSONA',  7, 'Persona Felipe Morales bloqueada por incidentes reiterados',    '2026-08-24 17:00:00'),
(2, 'CERRAR_POR_SISTEMA', 'VISITA', 6, 'Visita cerrada automáticamente por salida no registrada',       '2026-08-25 07:00:00');

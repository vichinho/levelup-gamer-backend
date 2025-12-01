# README.md - Microservicio Gestión de Usuarios

## Descripción del Proyecto

Microservicio de gestión de usuarios, roles y permisos para la plataforma Level-Up Gamer. Este microservicio forma parte de una arquitectura de microservicios para una tienda online de productos gaming que opera en Chile.

## Información del Proyecto

- **Nombre:** gestion-usuarios
- **Tipo:** Microservicio REST API
- **Puerto:** 8081
- **Base de datos:** MySQL 8.0
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.3.4

## Tecnologías Utilizadas

- Spring Boot 3.3.4
- Spring Data JPA
- Spring Web
- MySQL 8.0
- Maven
- Lombok 1.18.34
- BCrypt (jbcrypt 0.4)
- Bean Validation
- Hibernate 6.5.3

## Estructura del Proyecto

```
gestion-usuarios/
├── src/main/java/com/levelup/gestionusuarios/
│   ├── entity/              (UsuarioEntity, RolEntity, PermisoEntity)
│   ├── repository/          (UsuarioRepository, RolRepository, PermisoRepository)
│   ├── model/               (TipoRol enum)
│   ├── dto/                 (UsuarioDTO, RolDTO, PermisoDTO, UsuarioCreateDTO)
│   ├── service/             (UsuarioService, RolService, PermisoService)
│   ├── controller/          (UsuarioController, RolController, PermisoController)
│   └── GestionUsuariosApplication.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## Funcionalidades Principales

### Gestión de Usuarios

- CRUD completo de usuarios
- Validación de edad (mayores de 18 años)
- Descuento automático del 20% para correos @duoc.cl
- Encriptación de contraseñas con BCrypt
- Búsqueda por nombre, apellido o email
- Borrado lógico y físico de usuarios
- Sistema de puntos LevelUp

### Gestión de Roles

- CRUD completo de roles
- Asignación de permisos a roles
- Roles predefinidos: ADMIN, USER, MODERATOR

### Gestión de Permisos

- CRUD completo de permisos
- Definición de permisos por recurso y método HTTP
- Permisos granulares para cada operación

### Relaciones

- Many-to-Many entre Usuarios y Roles
- Many-to-Many entre Roles y Permisos

## Configuración de la Base de Datos

### Crear Base de Datos

```sql
CREATE DATABASE levelup_usuarios;
```

### Configuración en application.properties

```properties
server.port=8081
spring.application.name=gestion-usuarios

spring.datasource.url=jdbc:mysql://localhost:3306/levelup_usuarios?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### Cambiar Plugin de Autenticación MySQL (si es necesario)

```sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'admin';
FLUSH PRIVILEGES;
```

## Instalación y Ejecución

### Requisitos Previos

- JDK 17 o superior
- Maven 3.6 o superior
- MySQL 8.0
- Postman (para pruebas)

### Pasos de Instalación

1. Clonar el repositorio
2. Configurar MySQL con las credenciales correctas
3. Actualizar application.properties si es necesario
4. Compilar el proyecto:

```bash
mvn clean install
```

5. Ejecutar la aplicación:

```bash
mvn spring-boot:run
```

La aplicación iniciará en http://localhost:8081

## Endpoints de la API

### Usuarios

| Método | Endpoint                                | Descripción                  |
| ------ | --------------------------------------- | ---------------------------- |
| POST   | /api/usuarios                           | Crear usuario                |
| GET    | /api/usuarios                           | Obtener todos los usuarios   |
| GET    | /api/usuarios/{id}                      | Obtener usuario por ID       |
| GET    | /api/usuarios/email/{email}             | Obtener usuario por email    |
| GET    | /api/usuarios/activos                   | Obtener usuarios activos     |
| GET    | /api/usuarios/buscar?termino={termino}  | Buscar por nombre o apellido |
| PUT    | /api/usuarios/{id}                      | Actualizar usuario           |
| DELETE | /api/usuarios/{id}                      | Eliminar usuario (lógico)    |
| DELETE | /api/usuarios/{id}/fisico               | Eliminar usuario físicamente |
| POST   | /api/usuarios/{usuarioId}/roles/{rolId} | Agregar rol a usuario        |
| DELETE | /api/usuarios/{usuarioId}/roles/{rolId} | Remover rol de usuario       |

### Roles

| Método | Endpoint                                | Descripción             |
| ------ | --------------------------------------- | ----------------------- |
| POST   | /api/roles                              | Crear rol               |
| GET    | /api/roles                              | Obtener todos los roles |
| GET    | /api/roles/{id}                         | Obtener rol por ID      |
| GET    | /api/roles/nombre/{nombre}              | Obtener rol por nombre  |
| PUT    | /api/roles/{id}                         | Actualizar rol          |
| DELETE | /api/roles/{id}                         | Eliminar rol            |
| POST   | /api/roles/{rolId}/permisos/{permisoId} | Agregar permiso a rol   |
| DELETE | /api/roles/{rolId}/permisos/{permisoId} | Remover permiso de rol  |

### Permisos

| Método | Endpoint                          | Descripción                      |
| ------ | --------------------------------- | -------------------------------- |
| POST   | /api/permisos                     | Crear permiso                    |
| GET    | /api/permisos                     | Obtener todos los permisos       |
| GET    | /api/permisos/{id}                | Obtener permiso por ID           |
| GET    | /api/permisos/metodo/{metodoHttp} | Obtener permisos por método HTTP |
| GET    | /api/permisos/recurso/{recurso}   | Obtener permisos por recurso     |
| PUT    | /api/permisos/{id}                | Actualizar permiso               |
| DELETE | /api/permisos/{id}                | Eliminar permiso                 |

## Ejemplos de Uso

### Crear Usuario

```json
POST /api/usuarios
Content-Type: application/json

{
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@duoc.cl",
    "password": "password123",
    "fechaNacimiento": "1995-05-15",
    "telefono": "+56912345678",
    "direccion": "Av. Providencia 1234, Santiago"
}
```

### Crear Rol

```json
POST /api/roles
Content-Type: application/json

{
    "nombre": "ROLE_ADMIN",
    "descripcion": "Administrador del sistema"
}
```

### Crear Permiso

```json
POST /api/permisos
Content-Type: application/json

{
    "nombre": "USER_READ",
    "descripcion": "Permiso para leer usuarios",
    "recurso": "/api/usuarios",
    "metodoHttp": "GET"
}
```

## Validaciones Implementadas

- Email debe ser válido y único
- Contraseña mínimo 6 caracteres
- Edad mínima 18 años
- Campos nombre, apellido, email y password son obligatorios
- Fecha de nacimiento debe ser en el pasado

## Reglas de Negocio

- Usuario con email @duoc.cl recibe automáticamente 20% de descuento
- El campo esMayorEdad se calcula automáticamente según la fecha de nacimiento
- Las contraseñas se almacenan encriptadas con BCrypt
- El password no se incluye en las respuestas JSON por seguridad
- Los usuarios nuevos inician con 0 puntos LevelUp
- El rol USER se asigna automáticamente al crear un usuario

## Pruebas con Postman

Se incluye una colección de Postman con 44 requests organizados en 4 carpetas:

1. Permisos (8 requests)
2. Roles (10 requests)
3. Usuarios (15 requests)
4. Validaciones y Casos Especiales (6 requests)

Importar la colección desde el archivo: `LevelUpGamer-GestionUsuarios.postman_collection.json`

## Autor

Proyecto desarrollado como parte de la Sumativa 2 - Optativo JAVA

## Estado del Proyecto

Completado y funcional

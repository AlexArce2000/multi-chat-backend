# Multi-Chat Backend (Spring Boot & WebSocket)

Backend para la aplicación Multi-Chat, una plataforma de chat en tiempo real construida con Java y el ecosistema de Spring. Proporciona una API REST para la gestión de usuarios y salas, y utiliza WebSockets con STOMP para la comunicación de mensajes en tiempo real.

## Características Principales

-   **Autenticación Segura:** Registro e inicio de sesión de usuarios con contraseñas hasheadas (BCrypt) y gestión de sesión mediante **JSON Web Tokens (JWT)**.
-   **Salas de Chat Públicas y Privadas:** Creación y gestión de salas con diferentes niveles de acceso. Las salas privadas están protegidas por contraseña.
-   **Mensajería en Tiempo Real:** Comunicación bidireccional instantánea gracias a **Spring WebSocket** y el protocolo STOMP.
-   **Arquitectura de Datos Híbrida:**
    -   **PostgreSQL:** Para almacenar datos relacionales y críticos (usuarios, salas, membresías).
    -   **MongoDB:** Para almacenar el gran volumen de mensajes del chat de forma eficiente.
-   **Seguridad Robusta:** Protección de endpoints REST y del canal de WebSocket para asegurar que solo usuarios autenticados y autorizados puedan realizar acciones.
-   **Documentación de API:** Generación automática de documentación interactiva con **SpringDoc (Swagger UI)**.

## Stack Tecnológico

-   **Lenguaje:** Java 17+
-   **Framework:** Spring Boot 3+
-   **Seguridad:** Spring Security 6+
-   **Bases de Datos:**
    -   PostgreSQL
    -   MongoDB
-   **Acceso a Datos:** Spring Data JPA, Spring Data MongoDB
-   **Tiempo Real:** Spring WebSocket
-   **Gestor de Dependencias:** Maven
-   **API Docs:** SpringDoc OpenAPI

### **Prerrequisitos**
-   **JDK 17** o superior.
-   **Maven 3.8** o superior.
-   **PostgreSQL 14** o superior, corriendo en su puerto por defecto (`5432`).
-   **MongoDB 6** o superior, corriendo en su puerto por defecto (`27017`).
-   **IntelliJ IDEA** o VS Code.


### Principales Endpoints

-   `POST /api/auth/register`: Registra un nuevo usuario.
-   `POST /api/auth/login`: Inicia sesión y obtiene un token JWT.
-   `POST /api/v1/rooms`: Crea una nueva sala (requiere token).
-   `GET /api/v1/rooms/public`: Obtiene la lista de salas públicas (requiere token).
-   `POST /api/v1/rooms/{roomId}/join`: Se une a una sala (requiere token).

##  WebSocket

-   **Endpoint de Conexión:** `ws://localhost:8080/ws`
-   **Destinos de Envío:**
    -   `/app/chat.sendMessage/{roomId}`
    -   `/app/chat.addUser/{roomId}`
-   **Topics de Suscripción:**
    -   `/topic/rooms/{roomId}`
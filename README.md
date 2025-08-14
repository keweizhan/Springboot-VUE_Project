# Frontend-Backend Separated Project (JWT Authentication)

A template project built with **Spring Boot 3** (backend) and **Vue 3** (frontend), following a fully separated architecture.  
Integrates multiple technologies and implements a **JWT-based authentication** scheme.

---

## Backend Features & Technical Highlights

Implements basic features such as **user registration, user login, password reset**, and corresponding APIs.

- **MyBatis-Plus** as the persistence framework for easier database operations.
- **Redis** for storing verification codes for registration/password reset, with expiration control.
- **RabbitMQ** for queuing SMS sending tasks, processed asynchronously by listeners.
- **Spring Security** as the authentication framework, manually integrated with a JWT validation scheme.
- **Redis** for IP-based request rate limiting to prevent API abuse.
- Separate **View Layer Objects** and **Data Layer Objects**, with utility methods leveraging reflection for quick conversion.
- Unified JSON format for error and exception responses, ensuring consistent handling on the frontend.
- Manual CORS handling via filters.
- Automatic **Snowflake ID** generation for all requests, aiding issue tracking in production.
- Environment-specific configuration handling (different setups for development and production).
- Logging includes complete request details and the corresponding Snowflake ID, with file-based logging support.
- Clean project structure with clear responsibilities, comprehensive comments, and ready-to-use setup.

---

## Frontend Features & Technical Highlights

Includes **user registration, user login, password reset** pages, and a simple homepage.

- **Vue Router** for client-side routing.
- **Axios** for asynchronous HTTP requests.
- **Element Plus** as the UI component library.
- **VueUse** for dark mode switching.
- **unplugin-auto-import** for on-demand imports, reducing final bundle size.

---

## Getting Started

### Backend
1. Clone the repository and navigate to the backend directory.
2. Configure application properties for database, Redis, RabbitMQ, etc.
3. Build and run the Spring Boot project.

### Frontend
1. Navigate to the frontend directory.
2. Install dependencies:
   ```bash
   npm install

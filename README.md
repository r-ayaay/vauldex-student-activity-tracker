# My Full-Stack Activity Logger

A simple activity logging web application built with **Vue 3 (frontend)** and **Spring Boot + Kotlin (backend)**.

---

## 🛠 Prerequisites

Before running the project, make sure you have installed:

-   **Node.js** (v18+) – [https://nodejs.org/](https://nodejs.org/)
-   **Java JDK** (v17+) – [https://adoptium.net/](https://adoptium.net/)
-   **Maven** or **Gradle** (for the backend)
-   **Database** (PostgreSQL/MySQL/etc.) – make sure it's running locally

---

## ⚙️ Backend Setup

1. Go to the backend folder in your terminal.
2. Create a file named `.env`:

```
SERVER_PORT=8080
DB_URL=jdbc:postgresql://localhost:5432/your_db_name
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password
JWT_SECRET=your_secret_key
```

> Replace `your_db_name`, `your_db_user`, `your_db_password`, and `your_secret_key` with your own values.

3. Make sure your `application.yml` reads environment variables:

```
server:
  port: ${SERVER_PORT:8080}

spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/default_db}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
  jpa:
    hibernate:
      ddl-auto: update

jwt:
  secret: ${JWT_SECRET}
```

4. Run the backend:

```
./mvnw spring-boot:run or ./gradle spring-boot:run
```

> The backend should run at `http://localhost:8080`.

---

## ⚡ Frontend Setup

1. Go to the frontend folder in your terminal.
2. Create a file named `.env`:

```
VITE_API_URL=http://localhost:8080
VITE_APP_URL=http://localhost:5173
```

3. Install dependencies:

```
npm install
```

4. Start the development server:

```
npm run dev
```

> The frontend should run at `http://localhost:5173`.

---

## 🗄 Database Setup

1. Make sure your database server is running.
2. Create the database (if it doesn’t exist):

```
CREATE DATABASE your_db_name;
```

3. Ensure the username and password in `.env` can access the database.
    > Spring Boot will automatically create tables.

---

## ▶️ Running the App

1. Start the **database**.
2. Start the **backend**.
3. Start the **frontend**.
4. Open your browser at [http://localhost:5173](http://localhost:5173)

---

## 📝 Example `.env.example`

**Backend:**

```
SERVER_PORT=8080
DB_URL=jdbc:postgresql://localhost:5432/your_db_name
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password
JWT_SECRET=your_secret_key
```

**Frontend:**

```
VITE_API_URL=http://localhost:8080
VITE_APP_URL=http://localhost:5173
```

> Copy `.env.example` → `.env` and edit values before running.

---

## ✅ Notes

-   Frontend communicates with backend using `VITE_API_URL`.
-   Backend uses `JWT_SECRET` for authentication.
-   Changing ports or DB name is easy with the `.env` files.
-   Pagination and activity logging features are included.

---

## Optional Diagram

Here’s a simple visual of how the app works:

```
[Browser / Frontend (Vue)]  -->  [Backend (Spring Boot + Kotlin)]  -->  [Database (PostgreSQL)]
        ^                                                          |
        |----------------------------------------------------------|
```

-   The frontend talks to the backend using HTTP requests.
-   The backend talks to the database to store and retrieve data.
-   Responses go back from backend → frontend → browser.

## Frontend Features and Usage

This frontend application is built with Vue 3 and Vite. It provides an interactive interface for managing activities and activity logs. Below is an overview of its main features:

### 1. Activity Management

-   **Add Activity**: Users can add a new activity via a modal form. Each activity includes:
    -   Type
    -   Description
-   **Edit Activity**: Click on an activity's type or description (depending on configuration) to edit it inline. Changes are saved on `Enter` or when clicking outside the input.
-   **Delete Activity**: Remove activities directly from the list by clicking the delete icon.
-   **Sorting**: Activities are sorted by ID to ensure edited items remain in their place.

### 2. Activity Logs

-   **Log Activities**: Users can log a selected activity to create a new activity log entry with the current timestamp.
-   **Edit Logs**:
    -   Change the activity associated with a log entry via a dropdown.
    -   Edit the timestamp inline using a `datetime-local` input.
-   **Delete Logs**: Remove individual log entries with a delete button.
-   **Pagination**: Activity logs are paginated for easier navigation.

### 3. Filters

-   **Activity Type Filter**: Filter logs by a specific activity type.
-   **Date Filters**: Filter logs by start and end dates.
-   **Reset Filters**: Clear all filters to view all logs.

### 4. Responsive Design

-   The interface adjusts for various screen sizes:
    -   On larger screens (`lg`), form inputs and filter controls are stacked vertically.
    -   Buttons, selects, and inputs resize appropriately for readability.

### 5. User Feedback

-   Buttons and inputs provide visual feedback, including:
    -   Disabled states when operations are in progress.
    -   Hover effects and underline on clickable text for better UX.
    -   Loading indicators when logging activities or submitting new activity entries.

### 6. Integration

-   Communicates with a backend API for:
    -   Fetching activities and activity logs.
    -   Adding, updating, and deleting activities and logs.
    -   Filtering activity logs by type and date.

### 7. Environment Variables

-   Frontend URL and API endpoints can be configured via environment variables for local development and deployment. By default:
    -   Frontend: `http://localhost:5173`
    -   Backend API: `http://localhost:8080`

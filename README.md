# Currency Converter Overview

Java-based API (RESTful) that converts currency values using rates from [Swop](https://swop.cx/). The API handle input validation, caching, and security features (CSRF and CSP). Containerize the application with Docker, and develop a Vue.js frontend for user interaction.

## How to Run the Application with Docker

1. **Navigate to Project Directory:**

   Change to the project directory where your `docker-compose.yml` file is located. For example:

   ```bash
   cd <repository-directory>
   ```

2. **API Key:**

   - Ensure that the API key is included in the `<repository-directory>/.env` file. You can obtain the key from [Swop](https://swop.cx/rest/rates). Add the key to the `.env` file using the format:
     ```plaintext
     SWOP_API_KEY=your-api-key-here
     ```
   - Replace `your-api-key-here` with the actual API key provided by Swop.

3. **Docker:**

   - Make sure Docker is installed and running.
   - Run:
     ```bash
     docker-compose up --build
     ```

4. **Accessible:**

   - The application will be accessible at:
     ```bash
     http://localhost:8081/
     ```

   ## **Demo**

   You can check out the demo of the application here: [Videos](https://drive.google.com/drive/folders/1vnLJPZavJKMFOnesH_l3--ZXI7PhKGOB?usp=sharing)

   ## **Spring Boot Application**

   This is a Spring Boot application built with Maven. Below are the instructions to build and run the application.

   Prerequisites

   - Java JDK 8+
   - Apache Maven
   - Getting Started

   1. Clone the Repository
      ```bash
       git clone <repository-url>
       cd <repository-directory>
      ```
   2. Build the Application
      ```bash
       mvn clean install
      ```
   3. Package the Application
      ```bash
       mvn clean package
      ```
   4. Run the Application
      ```bash
       mvn spring-boot:run
      ```
   5. Test the Application
      ```bash
       mvn test
      ```

   ## **Setting Up Vue.js Project**

   Prerequisites
   Node.js: Ensure you have Node.js installed. Vue CLI requires Node.js
   version 12.0.0 or later. You can download Node.js from nodejs.org. For
   optimal performance and compatibility, it is recommended to use the latest
   LTS (Long Term Support) version.

   npm: npm (Node Package Manager) comes bundled with Node.js. The minimum
   required npm version is 6.0.0. You can check your npm version with:

   ```bash
    npm-v
   ```

   1. Navigate to Project Directory For example:

      ```bash
       cd <repository-directory>/frontend
      ```

   2. Install packages
      ```bash
       npm install
      ```
   3. Starts a development server with hot-reloading enabled.

      ```bash
       npm run serve
      ```

   4. Compiles and minifies the project for production.

      ```bash
       npm run build
      ```

   5. Lints and fixes files according to the configured linting rules.

      ```bash
       npm run lint
      ```

   6. Runs unit tests.
      ```bash
        npm run test
      ```

   ## Usage

   - Access the backend API at `http://localhost:8080/api/convert?from=EUR&to=USD&amount=30`.
   - Access the frontend at `http://localhost:8081`.

   ## Testing

   - To test the API via the terminal, use curl:

   ```bash
     curl "http://localhost:8080/api/convert?from=EUR&to=USD&amount=30"
   ```

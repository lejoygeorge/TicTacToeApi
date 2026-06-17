# Tic-Tac-Toe Stateless REST API

A highly optimized, stateless REST API for playing Tic-Tac-Toe. Built with **Java 17** and **Spring Boot 3**, this project strictly adheres to **SOLID principles** and uses an **API-Design-First** approach via OpenAPI (Swagger) code generation.

---

## 🚀 Features

* **Stateless Architecture:** The server does not maintain session state. The entire game board is passed in the request and response, allowing the API to handle thousands of concurrent games effortlessly.
* **Dynamic N x N Scalability:** The rule engine mathematically generates winning combinations at startup, allowing the game to dynamically support custom board sizes (e.g., 3x3, 4x4, 5x5) without changing core logic.
* **API-Design-First:** Built using OpenAPI 3.0 specifications. Data models and API interfaces are auto-generated at compile time.
* **Extensible Validation (Rules Pattern):** Validation is decoupled into highly specific, single-responsibility rules (Strategy Pattern) orchestrated by a central validator, ensuring absolute adherence to the Open/Closed Principle.
* **Java 17 Optimized:** Utilizes modern Java features such as `var`, `String.formatted()`, immutable collections (`List.of`), and functional streams.
* **Interactive Documentation:** Out-of-the-box Swagger UI for easy API exploration and testing.

---

## 🛠️ Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.x
* **API Specification:** OpenAPI 3.0 (Swagger)
* **Code Generation:** OpenAPI Generator Maven Plugin
* **Documentation UI:** Springdoc OpenAPI Starter WebMVC UI
* **Testing:** JUnit 5, Mockito, Spring Boot Test (`MockMvc`)
* **Code Quality:** JaCoCo, SonarLint / SonarQube, PiTest

---

## ⚙️ Architecture

The application is structured to decouple routing, validation, and business logic:

1. **API/Models (Generated):** `api-spec.yaml` drives the automatic generation of DTOs and Controller interfaces.
2. **Controller (`TicTacToeController`):** Implements the generated interface and acts as the entry point for HTTP requests.
3. **Service (`TicTacToeServiceImpl`):** The orchestrator that coordinates validation, state updates, and rule evaluation.
4. **Validation Engine (`GameValidationRule`):** Utilizes the Rules Pattern. A central orchestrator (`TicTacToeValidator`) sequentially routes requests through specific, isolated rules (e.g., `BoardStateRule`, `PlayerRule`, `BoundsAndAvailabilityRule`).
5. **Rule Engine (`TicTacToeRuleEngine`):** Dynamically evaluates the board array to check for wins, draws, and determines the next player using Java Streams.

---

## 🏃 Getting Started

### Prerequisites
* Java 17 or higher
* Maven 3.8+

### Installation & Execution

1. **Clone the repository:**
    ```bash
   git clone [https://github.com/yourusername/tictactoe-api.git](https://github.com/yourusername/tictactoe-api.git)
   cd tictactoe-api

2. **Compile and generate the OpenAPI models:**
   ```bash
   mvn clean compile
   ```


3. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`.

---

## 📖 API Documentation (Swagger UI)

Once the application is running, you can interactively test the endpoints without writing a single line of frontend code.

* **Swagger UI:** [http://localhost:8080/swagger-ui.html](https://www.google.com/search?q=http://localhost:8080/swagger-ui.html)
* **OpenAPI JSON Spec:** [http://localhost:8080/v3/api-docs](https://www.google.com/search?q=http://localhost:8080/v3/api-docs)

---

## 🎮 Endpoints Overview

### 1. Initialize a Game

**`GET /api/tictactoe/init`**

Returns a pristine board state and sets up the starting player (Player X).

**Response (200 OK):**

```json
{
  "board": ["0", "1", "2", "3", "4", "5", "6", "7", "8"],
  "nextPlayer": "X",
  "status": "IN_PROGRESS",
  "message": "Game initialized. Player X to move first."
}

```

### 2. Play a Move

**`POST /api/tictactoe/play`**

Submits a player's move. Validates the move, updates the board, and evaluates win/draw conditions.

**Request Body:**

```json
{
  "board": ["0", "1", "2", "3", "4", "5", "6", "7", "8"],
  "currentPlayer": "X",
  "position": 4
}

```

**Response (200 OK - In Progress):**

```json
{
  "board": ["0", "1", "2", "3", "X", "5", "6", "7", "8"],
  "nextPlayer": "O",
  "status": "IN_PROGRESS",
  "message": "Move accepted."
}

```

**Response (400 Bad Request - Invalid Move):**

```json
{
  "board": ["0", "1", "2", "3", "X", "5", "6", "7", "8"],
  "nextPlayer": "O",
  "status": "INVALID_MOVE",
  "message": "Position already taken! Choose an empty spot."
}

```

## 🧪 Testing & Code Coverage

This project uses JaCoCo to ensure high code coverage. The generated models (`com.game.tictactoeapi.model.*`) are excluded from the coverage report to ensure metrics strictly reflect business and controller logic.

To run the unit tests and generate the coverage report:

```bash
mvn clean test

```

Once the tests pass, view the interactive HTML report by opening:
`target/site/jacoco/index.html` in your web browser.

## 🧪 Mutation Testing (Pitest)
To evaluate the absolute quality and effectiveness of the test suite, this project utilizes Pitest for mutation testing. It intentionally injects flaws (mutants) into the compiled bytecode to ensure the unit tests actually fail when logic is compromised.

Note: Due to Java agent conflicts between JaCoCo and Pitest, you must explicitly skip JaCoCo when running the mutation analysis.

To execute the mutation tests:

```bash
mvn clean test-compile org.pitest:pitest-maven:mutationCoverage -Djacoco.skip=true

```
View the mutation coverage report to see killed and survived mutants by opening:
`target/pit-reports/index.html` in your web browser.
---

*Developed by Lejoy George*

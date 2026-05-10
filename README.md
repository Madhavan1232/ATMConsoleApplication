• # ATM Console Application

  A simple console-based ATM application built with Java, Spring Boot, Maven, and MySQL.

  ## Requirements

  Install these before running the project:

  - Java 21
  - MySQL Server
  - Maven

  You can skip installing Maven globally if you use the included Maven Wrapper:

  - `mvnw`
  - `mvnw.cmd`

  ## Dependencies

  This project uses these main dependencies:

  - Spring Boot
  - Spring Data JPA
  - Spring Data JDBC
  - MySQL Connector
  - Lombok

  All dependencies are managed in `pom.xml` and will be downloaded automatically by Maven.

  ## Database Setup

  Create a MySQL database named:

  ```sql
  CREATE DATABASE atmsys;

  Update src/main/resources/application.properties if your MySQL username or password is different.

  ## How to Run

  ### Windows

  mvnw.cmd spring-boot:run

  ### Linux / Mac

  ./mvnw spring-boot:run

  ### Or using Maven

  mvn spring-boot:run

  ## How to Use

  After running the project, the ATM menu will appear in the console.

  You can:

  - Create a new account
  - Login with account number and PIN
  - Check balance
  - Deposit money
  - Withdraw money
  - Change PIN
  - Login as admin

  ## Admin Login

  Admin ID: admin
  Admin PIN: 1234

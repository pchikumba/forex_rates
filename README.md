# Forex Rates Application

This project is a Spring Boot application that fetches and stores exchange rates, calculates bank-style rates, 
and exposes APIs for authentication and currency management. It can be run locally using Docker and MySQL.  

---

## Prerequisites

- Java 17 installed
- Maven installed (optional if using Docker)
- Docker & Docker Compose installed
- Postman or Swagger UI for API testing  

---

## Setup Instructions

### 1. Clone the project

git clone https://github.com/pchikumba/forex_rates

2. Build the project
Before building, make sure you update your database settings in application-dev.properties

spring.datasource.url=jdbc:mysql://localhost:3306/forex_rates
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update

cd forex_rates
mvn clean install

## run the jar

java -jar forex_rates-0.0.1-SNAPSHOT.jar

## Access auth endpoints using swagger
http://localhost:5300/swagger-ui.html

Other APIs
Use Postman for all other APIs:
Include the Authorization: Bearer <token> header obtained from the auth API.
Example:
GET http://localhost:5300/api/v1/rates
Authorization: Bearer <your-token>


# Blood Bank Donation System

A microservices-based Blood Bank Donation System implemented using Spring Boot and Spring Cloud Gateway.

## System Architecture

The system consists of three main microservices and a gateway service:

1. **Donor Service** (Port: 8081)
   - Manages donor information and donations
   - Handles donor registration and updates
   - Tracks donation history

2. **Blood Inventory Service** (Port: 8082)
   - Manages blood inventory levels
   - Tracks blood units by blood type
   - Handles inventory updates

3. **Request Service** (Port: 8083)
   - Manages blood requests from hospitals/patients
   - Processes request fulfillment
   - Tracks request status

4. **API Gateway** (Port: 8085)
   - Single entry point for all client requests
   - Routes requests to appropriate services
   - Handles load balancing

## Technology Stack

- Java 17
- Spring Boot 3.x
- Spring Cloud Gateway
- Spring Data JPA
- MySQL Database
- Maven for dependency management

## Project Structure

```
blood-bank-system/
├── donor-service/
├── inventory-service/
├── request-service/
├── gateway-service/
└── pom.xml
```

## Setup Instructions

1. Clone the repository
2. Navigate to each service directory and run:
   ```bash
   mvn clean install
   ```
3. Start MySQL server
4. Start each service in the following order:
   - Gateway Service
   - Donor Service
   - Inventory Service
   - Request Service

## API Endpoints

### Donor Service
- POST /api/donors - Register new donor
- GET /api/donors - List all donors
- GET /api/donors/{id} - Get donor details
- PUT /api/donors/{id} - Update donor information

### Inventory Service
- GET /api/inventory - Get current blood inventory
- PUT /api/inventory/{bloodType} - Update inventory levels
- GET /api/inventory/{bloodType} - Get specific blood type inventory

### Request Service
- POST /api/requests - Create new blood request
- GET /api/requests - List all requests
- GET /api/requests/{id} - Get request status
- PUT /api/requests/{id} - Update request status

All endpoints are accessible through the API Gateway at http://localhost:8085

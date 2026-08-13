# Task: Create Product CRUD Module Based on Spring Boot Skeleton Rules

## Context

The system provides product management capability.

This task introduces a product CRUD module for internal product management.
The implementation must follow the existing Spring Boot skeleton conventions and engineering rules.

---

## Objective

Implement a product management module supporting:

* Product creation
* Product update
* Product deletion
* Product detail query
* Product list query

The implementation should demonstrate:

* Correct project structure
* Proper separation of Controller, Service, Repository, and Entity responsibilities
* Standard API response format
* DTO and Entity boundary enforcement
* Request validation
* Pagination and query filtering support

---

## Scope

The resource implemented in this task is:

```
Product
```

The module should provide the following capabilities:

* Create a product
* Query product list
* Query product detail by id
* Update product by id
* Delete product by id

---

# API Requirements

## Create Product

### Endpoint

```
POST /products
```

### Request Fields

| Field       | Type       | Required | Description         |
| ----------- | ---------- | -------- | ------------------- |
| name        | String     | Yes      | Product name        |
| price       | BigDecimal | Yes      | Product price       |
| description | String     | No       | Product description |

---

## Query Product List

### Endpoint

```
GET /products
```

### Supported Features

The API must support:

* Pagination
* Name wildcard search
* Created time range filtering
* Price range filtering
* Sorting by creation time descending

### Query Fields

| Field          | Type          | Description                     |
| -------------- | ------------- | ------------------------------- |
| name           | String        | Wildcard search by product name |
| createdAtStart | LocalDateTime | Creation time start             |
| createdAtEnd   | LocalDateTime | Creation time end               |
| minPrice       | BigDecimal    | Minimum price                   |
| maxPrice       | BigDecimal    | Maximum price                   |
| page           | Integer       | Page number                     |
| size           | Integer       | Page size                       |

---

## Query Product Detail

### Endpoint

```
GET /products/{id}
```

### Description

Query product information by product id.

---

## Update Product

### Endpoint

```
PUT /products/{id}
```

### Request Fields

| Field       | Type       | Required | Description         |
| ----------- | ---------- | -------- | ------------------- |
| name        | String     | Yes      | Product name        |
| price       | BigDecimal | Yes      | Product price       |
| description | String     | No       | Product description |

---

## Delete Product

### Endpoint

```
DELETE /products/{id}
```

### Description

Delete product by id.

The implementation should use the existing soft delete mechanism.

---

# Data Model

## Product Entity

Create a Product entity with the following fields:

| Field       | Type          | Description         |
| ----------- | ------------- | ------------------- |
| id          | Long          | Primary key         |
| name        | String        | Product name        |
| price       | BigDecimal    | Product price       |
| description | String        | Product description |
| createdAt   | LocalDateTime | Creation time       |
| updatedAt   | LocalDateTime | Last update time    |
| createdBy   | String        | Creator             |
| updatedBy   | String        | Last updater        |
| deleted     | Boolean       | Soft delete flag    |

---

# DTO Requirements

The module should define dedicated DTO objects.

## Request DTOs

Create:

```
ProductCreateRequest

ProductUpdateRequest

ProductSearchRequest
```

Rules:

* Request DTOs only contain client input fields.
* Do not expose Entity classes as API input models.

---

## Response DTO

Create:

```
ProductResponse
```

Rules:

* Controller must return DTO instead of Entity.
* Entity objects must not leave the Service layer.

---

# Validation Rules

The following validations must be implemented:

* Product name is required.
* Product price is required.
* Product price supports maximum 8 decimal places.
* Product fields should follow existing project validation conventions.

---

# Implementation Rules

The implementation must follow the Spring Boot skeleton rules.

## Controller Layer

Controller responsibilities:

* Validate request parameters.
* Call Service methods.
* Wrap response using `CommonResponse`.

Controller must not:

* Contain business logic.
* Access Repository directly.
* Perform Entity conversion.
* Manage transactions.
* Catch business exceptions.

---

## Service Layer

Service responsibilities:

* Implement business logic.
* Coordinate repository operations.
* Handle business validations.
* Convert Entity and DTO using Convertor.

---

## Repository Layer

Repository responsibilities:

* Handle database access only.
* Do not contain business logic.

---

## Entity Rules

* Entity is only used inside persistence and service layers.
* Entity must not be returned directly from API.

---

# Acceptance Criteria

The task is completed when:

## API

* All CRUD APIs are available.
* APIs return standard `CommonResponse`.
* Pagination works correctly.
* Filtering works correctly.
* Sorting works correctly.

## Code Quality

* Project structure follows skeleton conventions.
* Controller / Service / Repository responsibilities are separated.
* DTO and Entity boundaries are respected.
* Validation rules are implemented.

## Build

The project passes:

```bash
./mvnw verify
```

---

# Out of Scope

The following features are not included:

* Authentication
* Authorization
* Product category management
* Inventory management
* Product image management

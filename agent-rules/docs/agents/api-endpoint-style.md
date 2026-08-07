# API Endpoint Style

Read this when creating or changing an HTTP endpoint.

| If you are...                | Read                | It covers                                                         |
|------------------------------|---------------------|-------------------------------------------------------------------|
| request and response objects | `api-models.md`     | Request DTO, Response DTO, response envelope, object mapping      |
| pagination                   | `api-pagination.md` | Pagination parameters, paging response structure, sorting rules   |
| validation rules             | `api-validation.md` | Request validation, constraint annotations, validation error handling |


## Endpoint style
Use resource-oriented URLs.
URLs represent resources.
HTTP methods represent operations.

Good:

```text
GET    /users
GET    /users/{id}
POST   /users
PUT    /users/{id}
DELETE /users/{id}
```

Avoid RPC-style URLs:

```text
GET  /getUser
POST /createUser
POST /deleteUser
```

## Resource endpoints

Use plural nouns for resource names.

Examples:

```text
/users
/orders
/products
```

Do not use verbs in resource paths.

Bad:

```text
/users/create
/orders/delete
/getUsers
```

Good:

```text
POST   /users
DELETE /users/{id}
GET    /users
```

## CRUD operations

Use standard HTTP methods.

| Operation        | HTTP method | Example       |
| ---------------- | ----------- | ------------- |
| List resources   | GET         | `/users`      |
| Get one resource | GET         | `/users/{id}` |
| Create resource  | POST        | `/users`      |
| Replace resource | PUT         | `/users/{id}` |
| Delete resource  | DELETE      | `/users/{id}` |

## State transitions

Operations that are not CRUD and cannot be expressed as a field update must use a sub-resource action.

Always use POST.

Examples:

```text
POST /orders/{id}/cancel
POST /users/{id}/password/reset
POST /auth/login
POST /auth/logout
```

Avoid:

```text
POST /cancelOrder
POST /resetUserPassword
POST /loginUser
```

## Path parameters

Use path parameters to identify a specific resource.

Good:

```text
GET /users/{id}
GET /orders/{orderId}/items
```

Avoid putting resource identity in query parameters.

Bad:

```text
GET /users?id=123
```

## Query parameters

Use query parameters for simple:

* filtering
* sorting
* pagination

For complex queries, use POST search endpoints.

Examples:

Filtering:

```text
GET /users?status=ACTIVE
GET /orders?customerId=123
```

Sorting:

```text
GET /users?sortBy=createdAt&sortOrder=DESC
```

Pagination:

```text
GET /users?page=1&pageSize=20
```

## Complex queries

- Use GET for simple resource queries.
- Use POST with a `/search` action for complex queries that cannot be represented cleanly as query parameters.

Examples:

### Simple query:

```text
GET /users?status=ACTIVE&page=1&pageSize=20
GET /orders?customerId=123
```

### Complex query:

- Request body contains the query criteria.
- Do not use GET with a large number of query parameters.

Good
```text
POST /users/search
```

Bad
```text
GET /users?name=John&status=ACTIVE&role=ADMIN&createdFrom=2026-01-01&createdTo=2026-02-01&...
```


## Nested resources

Use nested resources when the child resource cannot exist independently.

Good:

```text
GET /users/{id}/orders
GET /orders/{id}/items
```

Avoid excessive nesting.

Bad:

```text
GET /companies/{companyId}/departments/{departmentId}/employees/{employeeId}
```

Prefer:

```text
GET /employees/{employeeId}
```

## HTTP method rules

### GET

Use for retrieving resources.

Rules:

* must not change server state
* parameters belong in path or query

### POST

Use for:

* creating resources
* state transitions
* operations that cannot be represented as CRUD
* complex queries using search endpoints

### PUT

Use for full resource replacement.

Example:

```text
PUT /users/{id}
```

The request should represent the complete resource state.

### PATCH

Use for partial updates when supported.

Example:

```text
PATCH /users/{id}
```

Do not use POST for simple field updates.

Bad:

```text
POST /users/{id}/updateStatus
```

Good:

```text
PATCH /users/{id}
```

### DELETE

Use for removing resources.

Example:

```text
DELETE /users/{id}
```



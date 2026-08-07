# API Pagination

Read this when creating or changing paginated APIs.

| If you are...                | Read                    | It covers                        |
| ---------------------------- | ----------------------- | -------------------------------- |
| endpoint style               | `api-endpoint-style.md` | URL and HTTP method rules        |
| request and response objects | `api-models.md`         | DTO naming, response envelope    |
| validation rules             | `api-validation.md`     | Bean Validation rules            |

## When to paginate

Use pagination for collection endpoints that may return a large number of records.
MUST NOT return unlimited collections.

Examples:

```text
GET /accounts
GET /orders
POST /accounts/search
```

## Page numbering

Pages are 1-based. First page is `currentPage = 1`.
MUST NOT use zero-based pages.

## Default values

When pagination parameters are omitted, use:

| Parameter     | Default |
| ------------- | ------- |
| `currentPage` | `1`    |
| `pageSize`    | `20`   |

## Page size limits

| Setting  | Value |
| -------- | ----- |
| default  | `20`  |
| maximum  | `100` |

Reject or normalize values exceeding the maximum.

## PagingRequest

The project provides a standard paging request wrapper.
Use `PagingRequest<T>` for all paginated POST query endpoints.
Do NOT create additional paging request classes.

Package: `com.matt.dto.request`

```java
@Data
@AllArgsConstructor
public class PagingRequest<T> {
    private Integer currentPage;
    private Integer pageSize;
    private T requestBody;
}
```

The generic type `T` is the query criteria DTO (e.g. `AccountQueryRequest`).

### POST search endpoints

Wrap the query DTO inside `requestBody`.

Controller signature:

```java
@PostMapping("/accounts/search")
public ResponseDTO<PagingResponse<AccountResponse>> search(
        @RequestBody PagingRequest<AccountQueryRequest> request) {}
```

Request body:

```json
{
  "currentPage": 1,
  "pageSize": 20,
  "requestBody": {
    "status": "ACTIVE",
    "keyword": "test"
  }
}
```

### GET collection endpoints

For simple GET collections, accept `currentPage` and `pageSize` as query parameters directly.

```text
GET /accounts?currentPage=1&pageSize=20
```

Do NOT use `PagingRequest<T>` for GET endpoints.

## PagingResponse

All paginated responses MUST be wrapped in `PagingResponse<T>`.
Package: `com.matt.dto.response`

```java
@Data
public class PagingResponse<T> {
    /** current page index (1-based) */
    private Integer currentPage;

    /** record count per page */
    private Integer pageSize;

    /** total matching records */
    private Long total;

    /** total pages */
    private Long pages;

    /** page records */
    private List<T> records;
}
```

## Full return type

Paginated endpoints MUST return:

```java
ResponseDTO<PagingResponse<T>>
```

Example:

```java
ResponseDTO<PagingResponse<AccountResponse>>
```

## Example JSON response

```json
{
  "code": "200",
  "message": "",
  "data": {
    "currentPage": 1,
    "pageSize": 20,
    "total": 58,
    "pages": 3,
    "records": [
      { "id": 1, "name": "Savings Account" }
    ]
  }
}
```

## Non-paginated endpoints

MUST NOT add pagination to single-resource endpoints.

```text
GET /accounts/{id}
GET /orders/{id}
```

## Sorting with pagination

Sorting parameters may be combined with pagination.

Simple sorting via query params:

```text
GET /accounts?currentPage=1&pageSize=20&sortBy=createdAt&sortOrder=DESC
```

Complex sorting belongs in the POST search request body.

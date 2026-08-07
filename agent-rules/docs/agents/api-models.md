# API Models

Read this when creating or changing request and response objects.

| If you are...    | Read                    | It covers                             |
| ---------------- | ----------------------- | ------------------------------------- |
| endpoint style   | `api-endpoint-style.md` | URL and HTTP method rules             |
| validation rules | `api-validation.md`     | Bean Validation rules                 |
| pagination       | `api-pagination.md`     | Page request and response rules       |

## Object boundaries

API layers MUST have clear object boundaries.
Entity objects MUST NOT leave the service layer.
Entity objects MUST NOT appear in controller signatures.

Default flow:

```text
Request:   Request DTO -> Controller -> Service -> Converter -> Entity -> Mapper -> Database
Response:  Database -> Mapper -> Entity -> Service -> Converter -> Response DTO -> Controller
```

## Request DTO

Request DTOs represent data received from clients.
Package: `com.matt.dto.request`

Naming:

| Purpose        | Pattern              | Example                |
| -------------- | -------------------- | ---------------------- |
| Create request | `*CreateRequest`     | `AccountCreateRequest` |
| Update request | `*UpdateRequest`     | `AccountUpdateRequest` |
| Query/search   | `*QueryRequest`      | `AccountQueryRequest`  |

MUST NOT use Entity classes as request objects.

Bad:

```java
@PostMapping("/accounts")
public ResponseDTO<Void> create(@RequestBody Account account) {}
```

Good:

```java
@PostMapping("/accounts")
public ResponseDTO<Void> create(@RequestBody AccountCreateRequest request) {}
```

## Response Payload DTO

All objects returned to clients MUST be defined as DTOs with the suffix `Response`.
Package: `com.matt.dto.response`

Naming:

| Purpose         | Pattern      | Example            |
| --------------- | ------------ | ------------------ |
| Single resource | `*Response`  | `AccountResponse`  |

MUST NOT return Entity objects from controllers.

## Response Envelope

All controller methods MUST return `ResponseDTO<T>`.
Package: `com.matt.dto.response`

```java
@Data
@AllArgsConstructor
public class ResponseDTO<T> {
    private String code;
    private String message;
    private T data;
}
```

Static factory methods:

```java
ResponseDTO.success()          // ResponseDTO<Void>, code = success constant
ResponseDTO.success(data)      // ResponseDTO<T>
ResponseDTO.error()            // ResponseDTO<Void>, code = error constant
ResponseDTO.error(message)     // ResponseDTO<Void>, custom message
ResponseDTO.error(code, msg)   // ResponseDTO<Void>, custom code and message
```

### Examples

Bad:

```java
@GetMapping("/{id}")
public Account getAccount(@PathVariable Long id) {}
```

Reason: exposes Entity directly, no envelope.

Bad:

```java
@GetMapping("/{id}")
public AccountResponse getAccount(@PathVariable Long id) {}
```

Reason: missing response envelope.

Good:

```java
@GetMapping("/{id}")
public ResponseDTO<AccountResponse> getAccount(@PathVariable Long id) {}
```

## Converter

Converters handle object conversion between layers.
Package: `com.matt.converter`

Naming:

| Purpose                         | Pattern        | Example            |
| ------------------------------- | -------------- | ------------------ |
| Convert between DTO and Entity  | `*Converter`   | `AccountConverter` |

Responsibilities:

```text
Request DTO  -> Entity      (toEntity)
Entity       -> Response DTO (toResponse)
List<Entity> -> List<Response DTO> (toResponseList)
```

MUST NOT put conversion logic in:

* Controllers
* Service business methods
* Mappers (MyBatis mapper interfaces)

Bad:

```java
// inside a controller or service method
AccountResponse response = new AccountResponse();
response.setName(account.getName());
response.setBalance(account.getBalance());
```

Good:

```java
AccountResponse response = accountConverter.toResponse(account);
```

## Business Object (BO)

BO is optional. Use ONLY when business logic needs an object that is not a persistence Entity.

Package: `com.matt.bo`

Naming:

| Purpose         | Pattern | Example             |
| --------------- | ------- | ------------------- |
| Business object | `*Bo`   | `OrderSettlementBo` |

Use BO when:

- business process spans multiple entities
- calculation requires intermediate state
- business object differs from database structure

BOs MUST NOT:
- be exposed through controllers
- be returned as API responses
- simply wrap a single Entity

Bad:

```java
public class AccountBo {
    private Account account; // just a wrapper
}
```

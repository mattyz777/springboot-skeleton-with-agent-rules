# API Validation

Read this when adding or changing request validation.

| If you are...                | Read                    | It covers                        |
| ---------------------------- | ----------------------- | -------------------------------- |
| endpoint style               | `api-endpoint-style.md` | URL and HTTP method rules        |
| request and response objects | `api-models.md`         | DTO naming and object boundaries |
| pagination                   | `api-pagination.md`     | Paging request and response      |

## Approach

Use Jakarta Bean Validation (JSR 380) annotations on Request DTO fields.
MUST NOT write manual validation logic in controllers or services when a standard constraint exists.

## Activating validation

Add `@Validated` on the controller class or `@Valid` on the request body parameter.

Example:

```java
@RestController
@RequestMapping("/accounts")
@Validated
public class AccountController {

    @PostMapping
    public ResponseDTO<Void> create(@RequestBody @Valid AccountCreateRequest request) {}
}
```

## Standard constraints

Use Jakarta Validation annotations. Common ones:

| Annotation       | Use for                                |
| ---------------- | -------------------------------------- |
| `@NotNull`       | field must not be null                 |
| `@NotBlank`      | String must not be null or empty/blank |
| `@NotEmpty`      | Collection/String must not be empty    |
| `@Size`          | String/Collection length range         |
| `@Min` / `@Max`  | Numeric range                          |
| `@Pattern`       | Regex match                            |
| `@Email`         | Email format                           |
| `@Positive`      | Number > 0                             |
| `@DecimalMin`    | BigDecimal minimum                     |
| `@Future`        | Date must be in the future             |
| `@Past`          | Date must be in the past               |

## Annotation placement

Put constraints directly on Request DTO fields.

Example:

```java
@Data
public class AccountCreateRequest {

    @NotBlank(message = "Account name is required")
    @Size(max = 100, message = "Account name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", message = "Balance must be non-negative")
    private BigDecimal balance;

    @NotNull(message = "Account type is required")
    private AccountTypeEnum type;
}
```

## Message conventions

Every constraint annotation MUST include a `message` attribute.
Messages MUST be human-readable English describing what is expected.

Bad:

```java
@NotBlank
private String name;
```

Good:

```java
@NotBlank(message = "Account name is required")
private String name;
```

## Nested validation

When a Request DTO contains a nested object that also needs validation, annotate it with `@Valid`.

```java
@Data
public class OrderCreateRequest {

    @NotBlank(message = "Order number is required")
    private String orderNo;

    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressRequest address;
}
```

## Collection element validation

To validate each element in a list, annotate the collection with `@Valid`.

```java
@NotEmpty(message = "At least one item is required")
@Valid
private List<OrderItemRequest> items;
```

## Custom constraint annotations

When standard annotations are not sufficient, create a custom constraint.

Annotation: placed in `com.matt.annotation`
Validator: placed in `com.matt.validator`

Naming:

| Kind       | Pattern          | Example          |
| ---------- | ---------------- | ---------------- |
| Annotation | descriptive name | `@Phone`         |
| Validator  | `*Validator`     | `PhoneValidator` |

### Custom annotation example

```java
package com.matt.annotation;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String message() default "Invalid phone number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### Validator implementation example

```java
package com.matt.validator;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // let @NotBlank handle null checks
        }
        return PHONE_PATTERN.matcher(value).matches();
    }
}
```

Usage on a DTO field:

```java
@Phone
private String phoneNumber;
```

## Custom validator null handling

Custom validators MUST return `true` for null values.
Use `@NotNull` or `@NotBlank` separately to reject nulls.
This follows Bean Validation convention and allows optional fields to skip custom validation when null.

## Path variable and query parameter validation

For simple path/query param validation, use constraint annotations directly on controller method parameters.
Requires `@Validated` on the controller class.

```java
@GetMapping("/{id}")
public ResponseDTO<AccountResponse> getAccount(
        @PathVariable @Positive(message = "ID must be positive") Long id) {}
```

## Validation error handling

Validation errors MUST be handled by `GlobalExceptionHandler` (in `com.matt.handler`).
The handler catches `MethodArgumentNotValidException` and `ConstraintViolationException` and returns a `ResponseDTO` with an appropriate error code and message.

MUST NOT catch or handle validation exceptions inside controllers.

## Rules summary

1. MUST use Bean Validation annotations on Request DTOs — no manual checks for standard constraints.
2. MUST include `message` on every constraint annotation.
3. MUST use `@Valid` / `@Validated` to activate validation.
4. MUST use `@Valid` for nested object and collection element validation.
5. MUST place custom annotations in `com.matt.annotation`, validators in `com.matt.validator`.
6. MUST NOT handle validation exceptions in controllers — `GlobalExceptionHandler` owns this.
7. MUST NOT validate in service layer what can be expressed as a Bean Validation constraint.

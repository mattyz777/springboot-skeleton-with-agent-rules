# Project Structure

## Layout

The filenames below are ILLUSTRATIVE EXAMPLES showing naming and placement.
Do NOT create them. Use them to decide where a new file belongs.

```text
src/main/java/com/matt/demo/
├── Application.java              # single @SpringBootApplication entry point
├── controller/                   # UserController.java
├── service/                      # UserService.java          (interface)
│   └── impl/                     # UserServiceImpl.java      (implementation)
├── mapper/                       # UserMapper.java
├── entity/                       # User.java                 (DB table mapping)
├── enums/                        # OrderStatusEnum.java
├── dto/
│   ├── request/                  # PagingRequest.java, UserCreateRequest.java, UserUpdateRequest.java
│   └── response/                 # PagingResponse.java, UserResponse.java, UserListResponse.java
├── bo/                           # OrderBo.java              (business object)
├── converter/                    # UserConverter.java
├── validator/                    # PhoneValidator.java       (impl of @Phone)
├── job/                          # OrderTimeoutJob.java      (scheduled tasks)
├── client/                       # OrderClient.java          (outbound HTTP)
├── cache/                        # TokenCache.java           (Redis access)
├── queue/                        # OrderProducer.java, OrderConsumer.java
├── config/                       # WebMvcConfig.java
├── handler/                      # GlobalExceptionHandler.java
├── interceptor/                  # DecryptInterceptor.java
├── filter/                       # XssFilter.java, TraceIdFilter.java
├── annotation/                   # Phone.java, DistributedLock.java
├── aspect/                       # DistributedLockAspect.java
├── constant/                     # Constant.java, ErrorCode.java
├── exception/                    # BusinessException.java
└── utils/                        # DatetimeUtil.java

src/main/resources/
├── mapper/                       # UserMapper.xml  (mirrors mapper/ package)
├── application.yml               # shared config; no environment secrets
├── application-dev.yml
├── application-prod.yml
└── logback-spring.xml

src/test/java/com/matt/demo/       # mirrors the main package tree
```

## Load these on demand
| If you are...                                     | Read                                  | It covers                                        |
|---------------------------------------------------|---------------------------------------|--------------------------------------------------|
| Creating a new file / unsure where it goes        | `docs/agents/project-structure.md`    | layout, naming, allowed dependencies             |
| Adding or changing an HTTP endpoint               | `docs/agents/api-conventions.md`      | URL style, response envelope, paging, validation |
| Touching entities, mappers, queries, DDL          | `docs/agents/persistence.md`          | MyBatis-Plus, transactions, paging queries       |
| Calling HTTP clients, Redis, or Kafka             | `docs/agents/integration.md`          | RestClient, Redisson, producers/consumers        |
| Writing a scheduled job or a Kafka consumer       | `docs/agents/jobs-and-async.md`       | scheduling, locking, idempotency                 |
| Adding beans, config, profiles, filters, aspects  | `docs/agents/spring-config.md`        | @Configuration, profiles, registration           |
| Writing any test                                  | `docs/agents/testing.md`              | test slices, naming, fixtures                    |
| Touching auth, roles, secrets, CORS               | `docs/agents/security.md`             | authn/authz, secret handling                     |
| Adding logs, metrics, traces                      | `docs/agents/observability.md`        | log format, traceId, metric naming               |

## Naming rules

| Kind              | Pattern                                        | Example                 |
|-------------------|------------------------------------------------|-------------------------|
| Controller        | `*Controller`                                  | `UserController`        |
| Service interface | `*Service`                                     | `UserService`           |
| Service impl      | `*ServiceImpl`                                 | `UserServiceImpl`       |
| Mapper            | `*Mapper`                                      | `UserMapper`            |
| Mapper XML        | `resources/mapper/*Mapper.xml`, same base name | `UserMapper.xml`        |
| Entity            | Singular noun, matches table                   | `User`                  |
| Enum              | `*Enum`                                        | `OrderStatusEnum`       |
| Request DTO       | `*Request` in `dto.request`                    | `UserCreateRequest`     |
| Response DTO      | `*Response` in `dto.response`                  | `UserResponse`          |
| Business object   | `*Bo`                                          | `OrderBo`               |
| Converter         | `*Converter`                                   | `UserConverter`         |
| Aspect            | `*Aspect`                                      | `DistributedLockAspect` |
| Scheduled job     | `*Job`                                         | `OrderTimeoutJob`       |
| Unit test         | `*Test`, mirrors package                       | `UserServiceImplTest`   |
| Integration test  | `*IT`, mirrors package                         | `UserControllerIT`      |


## Dependency direction

### Allowed calls:
```text
controller -> service
controller -> dto, enums

service -> service(other), mapper, client, cache, queue, converter
service -> dto, bo, entity, enums

mapper -> entity

job -> service
consumer -> service
aspect -> service

all layers -> common, enums
```

### Forbidden, no exceptions:

Entity objects must not leave the service layer.

```text
- controller -> entity
- controller -> bo
- mapper -> dto
- mapper -> service
- entity -> dto
```

## Object flow

### Default flow:
```text
request:   Request DTO -> Controller -> Service -> Converter -> Entity -> Mapper -> Database
response:  Database -> Mapper -> Entity -> Service -> Converter -> Response DTO
```

### Use BO when:
- business process spans multiple entities
- calculation requires intermediate state
- domain object is not a persistence entity

BO is optional, not mandatory.



# API Method Naming

Read this when naming a controller or service method.

| If you are...                | Read                    | It covers                        |
| ---------------------------- | ----------------------- | -------------------------------- |
| endpoint URLs and methods    | `api-endpoint-style.md` | URL and HTTP method rules        |
| request and response objects | `api-models.md`         | DTO naming, response envelope    |
| pagination                   | `api-pagination.md`     | Paging request and response      |

## Method name prefixes

Method names MUST start with one of the standard prefixes below.
The prefix declares the operation and implies the return-value shape.
Do NOT invent new prefixes for these operations.

| Prefix   | Meaning                                   | Return value                | Example          |
| -------- | ----------------------------------------- | --------------------------- | ---------------- |
| `get`    | Fetch a single record                     | single object               | `getUserById()`  |
| `search` | Fetch a collection (paginated)            | paging object               | `searchUsers()`  |
| `create` | Create a record                           | new record id               | `createUser()`   |
| `update` | Edit a record                             | none                        | `updateUser()`   |
| `delete` | Delete a record                           | none                        | `deleteUser()`   |
| `export` | Export data to a file or storage          | none                        | `exportUsers()`  |
| `import` | Import data                               | none                        | `importUsers()`  |
| `upload` | Upload data                               | none                        | `uploadImage()`  |

## Return types

The prefix maps to a concrete response type. All controller methods are still
wrapped in `ResponseDTO<T>` (see `api-models.md`).

| Prefix   | Controller return type                  |
| -------- | --------------------------------------- |
| `get`    | `ResponseDTO<XxxResponse>`              |
| `search` | `ResponseDTO<PagingResponse<XxxResponse>>` |
| `create` | `ResponseDTO<Long>` (the new id)        |
| `update` | `ResponseDTO<Void>`                     |
| `delete` | `ResponseDTO<Void>`                     |
| `export` | `ResponseDTO<Void>`                     |
| `import` | `ResponseDTO<Void>`                     |
| `upload` | `ResponseDTO<Void>`                     |

`export` writes the data to a file or other storage destination. It MUST NOT
return the exported data to the client, so its return type is `ResponseDTO<Void>`.

## Alignment with HTTP methods

Method-name prefixes correspond to the HTTP method rules in `api-endpoint-style.md`.

| Prefix   | HTTP method | Notes                                            |
| -------- | ----------- | ------------------------------------------------ |
| `get`    | GET         | Single resource by id                            |
| `search` | POST        | Uses a `/search` action; criteria in the body    |
| `create` | POST        | Create a resource                                |
| `update` | PUT / PATCH | Full replace (PUT) or partial update (PATCH)     |
| `delete` | DELETE      | Remove a resource                                |
| `export` | POST        | Writes to a file or storage; no data returned    |
| `import` | POST        | Sub-resource action                              |
| `upload` | POST        | Sub-resource action                              |

`search` returns a paginated collection. A search MUST use POST even when it
returns only a single record (see `api-endpoint-style.md`).

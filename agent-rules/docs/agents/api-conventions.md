# API Conventions

Read this when adding or changing an HTTP endpoint.

For file placement and naming:
read `project-structure.md`.

For database access:
read `persistence.md`.

For business logic:
follow service layer conventions.


## Load these on demand

| If you are...                       | Read                                | It covers                                         |
|-------------------------------------|-------------------------------------|---------------------------------------------------|
| Designing a new endpoint URL        | `docs/agents/api-endpoint-style.md` | REST resources, HTTP methods, action endpoints    |
| Defining request or response models | `docs/agents/api-models.md`         | Request DTO, Response DTO, response envelope      |
| Adding request validation           | `docs/agents/api-validation.md`     | Bean Validation, custom validators                |
| Adding pagination or list endpoints | `docs/agents/api-pagination.md`     | paging parameters, page response format           |

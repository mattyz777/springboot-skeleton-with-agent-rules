# AGENTS.md

## Project

Spring Boot 3.5 / Java 21 service.
Base package: `com.matt.<business>`.
Single-module Gradle.

Commands:
- Build: `./gradlew build`
- Test: `./gradlew test`


## Rules

- Read relevant docs BEFORE writing code.
- If a required doc is missing, say so.
- Do not invent project conventions.
- If a routed doc has status `planned` (does not yet exist), proceed using
  only `project-structure.md` and the non-negotiable rules. Do not halt.
- When a task comes from a spec at `docs/jira_task/<jira_task_id>/spec.md`,
  work through the tasks in its `Tasks` section one at a time. After
  completing each individual task, STOP: list what you did as bullet points
  and ask the human to verify before starting the next task. Do not proceed
  to the next task until the human confirms.
  - Directly under the bullet list, auto-generate a commit message in
    Conventional Commits format `type(scope): message` describing what was
    done. Use it only after the human verifies; do not commit automatically.


## Non-negotiable rules

- Controllers do not access repositories.
- Database access happens only in repositories, through services.
- Entities are never exposed through HTTP APIs.
- Business logic belongs in services.
- Follow existing conventions before introducing new patterns.


## Documentation routing

| Task                                    | Read                               | Status  |
|-----------------------------------------|------------------------------------|---------|
| Create or move files                    | docs/agents/project-structure.md   | done    |
| Add/change HTTP endpoint (general)      | docs/agents/api-conventions.md     | done    |
| Design endpoint URLs or methods         | docs/agents/api-endpoint-style.md  | done    |
| Define request/response DTOs            | docs/agents/api-models.md          | done    |
| Add request validation                  | docs/agents/api-validation.md      | done    |
| Add pagination to a list endpoint       | docs/agents/api-pagination.md      | done    |
| Name a controller/service method        | docs/agents/api-method-naming.md   | done    |
| Touch entities, mappers, queries, DDL   | docs/agents/persistence.md         | planned |
| Call HTTP clients, Redis, Kafka         | docs/agents/integration.md         | planned |
| Add jobs or async processing            | docs/agents/jobs-and-async.md      | planned |
| Add configuration / beans / profiles    | docs/agents/spring-config.md       | planned |
| Write tests                             | docs/agents/testing.md             | planned |
| Touch authentication/security           | docs/agents/security.md            | planned |
| Add logs/metrics/tracing                | docs/agents/observability.md       | planned |
| Any other task                          | docs/agents/project-structure.md   | done    |

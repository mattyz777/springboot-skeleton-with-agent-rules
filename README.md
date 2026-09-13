# springboot-skeleton-with-agent-rules

A Spring Boot 3 project skeleton with agent rules for AI-assisted development. Use the included init script to scaffold new projects and optionally add agent rules.

## Quick Start

### Prerequisites

- Python 3.9+
- Java 21
- Gradle (optional — the skeleton ships with the Gradle wrapper `./gradlew`)

### Usage

```bash
python init-project.py <package_name> <target_path>
```

The script works on both **Windows** and **macOS/Linux**.

### Create a New Project

Scaffolds a new Spring Boot 3 project from the skeleton template **and** adds the
agent rules for AI-assisted development — in a single command.

```bash
# Windows
python init-project.py com.company.orderservice C:\code\projects\order-service

# macOS / Linux
python init-project.py com.company.orderservice /Users/dev/projects/order-service
```

| Parameter      | Description                                            | Example                    |
|----------------|--------------------------------------------------------|----------------------------|
| `package_name` | Java package name (also used as the Gradle `group`)    | `com.company.orderservice` |
| `target_path`  | Full path for the new project (must not already exist) | `C:\code\my-app`           |

What it does:

1. Copies the `springboot3-skeleton` template to the target path (creates parent directories if needed)
2. Updates `build.gradle` (`group`) and `settings.gradle` (`rootProject.name`, set to the folder name)
3. Renames the Java package directory structure to match the new package
4. Replaces all `package` and `import` declarations in `.java` files
5. Copies `AGENTS.md` and `CLAUDE.md` (for Claude Code compatibility) to the project root
6. Copies the `docs/agents/` directory (coding conventions and style guides) into the project
7. Updates package references in all agent files from `com.matt` to your package name

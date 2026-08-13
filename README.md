# springboot-skeleton-with-agent-rules

A Spring Boot 3 project skeleton with agent rules for AI-assisted development. Use the included init script to scaffold new projects and optionally add agent rules.

## Quick Start

### Prerequisites

- Python 3.9+
- Java 21
- Maven

### Usage

```bash
python init-project.py -i <package_name> <target_path>
python init-project.py -a <target_path>
```

The script works on both **Windows** and **macOS/Linux**.

### Init a New Project (`-i`)

Scaffolds a new Spring Boot 3 project from the skeleton template.

```bash
# Windows
python init-project.py -i com.company.orderservice C:\code\projects\order-service

# macOS / Linux
python init-project.py -i com.company.orderservice /Users/dev/projects/order-service
```

What it does:

1. Copies the `springboot3-skeleton` template to the target path (creates parent directories if needed)
2. Updates `pom.xml` with the new `groupId` and `artifactId` (folder name)
3. Renames the Java package directory structure to match the new package
4. Replaces all `package` and `import` declarations in `.java` files

| Parameter | Description | Example |
|-----------|-------------|---------|
| `package_name` | Java package name (lowercase, dot-separated, at least two segments) | `com.company.app` |
| `target_path` | Full path for the new project (must not already exist) | `C:\code\my-app` |

### Add Agent Rules (`-a`)

Copies `AGENTS.md` and `docs/agents/` to an existing project for AI-assisted development.

```bash
python init-project.py -a -p com.company.orderservice C:\code\projects\order-service
```

What it does:

1. Copies `AGENTS.md` to the target project root
2. Copies `CLAUDE.md` to the target project root (for Claude Code compatibility)
3. Copies `docs/agents/` directory (coding conventions and style guides) to the target project
4. Updates package references in all agent files from `com.matt` to your package name

The target project must already exist.

### Full Workflow Example

```bash
# Step 1: Create the project
python init-project.py -i com.company.orderservice C:\code\projects\order-service

# Step 2: Add agent rules
python init-project.py -a -p com.company.orderservice C:\code\projects\order-service
```

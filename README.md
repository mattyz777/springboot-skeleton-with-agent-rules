# springboot-skeleton-with-agent-rules

A Spring Boot 3 project skeleton with agent rules for AI-assisted development. Use the included init script to scaffold new projects with customized package names and project structure.

## Quick Start

### Prerequisites

- Python 3.9+
- Java 21
- Maven 3.x

### Create a New Project

```bash
python init-project.py <package_name> <target_path>
```

The script works on both **Windows** and **macOS/Linux**.

### Examples

```bash
# Windows
python  init-project.py  com.company.orderservice  C:\code\projects\order-service

# macOS / Linux
python  init-project.py  com.company.orderservice  /Users/dev/projects/order-service
```

### What It Does

1. Copies the `springboot3-skeleton` template to the target path (creates parent directories if they don't exist)
2. Updates `pom.xml` with the new `groupId` and `artifactId` (folder name)
3. Renames the Java package directory structure to match the new package
4. Replaces all `package` and `import` declarations in `.java` files

### Parameters

| Parameter | Description | Example |
|-----------|-------------|---------|
| `package_name` | Java package name (lowercase, dot-separated, at least two segments) | `com.company.app` |
| `target_path` | Full path for the new project (must not already exist) | `C:\code\my-app` |








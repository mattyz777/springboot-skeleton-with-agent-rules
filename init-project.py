#!/usr/bin/env python3
"""
Scaffold a new Spring Boot 3 project and/or copy agent rules.

Usage:
    python init-project.py -i <package_name> <target_path>
    python init-project.py -a <target_path>

Options:
    -i  Init a new project from skeleton template
    -a  Copy AGENTS.md and agent-rules docs to target project

Examples:
    # Create a new project
    python init-project.py -i com.company.orderservice C:/code/projects/order-service

    # Add agent rules to an existing project
    python init-project.py -a C:/code/projects/order-service

    # Both (init then add agent rules)
    python init-project.py -i com.company.orderservice C:/code/projects/order-service
    python init-project.py -a C:/code/projects/order-service
"""

import sys
import os
import shutil
import re
from pathlib import Path

# --- Configuration ---
SKELETON_DIR = "springboot3-skeleton"
AGENT_RULES_DIR = "agent-rules"
TEMPLATE_GROUP_ID = "com.matt"
TEMPLATE_PACKAGE_PATH = "com/matt"

# Directories to skip when copying
SKIP_DIRS = {".idea", "target", ".git"}


def validate_package_name(package_name: str) -> bool:
    """Validate Java package name format."""
    return bool(re.match(r"^[a-z][a-z0-9]*(\.[a-z][a-z0-9]*)+$", package_name))


def copy_skeleton(skeleton_path: Path, target_path: Path) -> None:
    """Copy skeleton to target, skipping .idea/target/.git."""
    def ignore(directory, contents):
        return [c for c in contents if c in SKIP_DIRS]

    shutil.copytree(skeleton_path, target_path, ignore=ignore)
    print(f"  Copied skeleton to {target_path}")


def replace_in_file(file_path: Path, replacements: list[tuple[str, str]]) -> None:
    """Apply text replacements in a file."""
    content = file_path.read_text(encoding="utf-8")
    original = content
    for old, new in replacements:
        content = content.replace(old, new)
    if content != original:
        file_path.write_text(content, encoding="utf-8")


def update_pom(target_path: Path, group_id: str, artifact_id: str) -> None:
    """Update groupId and artifactId in pom.xml."""
    pom = target_path / "pom.xml"
    replacements = [
        (f"<groupId>{TEMPLATE_GROUP_ID}</groupId>", f"<groupId>{group_id}</groupId>"),
        (f"<artifactId>{SKELETON_DIR}</artifactId>", f"<artifactId>{artifact_id}</artifactId>"),
    ]
    replace_in_file(pom, replacements)
    print(f"  Updated pom.xml: groupId={group_id}, artifactId={artifact_id}")


def rename_package_dirs(target_path: Path, new_package_path: str) -> None:
    """Rename com/matt directory structure to match new package."""
    for root_dir in ["src/main/java", "src/test/java"]:
        old_pkg_dir = target_path / root_dir / TEMPLATE_PACKAGE_PATH
        new_pkg_dir = target_path / root_dir / new_package_path

        if old_pkg_dir.exists():
            new_pkg_dir.parent.mkdir(parents=True, exist_ok=True)
            shutil.move(str(old_pkg_dir), str(new_pkg_dir))
            print(f"  Renamed package dir: {old_pkg_dir.relative_to(target_path)} -> {new_pkg_dir.relative_to(target_path)}")

            # Clean up empty parent dirs
            parent = old_pkg_dir.parent
            while parent != target_path / root_dir:
                if parent.exists() and not any(parent.iterdir()):
                    parent.rmdir()
                parent = parent.parent


def update_java_files(target_path: Path, old_package: str, new_package: str) -> None:
    """Update package declarations and imports in all .java files."""
    replacements = [
        (f"package {old_package}", f"package {new_package}"),
        (f"import {old_package}", f"import {new_package}"),
    ]

    count = 0
    for java_file in target_path.rglob("*.java"):
        replace_in_file(java_file, replacements)
        count += 1

    print(f"  Updated package/import in {count} Java files")


def init_project(package_name: str, target_path: Path, script_dir: Path) -> None:
    """Init a new project from skeleton template."""
    # Validate package name
    if not validate_package_name(package_name):
        print(f"Error: Invalid package name '{package_name}'")
        print("  Must be like: com.aaa.bbb (lowercase, at least two segments)")
        sys.exit(1)

    # Derive values
    group_id = package_name
    artifact_id = target_path.name
    new_package_path = package_name.replace(".", "/")

    # Skeleton source
    skeleton_path = script_dir / SKELETON_DIR

    if not skeleton_path.exists():
        print(f"Error: Skeleton directory not found at {skeleton_path}")
        sys.exit(1)

    if target_path.exists():
        print(f"Error: Target path already exists: {target_path}")
        sys.exit(1)

    print(f"\nScaffolding new project:")
    print(f"  Target path:   {target_path}")
    print(f"  Artifact ID:   {artifact_id}")
    print(f"  Package:       {package_name}")
    print()

    # Create parent dirs if needed
    target_path.parent.mkdir(parents=True, exist_ok=True)

    # Execute
    copy_skeleton(skeleton_path, target_path)
    update_pom(target_path, group_id, artifact_id)
    rename_package_dirs(target_path, new_package_path)
    update_java_files(target_path, TEMPLATE_GROUP_ID, package_name)

    print(f"\n[OK] Project '{artifact_id}' created at {target_path}")


def add_agent_rules(target_path: Path, script_dir: Path) -> None:
    """Copy AGENTS.md and agent-rules docs to the target project root."""
    agent_rules_src = script_dir / AGENT_RULES_DIR

    if not agent_rules_src.exists():
        print(f"Error: Agent rules directory not found at {agent_rules_src}")
        sys.exit(1)

    if not target_path.exists():
        print(f"Error: Target path does not exist: {target_path}")
        sys.exit(1)

    print(f"\nAdding agent rules to: {target_path}")

    # Copy AGENTS.md to project root
    agents_md_src = agent_rules_src / "AGENTS.md"
    agents_md_dst = target_path / "AGENTS.md"
    shutil.copy2(str(agents_md_src), str(agents_md_dst))
    print(f"  Copied AGENTS.md")

    # Copy docs/agents/ directory
    docs_src = agent_rules_src / "docs" / "agents"
    docs_dst = target_path / "docs" / "agents"
    if docs_dst.exists():
        shutil.rmtree(str(docs_dst))
    docs_dst.parent.mkdir(parents=True, exist_ok=True)
    shutil.copytree(str(docs_src), str(docs_dst))
    print(f"  Copied docs/agents/ ({len(list(docs_dst.iterdir()))} files)")

    print(f"\n[OK] Agent rules added to {target_path}")


def print_usage():
    print("Usage:")
    print("  python init-project.py -i <package_name> <target_path>")
    print("  python init-project.py -a <target_path>")
    print()
    print("Options:")
    print("  -i  Init a new project from skeleton template")
    print("  -a  Copy AGENTS.md and agent-rules docs to target project")
    print()
    print("Examples:")
    print("  python init-project.py -i com.company.app C:/code/projects/my-app")
    print("  python init-project.py -a C:/code/projects/my-app")


def main():
    if len(sys.argv) < 2:
        print_usage()
        sys.exit(1)

    script_dir = Path(__file__).resolve().parent
    mode = sys.argv[1]

    if mode == "-i":
        if len(sys.argv) != 4:
            print("Usage: python init-project.py -i <package_name> <target_path>")
            sys.exit(1)
        package_name = sys.argv[2]
        target_path = Path(sys.argv[3]).resolve()
        init_project(package_name, target_path, script_dir)

    elif mode == "-a":
        if len(sys.argv) != 3:
            print("Usage: python init-project.py -a <target_path>")
            sys.exit(1)
        target_path = Path(sys.argv[2]).resolve()
        add_agent_rules(target_path, script_dir)

    else:
        print(f"Error: Unknown option '{mode}'")
        print()
        print_usage()
        sys.exit(1)


if __name__ == "__main__":
    main()

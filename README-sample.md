# Sample Project

## prerequisite
Make sure <target_full_path> does NOT exist.

## initialization
```bash
# init
python init-project.py -i com.abc <target_full_path>
# copy agent related md files and update package name as expected
python init-project.py -a -p com.abc <target_full_path>
```

## install skill
```bash
cd <target_full_path>
npx skills add mattpocock/skills --skill=grill-with-docs
```

## copy function specification
copy <repo>/Samples/springboot3-func-spec.md into <target_full_path>/docs/tasks/<Jira_ID>/

## opencode
start opencode -> skills -> grill-with-docs
```
/grill-with-docs

Read and understand:
docs/tasks/JIRA_001/springboot3-func-spec.md

Use the project context and agent rules to review the specification with me.
Do not implement anything yet. Ask questions, identify ambiguities, missing requirements, and potential issues, then discuss them with me.
```




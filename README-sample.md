# Sample Project

## prerequisite
Make sure <target_full_path> does NOT exist.

## initialization
```bash
# init
python init-project.py -i com.abc <target_full_path>
# copy agent related md files
python init-project.py -a <target_full_path>
```

## install skill
```bash
cd <target_full_path>
npx skills add mattpocock/skills --skill=grill-with-docs
```

## function specification
@/Samples/springboot3-func-spec.md



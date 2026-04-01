#!/usr/bin/env python3
"""
Find TODO/FIXME/XXX/HACK comments in Java source files and create a GitHub
issue for each one that does not already have a corresponding open issue.

Requirements:
  - GitHub CLI (gh) installed and authenticated via GH_TOKEN
  - Run from the root of the repository
"""

import json
import re
import subprocess
import sys
from pathlib import Path

# Matches a TODO/FIXME/HACK/XXX tag that immediately follows a comment marker.
# Pattern: comment opener (// or /*), optional whitespace, then the tag keyword,
# then whitespace, colon, or end-of-string.
# This deliberately excludes identifiers like "isXXX" in section headers.
COMMENT_TAG_RE = re.compile(
    r'(?://|/\*)\s*(TODO|FIXME|HACK|XXX+)(\s|:|$)',
    re.IGNORECASE,
)

LABEL = 'todo'
LABEL_COLOR = 'e4e669'
LABEL_DESCRIPTION = 'Tracked TODO/FIXME/XXX comment from source code'


def run_gh(*args):
    return subprocess.run(['gh'] + list(args), capture_output=True, text=True)


def ensure_label():
    r = run_gh(
        'label', 'create', LABEL,
        '--description', LABEL_DESCRIPTION,
        '--color', LABEL_COLOR,
    )
    if r.returncode == 0:
        print(f"Label '{LABEL}' created.")
    else:
        print(f"Label '{LABEL}' already exists (or could not be created).")


def get_existing_titles():
    """Return the set of titles of all open issues labelled 'todo'."""
    # 1000 is well above the number of TODO comments any typical project
    # accumulates; increase further if the repository grows significantly.
    r = run_gh(
        'issue', 'list',
        '--label', LABEL,
        '--state', 'open',
        '--json', 'title',
        '--limit', '1000',
    )
    if r.returncode != 0:
        print(
            f"Warning: could not list existing issues: {r.stderr}",
            file=sys.stderr,
        )
        return set()
    return {issue['title'] for issue in json.loads(r.stdout or '[]')}


def find_todos(root, context_lines=5):
    """
    Walk all Java files under *root* and return a list of
    (relative_path, line_number, stripped_line, tag, context) for every line
    that contains a TODO/FIXME/HACK/XXX comment tag.

    File contents are read once per file; *context* is the snippet of up to
    *context_lines* source lines starting at the matching line.
    """
    todos = []
    for java_file in sorted(root.rglob('*.java')):
        rel = str(java_file.relative_to(root))
        try:
            lines = java_file.read_text(encoding='utf-8', errors='replace').splitlines()
        except OSError:
            continue
        for line_num, line in enumerate(lines, start=1):
            m = COMMENT_TAG_RE.search(line)
            if not m:
                continue
            raw_tag = m.group(1).upper()
            # Normalise XXX / XXXX variants to XXX
            tag = 'XXX' if raw_tag.startswith('XXX') else raw_tag
            start = line_num - 1
            end = min(len(lines), start + context_lines)
            context = '\n'.join(lines[start:end])
            todos.append((rel, line_num, line.strip(), tag, context))
    return todos


def create_issue(title, body):
    r = run_gh('issue', 'create', '--title', title, '--body', body, '--label', LABEL)
    if r.returncode == 0:
        print(f"  Created: {r.stdout.strip()}")
        return True
    print(f"  ERROR creating issue: {r.stderr.strip()}", file=sys.stderr)
    return False


def main():
    root = Path.cwd()

    ensure_label()

    existing = get_existing_titles()
    print(f"Existing '{LABEL}' issues: {len(existing)}")

    todos = find_todos(root)
    print(f"TODO/FIXME/XXX/HACK comments found: {len(todos)}")

    created = skipped = 0
    for file_path, line_num, line_text, tag, context in todos:
        title = f'{tag}: {file_path} line {line_num}'

        if title in existing:
            print(f"Skip (already exists): {title}")
            skipped += 1
            continue

        body = (
            f'## `{tag}` comment in source code\n\n'
            f'**File:** `{file_path}`  \n'
            f'**Line:** {line_num}\n\n'
            f'### Source context\n\n'
            f'```java\n{context}\n```\n\n'
            f'*This issue was automatically created from a `{tag}` comment '
            f'in the source code.*'
        )

        print(f"Creating: {title}")
        if create_issue(title, body):
            existing.add(title)
            created += 1

    print(f'\nDone: {created} created, {skipped} skipped.')


if __name__ == '__main__':
    main()

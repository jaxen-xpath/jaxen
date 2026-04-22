import re

keywords_re = re.compile(r'\$(Header|Revision|Date|Id)[:$]?.*\$')
stripped = "$Header$"
print(f"Match '$Header$': {keywords_re.fullmatch(stripped)}")

line = " * $Header$"
stripped = line.strip()
if stripped.startswith('*'):
    stripped = stripped[1:].strip()
print(f"Stripped: '{stripped}'")
print(f"Match stripped: {keywords_re.fullmatch(stripped)}")

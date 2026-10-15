"""Release notes for the GitHub release: one row per jar with its loader and the Minecraft versions it declares.

Usage: python3 .github/release-notes.py <folder with the release jars>
Reads the declared ranges from versions/<minecraft>/gradle.properties.
"""
import os, re, sys


def version_key(v):
    return [int(p) for p in v.split(".")]


def properties(path):
    values = {}
    with open(path, encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if line and not line.startswith("#") and "=" in line:
                key, value = line.split("=", 1)
                values[key.strip()] = value.strip()
    return values


folder = sys.argv[1]
rows = []
for name in os.listdir(folder):
    m = re.match(r"^(.+)-(fabric|neoforge)-(.+)\+(\d[\d.]*)\.jar$", name)
    if not m:
        continue
    _, loader, _, minecraft = m.groups()
    props = properties(os.path.join("versions", minecraft, "gradle.properties"))
    declared = props["minecraft_range_fabric"] if loader == "fabric" else props["minecraft_range_neoforge"]
    rows.append((version_key(minecraft), loader, minecraft, declared, props["java_version"], name))
rows.sort()

sha = os.environ.get("GITHUB_SHA", "")[:7]
print(f"One jar per loader and Minecraft version{f', all built from {sha}' if sha else ''}. "
      "Pick the jar for your loader whose range contains your Minecraft version.")
print()
print("| Loader | Built for | Declared Minecraft versions | Java | Jar |")
print("|---|---|---|---|---|")
for _, loader, minecraft, declared, java, name in rows:
    loader_name = "Fabric" if loader == "fabric" else "NeoForge"
    print(f"| {loader_name} | {minecraft} | `{declared}` | {java} | `{name}` |")
print()
print("Fabric jars also need Fabric API; both loaders need Architectury API for the same Minecraft version.")

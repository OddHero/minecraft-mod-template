# Example Mod (universal)

One source tree for every Minecraft version from **1.21 to 26.3** on Fabric and NeoForge (Architectury), built with [Stonecutter](https://stonecutter.kikugie.dev/) 0.9.8 and Architectury Loom 1.17.

## Versions

Each node builds against one Minecraft version and its jars declare the whole group of versions that share an Architectury line:

| Node | Jars declare | Architectury | Java | Loom mode |
|---|---|---|---|---|
| 1.21.1 | 1.21, 1.21.1 | 13.x | 21 | remapping (Mojang mappings) |
| 1.21.3 | 1.21.2, 1.21.3 | 14.x | 21 | remapping |
| 1.21.4 | 1.21.4 | 15.x | 21 | remapping |
| 1.21.5 | 1.21.5 | 16.x | 21 | remapping |
| 1.21.8 | 1.21.6 to 1.21.8 | 17.x | 21 | remapping |
| 1.21.10 | 1.21.9, 1.21.10 | 18.x | 21 | remapping |
| 1.21.11 | 1.21.11 | 19.x | 21 | remapping |
| 26.1.2 | 26.1.2 | 20.x | 25 | no remapping (unobfuscated) |
| 26.2 | 26.2 | 21.x | 25 | no remapping |
| 26.3 | 26.3 | 22.x | 25 | no remapping |

26.1 and 26.1.1 are not covered: Architectury 20.x requires 26.1.2. The declared ranges are `minecraft_range_fabric`, `minecraft_range_neoforge` and `neoforge_range` in `versions/<mc>/gradle.properties`; each node is built and tested on its own version.

## Layout

| Path | Holds |
|---|---|
| `common/src`, `fabric/src`, `neoforge/src` | The only sources you edit. They are shared by all versions. |
| `versions/<mc>/gradle.properties` | Per-version Minecraft, Java, Architectury, Fabric API and NeoForge versions, and the declared ranges. |
| `neoforge/versions/<mc>/gradle.properties` | `loom.platform=neoforge` for each NeoForge node. |
| `gradle/shared.gradle` | Setup shared by every node: Loom mode, mappings, Java, jar names, release jar, runs, publishing. |
| `stonecutter.gradle` | Stonecutter controller: active version, swaps, `collectJars`. |
| `settings.gradle` | The version list and the `common`/`fabric`/`neoforge` branches. |

Every loader × version pair is its own Gradle project (a node), for example `:fabric:1.21.4`. The release jars are `<loader>/versions/<mc>/build/libs/examplemod-<loader>-<mod_version>+<mc>.jar`.

## Everyday commands

```
./gradlew build                          # builds all 10 versions x 2 loaders
./gradlew collectJars                    # the same, plus all 20 release jars in build/libs/
./gradlew :fabric:1.21.4:build           # a single node
./gradlew :neoforge:1.21.1:runClient     # run one version (IntelliJ: "Client:neoforge:1.21.1")
./gradlew "Set active project to 1.21.4" # switch the version src/ is written for
./gradlew "Reset active project"         # back to 26.2; run this before every commit
```

The first build downloads every Minecraft version, its mappings and two JDKs (21 and 25) and takes several minutes. Later builds are incremental.

## Obfuscated and unobfuscated versions

Minecraft up to 1.21.11 is obfuscated, 26.1 and later is not. `gradle/shared.gradle` picks the Loom plugin per node:

- **Up to 1.21.11:** `dev.architectury.loom` with `mappings loom.officialMojangMappings()`. Mod dependencies use `modImplementation`, the loaders take `common` through its `namedElements` configuration, and the release jar is the shadow jar remapped by `remapJar`. Fabric jars end up with intermediary names, NeoForge jars with Mojang names.
- **From 26.1:** `dev.architectury.loom-no-remap`. No mappings, plain `implementation`, and the shadow jar is the release jar.

The build scripts use `project.modImplementation` (resolves to the right configuration name), `commonNamed(...)` and the `releaseJar` task, so they work for both.

## Version-specific code

`src/` always holds the code of the **active** version (`stonecutter.active` in `stonecutter.gradle`). The IDE sees only that version. The other versions compile from copies that Stonecutter generates under `*/versions/<mc>/build/generated/stonecutter/`. Never edit those copies.

```java
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*///?}

public static final String MINECRAFT_VERSION = /*$ minecraft*/ "26.2";  // swap, defined in stonecutter.gradle
```

- Version comparisons work across the numbering change: 1.21.11 < 26.1.
- Switch to a version before you write code for it, so its branch is the uncommented one and the IDE checks it.
- Conditions work in `.java`, `.gradle`, `.json5`, `.yml` and access widener files, not in `.json` or `.toml`. The mod metadata uses `${...}` placeholders filled by `processResources` instead.

## Adding a Minecraft version

1. Add it to `versions` in `settings.gradle`.
2. Create `versions/<mc>/gradle.properties` (copy a neighbour and update every value) and `neoforge/versions/<mc>/gradle.properties` with `loom.platform=neoforge`.
3. Run `./gradlew build` and fix compile errors with conditions.

## Renaming for a new mod

Rename `rootProject.name` (`template`), `maven_group`/`archives_name`/`mod_version` in `gradle.properties`, the `com.example.examplemod` packages, `MOD_ID`, `fabric.mod.json` and `META-INF/neoforge.mods.toml`.

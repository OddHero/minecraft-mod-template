# Example Mod (multi-version)

One source tree for Minecraft **26.1.2, 26.2 and 26.3** on Fabric and NeoForge (Architectury), built with [Stonecutter](https://stonecutter.kikugie.dev/) 0.9.8.

## Layout

| Path | Holds |
|---|---|
| `common/src`, `fabric/src`, `neoforge/src` | The only sources you edit. They are shared by all versions. |
| `versions/<mc>/gradle.properties` | Per-version dependency versions (Minecraft, Architectury, Fabric API, NeoForge). |
| `neoforge/versions/<mc>/gradle.properties` | `loom.platform=neoforge` for each NeoForge node. |
| `gradle/shared.gradle` | Setup shared by every node (Loom, Java, jar names, runs, publishing). |
| `stonecutter.gradle` | Stonecutter controller: active version, swaps, `collectJars`. |
| `settings.gradle` | The version list and the `common`/`fabric`/`neoforge` branches. |

Every loader × version pair is its own Gradle project (a node), for example `:fabric:26.3`. The jars are `<loader>/versions/<mc>/build/libs/examplemod-<loader>-<mod_version>+<mc>.jar`.

## Everyday commands

```
./gradlew build                          # builds all 3 versions x 2 loaders
./gradlew collectJars                    # the same, plus all 6 release jars in build/libs/
./gradlew :fabric:26.3:build             # a single node
./gradlew :neoforge:26.1.2:runClient     # run one version (IntelliJ: "Client:neoforge:26.1.2")
./gradlew "Set active project to 26.3"   # switch the version src/ is written for
./gradlew "Reset active project"         # back to 26.2; run this before every commit
```

## Version-specific code

`src/` always holds the code of the **active** version (`stonecutter.active` in `stonecutter.gradle`). The IDE sees only that version. The other versions compile from copies that Stonecutter generates under `*/versions/<mc>/build/generated/stonecutter/`. Never edit those copies.

```java
//? if >=26.2 {
newApiCall();
//?} else {
/*oldApiCall();
*///?}

public static final String MINECRAFT_VERSION = /*$ minecraft*/ "26.2";  // swap, defined in stonecutter.gradle
```

- Switch to a version before you write code for it, so its branch is the uncommented one and the IDE checks it.
- Conditions work in `.java`, `.gradle`, `.json5`, `.yml` and access widener files, not in `.json` or `.toml`. The mod metadata uses `${...}` placeholders filled by `processResources` instead.

## Adding a Minecraft version

1. Add it to `versions` in `settings.gradle`.
2. Create `versions/<mc>/gradle.properties` with the four dependency versions, and `neoforge/versions/<mc>/gradle.properties` with `loom.platform=neoforge`.
3. Run `./gradlew build` and fix compile errors with conditions.

## Renaming for a new mod

Rename `rootProject.name` (`template`), `maven_group`/`archives_name`/`mod_version` in `gradle.properties`, the `com.example.examplemod` packages, `MOD_ID`, `fabric.mod.json` and `META-INF/neoforge.mods.toml`.

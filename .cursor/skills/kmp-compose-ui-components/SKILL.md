---
name: kmp-compose-ui-components
description: Generate Kotlin Compose Multiplatform (KMP) UI components with strict conventions—`components` package, `Component` naming/suffix, one file per component, internal `@Preview` variants (default/loading/error), `@Immutable` state models, no collection parameters (use `ImmutableList`/`ImmutableMap` inside `@Immutable` wrappers), optional private single-file sub-composables, and Paparazzi snapshot tests when the module applies `app.cash.paparazzi` or lists a paparazzi dependency. Use when the user asks to scaffold, add, or generate Compose UI components, preview blocks, or screenshot tests for KMP modules; when enforcing component extraction rules; or when wiring Paparazzi for new composables.
---

# KMP Compose UI components

## Workflow

1. **Resolve target module and paths** from the user request or open files.
2. **Infer imports** from existing composables in the same module (prefer `androidx.compose.*` vs
   `org.jetbrains.compose.*` to match the codebase).
3. **Emit** the component `.kt` file(s) per rules below.
4. **Paparazzi:** If the module’s `build.gradle.kts` or `build.gradle` contains the substring
   `paparazzi` (plugin id `app.cash.paparazzi` and/or dependency), also emit
   `<ComponentName>Test.kt` in the correct test source set.

## Package layout

- Every standalone component lives under a **`components` package** (e.g.
  `com.example.app.feature.screen.components` or `com.example.app.components`).
- **This repo:** Prefer screen-local vs feature-wide `components` per
  `.cursor/rules/compose-component-architecture.mdc`; the skill still requires a `components`
  segment in the path.

## Naming

- **File:** `<Name>Component.kt` (PascalCase, ends with `Component`).
- **Public composable:** `fun <Name>Component(...)` — name ends with `Component`.
- **Preview functions:** `<Name>ComponentPreview`, `<Name>ComponentPreviewLoading`, etc. (describe
  state in the name).

## One component per file

- The primary `Component` composable and its **state/data classes** for that component may live in
  the same file.
- **Do not** put multiple unrelated standalone components in one file.

## Previews

- Add **at least one** `@Preview` per standalone component; prefer **multiple** previews for
  meaningful states (default, loading, error, empty, dark theme if themed).
- Annotate previews with `@Preview` and `@Composable`.
- Mark every preview **`internal`** (accessible from tests, not public API).
- If the app uses a shared theme (e.g. `AppTheme`), wrap preview content when the component depends
  on `MaterialTheme` / tokens.

## Parameters and models

- **No collection parameters** on any `@Composable` that is part of the component API: do not use
  `List`, `Set`, `Map`, arrays, or other collections as direct parameters.
- **Wrap** in an `@Immutable` state type; use `kotlinx.collections.immutable.ImmutableList`,
  `ImmutableMap`, etc., for collection fields. If the module does not yet depend on
  `kotlinx-collections-immutable`, add the dependency to that module’s Gradle file as part of the
  same change (standard for KMP); do not fall back to raw `List`/`Map` on the composable API.
- **`@Immutable`:** Apply `androidx.compose.runtime.Immutable` to **every** custom type passed into
  a `@Composable` parameter that is not a Kotlin primitive, `String`, or a standard stable
  Compose/Android SDK type you already treat as stable.
- **Primitives / `String`:** No `@Immutable` required on parameters that are only primitives or
  `String`.

## Private sub-components (exception)

- A **small** (about ≤ 30 lines) child composable used **only** in one parent **may** be a *
  *`private fun`** in the **same file** as the parent.
- **No** `@Preview` for private helpers; **do not** move them to a separate file unless promoted to
  a reusable `*Component`.
- If reuse grows (second call site) or size/complexity grows, **extract** to `*Component.kt` with
  full rules (previews, `@Immutable` state, etc.).

## Paparazzi detection and tests

1. Read the target module’s **`build.gradle.kts`** or **`build.gradle`**. If the file text contains
   **`paparazzi`** (case-sensitive match is fine; typical forms: `app.cash.paparazzi`, `paparazzi`),
   treat Paparazzi as enabled for that module.
2. Create **`<ComponentName>Test.kt`** alongside JVM/Android unit tests:
    - **Name:** matches the component (e.g. `AvatarComponent` → `AvatarComponentTest.kt`).
    - **Package:** align with the component’s package (same or conventional test package used in the
      module).
    - **Location:** Use the **same test source set** as existing Paparazzi tests in that module. If
      none exist, prefer **`src/androidUnitTest/kotlin/`** under the Android target for KMP UI
      modules (adjust if the project uses `src/test/...` for Paparazzi).
3. For **each `internal` preview** function in the component file, add **one `@Test` method** that
   invokes that preview composable inside `paparazzi.snapshot { ... }` (or the project’s established
   Paparazzi helper pattern if already present—mirror it).

### Paparazzi test sketch (adapt to project’s Paparazzi version and theme)

```kotlin
class AvatarComponentTest {
    @get:Rule
    val paparazzi = Paparazzi(/* device config: match project defaults */)

    @Test
    fun snapshotAvatarComponentPreview() {
        paparazzi.snapshot {
            AppTheme { AvatarComponentPreview() }
        }
    }

    @Test
    fun snapshotAvatarComponentPreviewDark() {
        paparazzi.snapshot {
            AppTheme(darkTheme = true) { AvatarComponentPreviewDark() }
        }
    }
}
```

- Import preview composables from the **same module** test source (same compilation unit as
  `internal` previews in many KMP setups; if previews are not visible from tests, co-locate previews
  in `src/androidMain` with `internal` + `Test` in the same source set per project convention—or
  follow existing module pattern).

## Imports (KMP)

- **Do not** invent a new dependency. Match **existing** imports in the module for Compose (e.g.
  `androidx.compose.material3` vs Material2, `org.jetbrains.compose.ui` where used).
- Use **`@Immutable`** from `androidx.compose.runtime.Immutable` when that artifact is already on
  the classpath (typical for Compose Multiplatform with AndroidX Compose runtime).

## Anti-patterns (do not generate)

- `List`/`Map`/arrays as direct `@Composable` parameters.
- Custom data classes as parameters **without** `@Immutable`.
- Standalone components without `Component` suffix or outside a `components` package.
- Public preview functions.
- Separate files for private one-off helpers that qualify for the exception rule.

## Good vs bad (quick reference)

**Bad:** `@Composable fun TagListComponent(tags: List<String>)`

**Good:** `@Immutable data class TagListComponentState(val tags: ImmutableList<String>)` +
`@Composable fun TagListComponent(state: TagListComponentState)`

**Bad:** Large or multi-use `private` composable in one file — extract to `*Component.kt` with
previews.

**Good:** Small, single-use `private fun UserCardHeader(...)` in `UserCardComponent.kt` with no
preview.

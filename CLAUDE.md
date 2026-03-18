# kmp-decompose-mvi-template Rules & Guidelines

## Tech Stack

- **Languages**: Kotlin Multiplatform (KMP)
- **Architecture**: Decompose (Navigation), MVIKotlin (State Management)
- **DI**: Koin
- **UI**: Compose Multiplatform
- **Network**: Ktor Client
- **Persistence**: Room
- **Logging**: Kermit
- **Testing**: Kotest, MockK, Turbine (Flow testing)

## Build & Run Commands

- **Build All**: `./gradlew build`
- **Build APK**: `./gradlew :composeApp:assembleDebug`
- **Run Android**: `./android_runner.sh` (Custom script for building and launching on
  emulator/device)
- **Run Desktop (JVM)**: `./gradlew :composeApp:run`
- **Run Server**: `./gradlew :server:run`
- **Run All Tests**: `./gradlew test`
- **Run Specific Module Tests**: `./gradlew :features:coffee:coffee-data:test`
- **Clean**: `./gradlew clean`
- **Lint (Ktlint)**: `./gradlew formatKotlin` / `./gradlew lintKotlin`
- **Static Analysis (Detekt)**: `./gradlew detekt`

## Architectural Patterns

### Decompose Components

- Define a component interface in `commonMain`: `interface FeatureComponent`
- Implement it with `DefaultFeatureComponent`
- Expose state as `Value<State>` or `StateFlow<State>`
- Navigation logic belongs to the parent component using `Child Stack` or `Child Slot`

### MVI (MVIKotlin)

- **Store**: Interface defining `Intent`, `State`, `Label`
- **StoreFactory**: Factory class containing `Executor` and `Reducer`
- **Executor**: Handles business logic, side effects, and dispatches `Message` or `Action`
- **Reducer**: Pure function that transforms `State` based on `Message`
- **Bootstrapper**: Optional, initializes the store with actions (e.g., loading initial data)

### Data Layer

- **Repository**: Interface in `domain`, implementation in `data`
- **DataSources**: Remote (Ktor) and Local (Room)
- **Mappers**: DTO -> Domain, Domain -> UI transformations

## Coding Standards

- **Naming**:
    - Components: `*Component`, `Default*Component`
    - Stores: `*Store`, `*StoreFactory`
    - Screens: `*Screen.kt` (Compose functions)
- **Packages**: Use `io.pylyp.<feature>.*`
- **Resources**: Use shared resources from `:common:resources` via `Res.strings.*` or `Res.images.*`
- **Immutability**: Prefer `data class` for State and DTOs; use `val` everywhere possible
- **Coroutines**: Use `DispatcherProvider` for injecting dispatchers; avoid `Dispatchers.IO`
  directly

## Testing Guidelines

- **Unit Tests**: Place in `src/commonTest`
- **Base Class**: Extend `io.pylyp.common.testkit.BaseTest`
- **Mocks**: Use MockK for dependency mocking
- **Flows**: Use Turbine for testing StateFlow/Flow emissions
- **MVI Tests**: Verify state transitions and label emissions in response to intents

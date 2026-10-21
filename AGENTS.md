# Agent guidance

This early-stage Kotlin Multiplatform app uses Compose Multiplatform, but Android is the only supported product target. Keep the iOS target buildable with minimal, non-functional stubs; do not implement iOS-specific functionality unless explicitly requested. Follow feature-first modularization, Clean Architecture principles, MVVM, and the Repository pattern. Current placeholder screens do not define the full architecture.

## Required architectural boundaries

- `core` owns reusable cross-feature infrastructure and components (currently `designsystem` and `navigation`). `feature` owns user-facing features (`auth`, `onboarding`, `schedule`, `settings`, `spaces`). `shared` composes features, app navigation, and shared app setup. `androidApp` and `iosApp` are platform entry points. Features may depend on core; core must not depend on features, and features must not depend on shared. Direct feature-to-feature dependencies require explicit justification.
- Each feature separates `data`, `domain`, and `presentation` responsibilities as they arise. Domain owns domain models, repository interfaces, and use cases with real business logic. It must not depend on data implementations, presentation, Compose, Ktor, DTOs, persistence implementations, platform APIs, or ViewModels. Never add a pass-through use case.
- Data owns network access, persistence, concrete repositories, DTOs, and external-to-domain mapping. DTOs and their mapping stay in data. Repositories expose domain-oriented APIs, never DTOs or persistence entities.
- Use Ktor for networking. Put shared network infrastructure in a core network module when needed; keep feature endpoints and DTOs in the owning feature's data layer. ViewModels and UI must not call Ktor directly; UI must not access persistence APIs.
- Presentation owns ViewModels, Compose UI, UI state, useful actions/events, and feature navigation integration. Use the multiplatform Lifecycle ViewModel API already available; do not add a custom KMP ViewModel base class without a concrete need. Keep business logic out of composables.
- Structure screens as `Route` plus `Content`: `Route` obtains the ViewModel, collects state, connects navigation, and passes state and callbacks to `Content`. `Content` must not receive a ViewModel or access repositories, DI, network clients, or navigation infrastructure.
- Use unidirectional data flow. ViewModels expose immutable `StateFlow<UiState>` and keep `MutableStateFlow` private. Use Koin for dependency injection; the module owning an implementation declares its dependencies.
- Use structured concurrency. Never use `GlobalScope` or swallow `CancellationException`.

## Implementation preferences

- Prefer `commonMain`; use platform-specific source sets only for genuine platform API needs. Add `data`, `domain`, or `presentation` packages only when they have work to do.
- Authentication providers are platform-specific: Android uses Google Sign In through Credential Manager, while iOS will use Sign in with Apple when iOS development begins. Keep shared authentication UI and session boundaries provider-neutral. Do not add Google authentication stubs or configuration to iOS, and do not implement Apple authentication until explicitly requested; keep the iOS target buildable with a disabled placeholder in the meantime.
- Keep Android Google authentication in Android source sets. Supply the current `Activity` only when launching Credential Manager UI, never retain it in an application singleton, and create request options and nonces for each authentication attempt rather than injecting them as long-lived dependencies.
- Favor meaningful boundaries over ceremonial layers: a ViewModel may call a repository interface directly for simple CRUD or retrieval. Add remote/local data sources only when they encapsulate a real responsibility. Use internal extension functions for simple DTO mappings; use mapper classes/packages when mapping is complex or reused.
- Keep `Content` stateless where practical, receiving immutable state and callbacks. Move UI components into the design system only when genuinely reusable across features.
- Prefer one immutable UI state data class; use sealed state hierarchies only for mutually exclusive states. Prefer intent-named ViewModel methods such as `onLoginClick()` and `onEmailChanged(value)`. Add sealed action/event types only when complexity warrants them. Represent important ViewModel outcomes as state where practical instead of defaulting to channels, shared flows, or one-shot effect wrappers.
- Prefer feature-level and core-module-level Koin modules over one global module of unrelated dependencies. Use suspend functions for one-shot asynchronous work and `Flow` for actual streams. Avoid unnecessary concurrency and platform-specific coroutine decisions in domain logic.
- Prefer immutable data, `val`, narrow visibility (`internal` for implementation details), idiomatic Kotlin, composition, and simple explicit APIs. Avoid speculative interfaces, managers, and wrappers. Prefer existing dependencies; add third-party libraries only for a concrete need.

## Decisions still local to each task

Do not establish repository-wide choices yet for the local database or persistence implementation, offline-first versus network-first behavior, source of truth, caching, retries, a global error model, logging framework, or complete testing strategy and library selection. If a task needs one, choose the smallest local solution and report the unresolved broader decision.

## Agent workflow

Before editing, inspect relevant code and identify the owning module and layer. Follow existing patterns when compatible with these rules. Keep changes focused and avoid unrelated refactors. Inspect the final diff, run relevant existing build, lint, and test tasks, and report what could not be verified. For multi-agent work, parallelize read-heavy exploration more freely than writes; do not edit the same files concurrently.

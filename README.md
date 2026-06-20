# kotlin-ode

`kotlin-ode` is an Android library by ÂnimaLab for teams that want a more opinionated and consistent delivery flow in Kotlin applications.

The project provides a small architectural runtime for:

- use case execution
- delivery/result orchestration
- MVVM support abstractions
- channel-based communication between view and controller/viewmodel layers
- reusable Android UI base components

The goal is to make application flow easier to standardize, easier to reason about, and less vulnerable to common implementation mistakes around lifecycle, asynchronous delivery and coordination between layers.

## Repository

- source: [github.com/animalab-netizen/kotlin-ode](https://github.com/animalab-netizen/kotlin-ode)

## Status

`kotlin-ode` is currently in early stage and evolving toward public Maven/Gradle distribution.

The API is usable, but still under refinement. Expect incremental improvements in typing, runtime safety and publication maturity as the library evolves.

## Coordinates

Current coordinates:

- `groupId`: `br.com.lab`
- `artifactId`: `kotlin-ode`
- `version`: `0.0.1`

Dependency:

```gradle
dependencies {
    implementation "br.com.lab:kotlin-ode:0.0.1"
}
```

## Repositories

For local development:

```gradle
repositories {
    mavenLocal()
}
```

For remote consumption, configure the Maven repository used by your distribution flow:

```gradle
repositories {
    maven { url "https://maven.pkg.jetbrains.space/v2/p/v-2-software-house/maven" }
}
```

## Installation

Example `build.gradle`:

```gradle
repositories {
    mavenLocal()
    maven { url "https://maven.pkg.jetbrains.space/v2/p/v-2-software-house/maven" }
}

dependencies {
    implementation "br.com.lab:kotlin-ode:0.0.1"
}
```

## Public API

The intended public surface of `kotlin-ode` is centered on these concepts:

- `UseCase<P, R>`
- `UseCaseDispatcher<P, R>`
- `Output`, `ValueOutput`, `ErrorOutput`, `EmptyOutput`
- `BaseViewModel`
- `Controller`
- `ControllerFactory`
- `BaseActivity`, `MvvmActivity`, `BaseFragment`, `BaseBottomSheet`
- `SequenceUseCase` and `ChainedUseCase`
- business exceptions exposed by the library

Internal coordination helpers are intentionally not part of the product contract and may change without notice.

## API Stability Notes

Current guidance:

- prefer typed channels via `BaseViewModel.Channel<T>`
- legacy string-based channels are still supported for compatibility
- prefer matching on the sealed `Output` hierarchy instead of relying only on boolean helpers
- do not couple application code to internal runtime helpers or implementation details of dispatch or collection

For example, this is the preferred mental model for output handling:

```kotlin
when (val output = result) {
    is ValueOutput -> render(output.value)
    is ErrorOutput -> handleError(output.error)
    is EmptyOutput -> Unit
}
```

## Core Concepts

### 1. UseCase

`UseCase<P, R>` is the main business execution abstraction.

It provides a standard lifecycle for:

- input validation via `guard`
- execution via `execute`
- result delivery via `onResult`
- failure handling via `onError`

This helps teams keep business flow consistent instead of re-implementing orchestration in every feature.

### 2. Dispatcher

`UseCaseDispatcher` runs use cases with coroutine dispatchers for background execution and result delivery.

This gives a standard way to execute work off the UI thread and return the result to the consumer layer.

### 3. BaseViewModel

`BaseViewModel` provides:

- dispatch support for use cases
- channel registration and observation
- typed channel support
- job disposal on lifecycle cleanup

This is the main delivery point between business and UI layers.

### 4. Channels

The library supports two styles:

- legacy string channels
- typed channels with `BaseViewModel.Channel<T>`

Typed channels are the safer option and should be preferred in new code.

### 5. Base UI Components

The library includes base Android components such as:

- `BaseActivity`
- `MvvmActivity`
- `BaseFragment`
- `BaseBottomSheet`

These components help standardize how features observe delivery channels and react to success/error output.

## Basic Example

### ViewModel

```kotlin
class LoginViewModel(
    private val loginUseCase: UseCase<LoginParams, User>
) : BaseViewModel() {

    val loginChannel = channel<Output<User>>("login")

    fun login(params: LoginParams) {
        dispatchUseCase(params, loginUseCase) { output ->
            postValue(loginChannel, output)
        }
    }
}
```

### Fragment

```kotlin
class LoginFragment : BaseFragment<LoginViewModel>() {
    override fun setupController(): LoginViewModel = TODO()

    override fun getLayout(): Int = TODO()

    override fun setupViews(view: View) {
        observe(controller.loginChannel) { output ->
            when (output) {
                is ValueOutput -> handleSuccess(output.value)
                is ErrorOutput -> handleError(output.error)
                is EmptyOutput -> Unit
            }
        }
    }

    override fun channelName(): String = "unused-when-typed-observe-is-adopted"
}
```

Note:

- the library still supports the original string-based observation model
- typed channels should be preferred for new integrations

## Publishing

Publish locally:

```bash
./gradlew publishReleasePublicationToMavenLocal
```

Publish to the configured Maven repository:

```bash
./gradlew publishReleasePublicationToMavenRepository
```

## Compatibility Notes

The library is Android-oriented and depends on AndroidX, Lifecycle, Material Components and Kotlin coroutines.

If you consume the library from another Android project, keep your AndroidX and Kotlin versions reasonably aligned with the artifact version you import.

## Contributing

See [CONTRIBUTING.md](/Users/caiosanchezchristino/Desktop/ode-projects/kotlin-ode/CONTRIBUTING.md).

## Changelog

See [CHANGELOG.md](/Users/caiosanchezchristino/Desktop/ode-projects/kotlin-ode/CHANGELOG.md).

## Maintainer

- name: `ÂnimaLab`
- email: `animalab.desenvolvimento@gmail.com`

## License

This project is licensed under Apache-2.0. See [LICENSE](/Users/caiosanchezchristino/Desktop/ode-projects/kotlin-ode/LICENSE).
## UseCase Guide

See [USECASE_GUIDE.md](/Users/caiosanchezchristino/Desktop/ode-projects/kotlin-ode/USECASE_GUIDE.md) for combinations, adoption guidance and common implementation doubts.

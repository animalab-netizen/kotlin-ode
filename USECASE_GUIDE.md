# UseCase Guide

## Why ODE starts from `UseCase`

In ODE, `UseCase` is the smallest stable unit of business intention.

Its role is not only to execute logic, but to standardize:

- input validation
- success delivery
- error delivery
- execution shape
- readability across features

The value is cognitive, not only technical. A reader should quickly recognize where validation happens, where the business rule happens and where delivery happens.

## Base Lifecycle

`UseCase<P, R>` gives a fixed flow:

1. `guard(param)`
2. `execute(param)`
3. `onResult(output)`
4. `onError(error)` when needed

That fixed lifecycle reduces improvisation in each feature.

## Basic Example

```kotlin
class LoginUseCase : UseCase<String, String>() {
    override fun execute(param: String?): Output<String> {
        return when (param?.trim()) {
            null, "" -> EmptyOutput()
            else -> ValueOutput("Hello, $param.")
        }
    }
}
```

Use this shape when the business rule is single-step and local.

## Guard Example

```kotlin
class LoadProfileUseCase : UseCase<String, Profile>() {
    override fun guard(param: String?): Boolean {
        return !param.isNullOrBlank()
    }

    override fun onGuardError() {
        onResult(ErrorOutput(IllegalArgumentException("profile id is required")))
    }

    override fun execute(param: String?): Output<Profile> {
        return ValueOutput(Profile(id = param!!))
    }
}
```

Use `guard` only when there is real precondition logic.

Do not override `guard` only because the method exists.

## Dispatch Example

```kotlin
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {
    val loginChannel = channel<Output<String>>("login")

    fun login(name: String) {
        dispatchUseCase(name, loginUseCase) { output ->
            postValue(loginChannel, output)
        }
    }
}
```

This is the normal ODE delivery path from business to viewmodel.

## Sequence Combination

`SequenceUseCase` is used when multiple use cases must run in order and every result matters.

```kotlin
val useCase = SequenceUseCase.builder()
    .add(LoadSessionUseCase(), sessionId)
    .add(LoadProfileUseCase(), profileId)
    .build()
```

Use it when:

- order matters
- each step should still be observable
- the output stream itself is part of the feature behavior

Avoid it when only the final transformed result matters. In that case `ChainedUseCase` is normally better.

## Chained Combination

`ChainedUseCase` is used when one use case produces the input for the next.

```kotlin
val useCase = ChainedUseCase(
    first = LoadTokenUseCase(),
    second = LoadProfileFromTokenUseCase()
)
```

Use it when:

- the second step depends directly on the first value
- the mental model is pipeline, not batch
- you want error and empty propagation without extra branching in the caller

## When not to override

The most common mistake in ODE adoption is unnecessary override.

Do not override:

- `guard` if there is no real precondition
- `onError` if default `ErrorOutput` is enough
- `onResult` if the caller already handles the output through `dispatchUseCase`
- `process` unless you are changing the product contract on purpose

Extra override without need increases resistance to ODE because it makes the pattern look heavier than it is.

## Frequent Resistance And How To Read It

### "It is too abstract"

Usually this means the team is not yet used to separating:

- orchestration
- business rule
- delivery

ODE keeps them explicit so each concern becomes predictable.

### "It is too much code for simple features"

For very small rules, that perception can be valid at first.

The payoff appears when the codebase grows and every feature keeps the same reading pattern.

### "Why not put everything in ViewModel"

Because ViewModel should coordinate feature delivery, not become the place where business rules accumulate.

When business rules stay inside `UseCase`, reuse and testing become simpler and the UI layer stays readable.

## Practical Rule

If a future reader can answer these questions in seconds, the `UseCase` is modeled well:

- what is the input?
- what is the business decision?
- what is success?
- what is error?
- how is the result delivered?

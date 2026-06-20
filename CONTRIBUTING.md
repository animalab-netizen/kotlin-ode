# Contributing

Thank you for your interest in contributing to `kotlin-ode`.

## Scope

Contributions are welcome in areas such as:

- runtime safety
- API consistency
- Android integration robustness
- test coverage
- documentation clarity
- publication quality

## Workflow

1. Open an issue or describe the problem clearly before large changes.
2. Keep changes focused and intentional.
3. Add or update tests for behavioral changes.
4. Make sure the project test suite passes before proposing changes.

## Development Notes

Run tests with:

```bash
./gradlew test
```

The project currently targets Android/Kotlin and depends on AndroidX and coroutines.

## API Discipline

When contributing:

- prefer typed channels over raw string channels in new code
- avoid expanding the public API surface without strong justification
- keep internal coordination helpers internal whenever possible
- preserve backward compatibility unless a breaking change is intentional and documented

## Maintainer

- name: `ÂnimaLab`
- email: `animalab.desenvolvimento@gmail.com`

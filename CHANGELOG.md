# Changelog

All notable changes to `kotlin-ode` should be documented in this file.

## 0.0.1

- extracted and stabilized the standalone `kotlin-ode` project
- configured standalone Gradle build, tests and publication structure
- introduced typed channel support alongside legacy string channels
- removed `GlobalScope` from use case dispatch flow
- simplified and hardened job disposal behavior
- fixed multi-observer channel behavior in `BaseViewModel`
- made `SequenceUseCase` free of accumulated state across executions
- migrated `Output` to a sealed hierarchy with `ValueOutput`, `ErrorOutput` and `EmptyOutput`
- reduced accidental exposure of internal runtime helpers
- added community-facing README and publication metadata

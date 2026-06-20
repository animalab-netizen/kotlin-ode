# Changelog

All notable changes to `kotlin-ode` should be documented in this file.

## 0.1.0

- prepared the standalone repository for public GitHub distribution
- aligned publication metadata with the `animalab-netizen` organization
- added CI workflow for build, test and local publication validation
- documented the public release gate for GitHub and Maven distribution

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

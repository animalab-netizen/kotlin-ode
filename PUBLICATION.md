# Publication Guide

This document describes the current and next-step publication strategy for `kotlin-ode`.

## Current State

The library is already configured for:

- local publication with `publishReleasePublicationToMavenLocal`
- remote publication to the Maven repository configured in `gradle.properties`
- generation of AAR, sources JAR, POM and Gradle module metadata
- publication metadata including maintainer and Apache-2.0 license
- `organization` and `scm` POM metadata through Gradle properties

## Current Coordinates

- groupId: `br.com.lab`
- artifactId: `kotlin-ode`
- version: `0.1.0`

## Source Repository

- repository: [github.com/animalab-netizen/kotlin-ode](https://github.com/animalab-netizen/kotlin-ode)
- scm url: `https://github.com/animalab-netizen/kotlin-ode`
- scm connection: `scm:git:https://github.com/animalab-netizen/kotlin-ode.git`
- scm developer connection: `scm:git:git@github.com:animalab-netizen/kotlin-ode.git`

## Local Publication

```bash
./gradlew publishReleasePublicationToMavenLocal
```

## Configured Remote Publication

```bash
./gradlew publishReleasePublicationToMavenRepository
```

This uses the Maven repository configured through `gradle.properties`.

## Current Remote Publication Limitation

The project is publication-ready, but the currently configured JetBrains Space endpoint has recently responded with `503 Service Temporarily Unavailable` during publication attempts.

That is an external availability problem, not a project metadata or artifact generation problem.

## Recommended Next Step For Broader Public Distribution

To distribute beyond the currently configured repository, prepare one of these targets:

### 1. Maven Central

Recommended when:

- the library should be broadly discoverable
- consumers should not need a custom repository declaration beyond Maven Central

Typical requirements:

- namespace ownership validation
- signed artifacts
- complete POM metadata
- public source repository URL
- issue tracker / SCM metadata

### 2. GitHub Packages

Recommended when:

- source repository is hosted on GitHub
- you want tight coupling between repository and package distribution

Typical requirements:

- GitHub repository URL
- package publication credentials or GitHub Actions token

## Gaps To Fill Before Wider Public Release

The following items are still recommended before a broader public push:

- define issue tracker URL
- optionally add artifact signing for Maven Central readiness
- optionally publish a sample app repository
- optionally add CI workflow for build, test and publish automation

## Suggested Release Checklist

### GitHub Release Gate

1. Run `./gradlew test`
2. Run `./gradlew publishReleasePublicationToMavenLocal`
3. Confirm CI is green in `.github/workflows/ci.yml`
4. Update `CHANGELOG.md`
5. Confirm version in `build.gradle`
6. Commit release metadata
7. Create and push tag `v0.1.0`

### Maven Publication Gate

1. Confirm Maven repository credentials are available
2. Confirm POM metadata, SCM metadata and maintainer metadata
3. Publish with `./gradlew publishReleasePublicationToMavenRepository`
4. Validate artifact coordinates from a clean sample consumer
5. Announce the published version in release notes

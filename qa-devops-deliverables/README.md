# QA / DevOps deliverables

This folder is a standalone handoff bundle for the QA, DevOps & Test Automation Engineer tasks. It does not change application source, Gradle configuration, or existing tests.

## Files

- `test-plan.md` — scope, test methods, roles, entry/exit criteria, and evidence.
- `test-cases.csv` — executable manual test cases for the five core modules, including the requested failure and privacy scenarios.
- `requirements-traceability.csv` — requirement-to-test-to-expected-result mapping; replace the example requirement IDs with the team's approved FR/NFR IDs before claiming formal traceability.
- `android-ci.yml` — GitHub Actions workflow. Copy it to `.github/workflows/android-ci.yml` in a feature branch/PR.
- `upload-and-branch-protection.md` — exact Git commands, artifact location, and repository-admin branch protection checklist.
- `execution-report.md` — what was actually run and the verified outcome in this environment.

## CI behaviour

The workflow runs on pull requests and pushes to `main` or `develop`, and can be started manually. It runs Android lint, JVM unit tests, builds the debug APK, and uploads the APK for 14 days as the `app-debug-apk` artifact. Instrumentation/UI tests are not included because this project currently has only the generated sample instrumentation test and no stable emulator CI setup.

The workflow uses Temurin Java 25 to match the checked-in Gradle daemon JVM criteria. Android SDK Platform 37 and Build Tools 37.0.0 are installed on the GitHub-hosted runner. If Android Gradle Plugin 9.4.1 requires a different SDK package revision, update the SDK install step to the exact package shown by the Actions failure log.

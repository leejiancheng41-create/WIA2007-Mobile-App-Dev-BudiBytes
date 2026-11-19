# BudiBytes QA test plan

## Objective

Check the core reporting, review, maintenance, map/risk, and notification journeys; record reproducible evidence; and gate pull requests with free GitHub Actions checks.

## Scope: five core modules

1. Report submission and photo selection.
2. Report listing, details, and administrator verification/moderation.
3. Maintenance task assignment and resolution.
4. Hazard map and risk overview.
5. Notifications and user-visible alert state.

## Test levels and tools

- Static/build gate: Android Lint, Gradle `testDebugUnitTest`, and `assembleDebug` in GitHub Actions.
- Manual functional testing: Android Studio emulator (Medium Phone API 37.0) and deterministic seeded demo data.
- Instrumented UI testing: future work after stable selectors and realistic acceptance criteria are agreed. The current sample instrumentation test checks only application package context.
- Free tools: GitHub Actions, Gradle Wrapper, Android Lint, JUnit, Android Studio Emulator, and GitHub Actions artifacts.

## Roles

- QA/DevOps owner: maintain cases, run the smoke/regression set, inspect CI, attach evidence, and report defects.
- Feature developer: fix defects and add focused automated coverage with the feature change.
- Reviewer: confirm traceability, expected results, and CI status before merge.

## Entry criteria

- A commit/PR is available and dependencies can be resolved.
- Emulator is booted and ADB reports `device` (not `offline` or `unauthorized`).
- Test data is disposable demo data; no production or personal data is used.

## Exit criteria

- All P0/P1 cases pass, or each failure has a filed issue and explicit reviewer acceptance.
- Lint, JVM unit tests, and debug APK build pass in CI.
- Evidence is attached to the PR or recorded in a run report.
- No report owner identity or restricted moderation action is exposed to an unauthorized role.

## Evidence to capture

- Commit SHA, branch, emulator/API level, app version, test ID, pass/fail, actual result, screenshot/logcat for failures, and linked issue.
- Do not include real user emails, location, or identifiable reports in screenshots/artifacts.

## Known test-environment limitation

The app seeds static demo records and stores report data locally in a Room database (`RainGuardDatabase`); SharedPreferences holds only the session role and e-mail. Network/offline, AI timeout, notification delivery, and moderator authorization cases cannot be represented as backend integration tests until corresponding backend/services exist; use the specified manual simulation and label results accordingly.

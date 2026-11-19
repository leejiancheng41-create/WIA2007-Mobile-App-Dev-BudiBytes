# Execution report

Date: 2026-11-19 (Asia/Kuala_Lumpur)

## Environment checks

- Android Studio JBR: Java 25.
- Gradle Wrapper: 9.6.0.
- Android Gradle Plugin: 9.4.1.
- App module: compile SDK 37, min SDK 24, Java source/target 11.
- Medium Phone API 37.0: ADB state `device`; boot completed; Package Manager service available.
- Pixel 9: ADB state was `unauthorized`; not used for this run.

## Commands attempted and outcomes

| Command/action | Outcome |
| --- | --- |
| `./gradlew --no-daemon --console=plain --no-configuration-cache assembleDebug` with Android Studio JBR | **PASS**, `BUILD SUCCESSFUL` (24 seconds); 35 actionable tasks, 7 executed, 28 up-to-date. |
| Install APK on Medium Phone with `adb install -r` | **FAIL as expected for this artifact**: `INSTALL_FAILED_TEST_ONLY`. |
| Install same APK with `adb install -r -t` | **PASS**: `Success`. |
| Launch `com.example.budibytes` with `adb shell monkey -p com.example.budibytes 1` | **PASS**: one launch event injected. |
| Verify process and resumed activity | **PASS**: process ID observed; top resumed activity `com.example.budibytes/com.um.rainguard.ui.auth.MainActivity`. |
| Lint and unit test tasks | **Not run in this environment**. They are configured in the CI workflow; local task run was not completed. |
| Full manual functional test cases | **Not run**. The attached CSV is the execution-ready test set, not claimed test evidence. |

## Important APK note

The APK installed successfully was the generated intermediate artifact at `app/build/intermediates/apk/debug/app-debug.apk`. It is test-only, so installation needs `-t`. A standard distributable debug artifact should be taken from `app/build/outputs/apk/debug/app-debug.apk` after `assembleDebug` completes and creates the final output copy. The CI workflow uploads that standard output path and fails if it is absent.

## CI status

The GitHub Actions workflow is prepared but has not been pushed to GitHub or run there. No PR, status check, or branch-protection rule is claimed as completed. Review the Java 25 / SDK 37 runner setup against the first Actions log.

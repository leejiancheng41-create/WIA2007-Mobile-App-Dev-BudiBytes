# Upload the QA/DevOps bundle in an isolated PR

The original application and Gradle files are not part of this bundle. Copy only the new deliverables into a feature branch. Do not commit `gradle/gradle-daemon-jvm.properties` from the local Android Studio troubleshooting session.

From the repository root in PowerShell:

```powershell
git switch -c codex/qa-devops-deliverables
New-Item -ItemType Directory -Force .github\workflows | Out-Null
Copy-Item qa-devops-deliverables\android-ci.yml .github\workflows\android-ci.yml
git add qa-devops-deliverables .github\workflows\android-ci.yml
git status --short
git commit -m "Add QA test plan and Android CI workflow"
git push -u origin codex/qa-devops-deliverables
```

Then open a pull request to `main`. Keep test artifacts and reports free of real user data.

## GitHub branch protection (repository admin)

After the workflow has run once, configure **Settings → Branches → Add branch protection rule** for `main` (and `develop` if the team uses it):

1. Require a pull request before merging.
2. Require the status check **Lint, test, and build debug APK** (the check-run name shown on a PR).
3. Require at least one approving review if team policy requires it.
4. Block force pushes and branch deletion on protected branches.
5. Apply the same protections to administrators if team policy requires it.

Only a repository administrator can apply these settings. The workflow cannot turn on its own required status check before its first run.

## Artifact

The successful Actions run publishes `app-debug-apk`; download it under that run's **Artifacts** section. It is retained for 14 days. A local debug APK, when produced, is at `app/build/outputs/apk/debug/app-debug.apk`.

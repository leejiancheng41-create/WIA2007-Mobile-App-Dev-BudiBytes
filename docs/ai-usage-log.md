# UM RainGuard — AI Usage Log

| | |
|---|---|
| **Document ID** | AI-01 — AI usage log (mandatory appendix) |
| **Project** | WIA2007 Mobile App Dev — BudiBytes (`UM RainGuard`) |
| **Policy** | Universiti Malaya AI policy — **Level 2, limited usage, specific tasks only** |
| **Status** | Living document. Append-only. Every commit to this repository adds exactly one entry. |
| **Applies to** | Every person who commits to this repository, and every AI tool they use |

> This file is the appendix required by clause 2 of the UM policy. It is deliberately boring:
> one entry per commit, dated, with the prompts and what was done with the answers. If an entry is
> missing, the change is treated as undisclosed AI use.

---

## 1. What the policy requires

The assignment runs under **Level 2 (limited usage — specific tasks only)**. Generative AI may help
with brainstorming, tone, refactoring and summarising technical material. It may **not** replace the
team's own design, reasoning or debugging. Paraphrased from the policy:

| Clause | Requirement | How this repository satisfies it |
|---|---|---|
| 1 | The core intellectual work — algorithm design, synchronisation strategy, race/deadlock resolution, performance analysis — is the team's own. | Product decisions, module design and acceptance testing stay with the team. See §5 for the boundary in each entry. |
| 2a | Name the AI tool(s) used. | Every entry has a **Tool** field: product, model, interface. |
| 2b | Record the specific prompts or queries. | Every entry quotes its prompts, trimmed only where marked `…`. |
| 2c | Say how the output was used, tested and modified. | Every entry has **Used for**, **Verification** and **Human changes** fields. |
| 3 | An inaccurate or missing log, or passing AI work off as original, is a disciplinary matter. | A missing entry is never an accident here: rule 4 below requires an entry even when no AI was used. |

Appendix 2(b) of the assignment is the standard template. The template in §3 is that template in
Markdown form.

## 2. The rule for every commit

> **No commit without an entry.**

1. **One entry per commit.** Any commit that adds, changes or deletes code, resources, build files,
>   documentation or configuration appends exactly one entry to §5.
2. **Append only.** Never edit or delete an earlier entry. A correction is a new entry that says what
   it corrects.
3. **Say where the AI helped and where it did not.** "Used for" must name files or areas, not just
>   "coding". If an AI tool was not used for a commit, the entry says so — that is still an entry.
4. **The entry ships with the change.** It must be in the same commit, or the commit immediately
>   after it. A pull request without its entry is not merged.
5. **Prompts are quoted.** Trim with `…` where a prompt is long or repeats; never rewrite it to look
>   better than it was.
6. **Verification is stated honestly.** "Compiles" is not "tested". "Not run on a device" is a
>   legitimate entry; claiming a test that did not happen is a violation.
7. **The commit message names the entry**, e.g. a trailing line `AI-2026-11-18-01`.
8. **The author owns the result.** An AI-written line is still the committer's responsibility in
>   review and in the report.

### How to add an entry

1. Copy the template from §3.
2. Give it the next id: `AI-<yyyy-mm-dd>-<nn>`, `nn` counting entries within that day.
3. Fill every field. Leave nothing as "TBD" except **Commit**, which is filled with the short SHA
   right after committing.
4. Append it at the end of §5 and commit it with the change.

## 3. Entry template

```markdown
### AI-YYYY-MM-DD-NN — <one-line description of the change>

| | |
|---|---|
| **Date** | YYYY-MM-DD (Asia/Kuala_Lumpur) |
| **Author** | <name / GitHub handle> |
| **Commit** | `<short sha>` — <subject> |
| **Tool** | <product>, <model>, <interface — e.g. IDE agent, chat> |
| **AI used for** | <files or areas, in one line> |
| **Prompts** | see below |
| **Verification** | <what was actually run/checked, and what was not> |
| **Human changes** | <what the author edited, rejected or decided after the AI output> |

**Prompts**

> 1. "<prompt>"
> 2. "<prompt>"

**What the AI produced**

<files, and one sentence each on what it did>

**How it was checked**

<commands, comparisons, reviews; state explicitly what has not been verified>

**What stayed human**

<the decisions, design and testing that the author did, and any part of the output that was
rejected or rewritten>

**Follow-ups**

<known gaps carried into the next entry>
```

## 4. Scope of this project

* **Human-owned, AI-assisted.** The module specification was written by the team (`@xp`); the Room
  schema and entities in PR #4 were written by Xuan Pei. The entries below record where AI was used
  on top of that work, and the AI does not own the product design.
* **Level 2 boundary in practice.** AI may draft documentation, refactor code and explain an error.
  It may not decide what the app should do, and it may not be the only reader of a change: a device
  run and a teammate review remain the team's responsibility.
* **Tool in use.** DeepSeek Harness (`dsh`) coding agent, model `deepseek-flash`, in the DSH web
  interface. It has no network access of its own beyond the tools it is given, and it cannot run the
  app on a device.

---

## 5. Log

### AI-2026-11-18-01 — Documentation set, PR #4 review, and the migration off the JSON data layer

| | |
|---|---|
| **Date** | 2026-11-18 (Asia/Kuala_Lumpur) |
| **Author** | BudiBytes team (commit author) |
| **Commit** | `3173939` — feat(data): run every screen on Room and move the models out of the database package |
| **Tool** | DeepSeek Harness (`dsh`) coding agent, model `deepseek-flash`, web interface |
| **AI used for** | `docs/` (conversion, review, usage log), `data/repository/`, 12 Activities, `database/` edits, package relocation |
| **Verification** | Gradle compile and APK build pass; docx-to-Markdown compared character by character; the `INSERT OR REPLACE` failure reproduced in SQLite; Room identity hash compared before and after the move. **Not run on a device.** |
| **Human changes** | The team supplied the specification, the schema (PR #4) and the two decisions (Java, real login); the author reviewed, accepted and committed the output |

**Prompts** (author's messages, trimmed with `…`)

> 1. "review this pr and see whether is it follow the documentation, explain on what the tech stack
>    it use for database, and view the docs/, then translate this modules document into the md format
>    for documentation" — with `UM RainGuard Module Specification (5 Modules).docx` attached.
> 2. "in the Module Specification.md we use java for our programming language, and it have to be
>    real login, change it, then i found out there is a directory
>    app/src/main/java/com/um/rainguard/data, is this package overlap with what the database
>    responsible?"
> 3. "will it able to transfer the data declare from database/ to data/ and then just keep database
>    as to responses on database only"
> 4. "then do both steps start from step 1"
> 5. "alright, write me a commit message, and since we need so define AI usage … add a file in docs/
>    name as AI Usage Log documentation, and set a rules everytime when a commit/changes/make to this
>    repo, then as well for this edition, i need you to append as the first log"

**What the AI produced**

* `docs/module-specification.md` (MS-01) — the Word specification converted to Markdown, figure
  extracted to `docs/assets/report-status-flow.png`, then updated for the two team decisions.
* `docs/database-layer.md` (DB-01) — stack explanation, schema, the 31 module functions mapped to
  the DAOs, findings DB-F-01…14, and §9 recording the follow-up work.
* `docs/README.md` — index and metrics for the two new documents.
* `data/repository/ReportRepository.java` — rewritten over `ReportDao`; `LiveData` for the screens,
  a synchronous snapshot for existing render paths, writes on `databaseExecutor` inside
  `runInTransaction`.
* `data/repository/DemoData.java` — first-run seed of 3 users, 5 reports, 5 `ai_triage` rows and 7
  `verification_logs` rows. Seed content is adapted from the demo data the team had written in the
  deleted JSON repository.
* 12 Activities, `MapSketchView`, `UIUtils`, `BaseActivity` — converted from `ReportItem` to
  `Report`, from direct mutation to repository calls, and from `SharedPreferences` to the `users` row
  for notification preferences.
* `database/ReportDao.java` — `INSERT OR REPLACE` replaced with `@Upsert`; `touch`, `count` and
  `observeById` added. `UserDao`/`VerificationLogDao` gained `observeById`, `count`, `insertAll`.
* 17 model/projection/enum/constant files moved to `com.um.rainguard.data.model`; `ReportItem`,
  `HistoryEntry` and the JSON repository deleted.

**How it was checked**

* `.docx` → Markdown: every `w:t` text node concatenated and compared against the file, whitespace
  and Markdown punctuation stripped — identical (13,764 characters). After the two team decisions
  were applied, the same comparison was re-run and the remaining differences reviewed one by one.
* `:app:compileDebugJavaWithJavac` and `:app:assembleDebug` both pass; APK produced.
* The `INSERT OR REPLACE` → cascade data loss was reproduced against the same schema in SQLite
  (`verification_logs` count went `1 → 0`); Room's generated DDL confirms
  `PRAGMA foreign_keys = ON` and `ON DELETE CASCADE`.
* The package move was checked against Room's schema identity hash: unchanged at
  `2495d47b88651f48784e1474dcf1579b`, so no migration is needed.
* Not verified: no device or emulator run, so screen behaviour, the seed on first install and the
  photo flows are unproven at runtime.

**What stayed human**

* The specification content, the five-module split, the schema and entities (PR #4), and the demo
  narrative are the team's work, not the AI's.
* The two decisions that changed the specification — **Java 11** and **real login** — were the
  author's, not proposals from the AI.
* Acceptance of every output was the author's; the AI's own recommendations (e.g. the order of the
  two migration steps) were explained and then chosen by the author.

**Follow-ups**

* Run the app on a device: first launch seeding, community votes, and the admin verify → assign →
  resolve path are the three flows to exercise.
* Outstanding from DB-01: `pinX`/`pinY` missing from the public projection (DB-F-04), thin Module 2
  and Module 5 queries (DB-F-05, DB-F-06), no `Result<T>` or ViewModel (DB-F-07), destructive
  migration (DB-F-08).
* The sign-in is still the demo one; "real login" is recorded as a decision but not implemented.

### AI-2026-11-18-02 — Root README rewritten as the development guide

| | |
|---|---|
| **Date** | 2026-11-18 (Asia/Kuala_Lumpur) |
| **Author** | BudiBytes team (commit author) |
| **Commit** | `TBD` — `docs: explain how to use docs/ while developing` |
| **Tool** | DeepSeek Harness (`dsh`) coding agent, model `deepseek-flash`, web interface |
| **AI used for** | `README.md` (repository root); two wording corrections in `docs/database-layer.md` |
| **Verification** | Every relative link and every Markdown table in the root README checked by script; `docs/` lint clean. No source file changed, so no build was run. |
| **Human changes** | The author asked for the section, chose what a new teammate needs to know, and reviewed the wording; no code or behaviour changed |

**Prompts**

> 1. "alright, now update the root directory README on how to use the documentation in doc/ when they
>    develop"

**What the AI produced**

* `README.md` — replaces the three-line stub (which also carried the typo recorded as DB-F-14) with a
  development guide: quick start (toolchain, build, demo sign-in, first-launch seed, how to reset),
  *How to use `docs/` while you develop* (which document for which task, a before-you-code checklist,
  a change-to-document table, three rules that keep the docs readable), repository layout, pre-push
  checks, and the AI-01 rule.
* `docs/database-layer.md` — DB-F-14 marked **Fixed**, and "every data-driven screen" replaces the
  count "all twelve".

**How it was checked**

* A script resolved every relative link in the root `README.md` and re-checked the cell count of each
  Markdown table; `docs/` passes the same lint used for the other documents. No broken link, no
  malformed table.
* Nothing under `app/` was touched in this change, so the previous build result still stands and no
  device run was required.

**What stayed human**

* What the guide should teach, which credentials and paths it publishes, and the review of its tone
  were the author's calls. The AI did not decide how the team works.

**Correction**

Entry AI-2026-11-18-01 says that 12 Activities were converted. It is 10 files plus two shared ones:
the ten edited Activity files are `CampusHome`, `Dashboard`, `MyReports`, `Notifications`,
`HazardMap`, `MaintenanceTasks`, `Analytics`, `RiskOverview`, `ReportSubmission` and `ReportDetails`;
`AllReportsActivity` inherits the change from `MyReportsActivity` and was not edited, and
`BaseActivity` and `MapSketchView` are counted separately.

**Follow-ups**

* Run the app on a device: first launch seeding, community votes, and the admin verify → assign →
  resolve path.
* `docs/database-layer.md` still carries the open findings DB-F-04 to DB-F-12.

### AI-2026-11-19-01 — Unblock the CI gate, restore the API-27 theme variant, correct the QA deliverables

| | |
|---|---|
| **Date** | 2026-11-19 (Asia/Kuala_Lumpur) |
| **Author** | BudiBytes team (commit author) |
| **Commit** | `TBD` — `fix: unblock the CI gate and restore API-27 theme inheritance` |
| **Tool** | DeepSeek Harness (`dsh`) coding agent, model `deepseek-flash`, web interface |
| **AI used for** | `.github/workflows/android-ci.yml`, `qa-devops-deliverables/android-ci.yml`, `values/styles.xml`, `values-v27/styles.xml`, `qa-devops-deliverables/upload-and-branch-protection.md`, `test-plan.md`, `performance-test-plan.md`, `test-cases.csv`, `test-data.csv` |
| **Verification** | The workflow fix was proven on the throwaway PR #8: run 44547494014 finished green in 3m19s (lint, JVM unit tests, debug APK, artifact). The theme change was **not** run on a device or emulator. |
| **Human changes** | The author asked for the fix branch; accepting the theme change and the QA corrections is the author's decision. Nothing was merged by the AI. |

**Prompts**

> 1. "so can this CI be merged? just need to know will it functions as it stated"
> 2. "ok now just open another branch call fix/test and dont forget to follow the documentation in docs/"

**What the AI produced**

* `.github/workflows/android-ci.yml`, `qa-devops-deliverables/android-ci.yml` — `android-actions/setup-android@v3` becomes `@v4`, because v3's default `packages` includes the `tools` package that Google removed from the SDK repository, so the action exits 1 before any Gradle task runs. `platforms;android-37` becomes `platforms;android-37.0`, because API 37 platforms are minor-versioned and that is the hash `compileSdk { release(37) }` resolves to.
* `values/styles.xml`, `values-v27/styles.xml` — an `AppTheme.Base` style now owns the parent and the items; the `values-v27` variant inherits it and adds only `android:windowLightNavigationBar`. A resource qualifier replaces rather than merges a style, so the previous bare `AppTheme` in `values-v27` had dropped the parent, the colours, the background and the font on API 27+.
* `upload-and-branch-protection.md` — the required check is `Lint, test, and build debug APK`, not `Lint, test, and build debug APK / build`.
* `test-plan.md`, `performance-test-plan.md` — the report store is Room, not SharedPreferences; SharedPreferences holds only the session role and e-mail.
* `test-cases.csv` — `MAP-002` and `CI-001` re-quoted; both parsed to 11 and 12 fields before.
* `test-data.csv` — demo accounts replaced with the two roles the app actually has (`student@um.edu.my`, `admin@um.edu.my`, shared password `RainGuard123!`).

**How it was checked**

* The two workflow fixes were run for real on PR #8: the unchanged workflow failed at `Set up Android SDK` (`Failed to find package 'tools'`), the fixed one passed every step and uploaded a 5.9 MB `app-debug-apk`.
* `test-cases.csv` re-parsed with a CSV reader: every row now has nine fields.
* The theme change was reasoned about, not executed: the rule that a qualifier entry replaces the base style is the same rule that made the earlier version wrong.

**What stayed human**

* Whether to document demo credentials at all, and whether to keep `AppTheme` on the platform Material parent instead of migrating to Material3, are the author's calls.
* The QA corrections were proposed by the AI; the author accepts or rejects them.

**Follow-ups**

* Run the app on the API 37 emulator: confirm the theme keeps its background, colours and `NoActionBar` parent, and that the navigation bar still renders light.
* `docs/activity-lifecycle-plan.md` still documents the removed `onBackPressed()` override (PR #7 replaced it with `OnBackPressedDispatcher`); that needs its own documentation pass.

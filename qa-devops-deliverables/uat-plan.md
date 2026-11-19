# User acceptance test plan

## Participants and consent

- Recruit at least five UM students/staff who represent intended campus users.
- Obtain informed consent before recording observations. Explain purpose, what will be recorded, retention, and withdrawal option.
- Use synthetic reports and test accounts. Do not collect passwords or unnecessary personal information.

## Session setup

- Record app commit/build, device/emulator model, Android API, date, and facilitator.
- Give each participant a unique non-identifying participant code (P01–P05).
- Ask participants to think aloud; facilitator must not lead them to the answer.

## Task script

1. Sign in using the assigned test role.
2. Find the hazard map and identify a high-risk report.
3. Submit a report with a valid description and optional photo.
4. Attempt submission with a missing/short description and describe the feedback.
5. Find the submitted report in My Reports and inspect its status/history.
6. Review a notification or alert and explain what action it suggests.
7. For moderator/admin sessions only: verify/reject a report or assign a maintenance task as authorized.

## Feedback form

For each task record: completed without help (Y/N), completion time, errors/help needed, confidence (1–5), and participant comments. End with overall ease (1–5), confusing labels, trust/privacy concerns, and one suggested improvement.

## Success criteria

- At least 4 of 5 participants complete each campus-user task without facilitator help.
- No participant can view another user's private reporter identity.
- Moderator/admin actions are understood and performed only in the matching role session.
- Median ease rating is at least 4/5.
- Any privacy, authorization, data-loss, or crash issue is a release blocker regardless of average score.

## Recording and reporting

Use `templates/uat-session.csv` with participant codes only. Store consent forms separately with restricted access; never put them in the repository or PR. Summarize aggregate counts and anonymized quotes in the PR evidence. UAT has not yet been run; at least five participants and consent are required.

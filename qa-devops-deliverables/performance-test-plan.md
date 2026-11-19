# Performance test plan

## Targets

- Map/report API response time: p95 under 3 seconds at 100 concurrent virtual users.
- Mobile screen render time: measure separately from API time; use 3 seconds p95 as a provisional target until the product owner approves a device-specific NFR.
- Record p50/p95/p99, request/error rate, throughput, cold/warm cache state, and dataset size.

## API load test with k6

1. Use a non-production staging API and synthetic accounts/data only.
2. Configure `BASE_URL` and a short-lived test token through local environment variables or GitHub Actions secrets; never commit them.
3. Exercise map/report list and detail endpoints with read-only requests. Do not load-test production or mutate real reports.
4. Run a gradual ramp to 100 VUs, hold for 5 minutes, then ramp down. Record the exact script revision and run timestamp.
5. Pass when p95 is below 3 seconds and the agreed error-rate threshold is met; product owner must set the error-rate threshold before sign-off.

Example command after an approved endpoint and script exist:

```sh
k6 run --vus 100 --duration 5m -e BASE_URL="$BASE_URL" -e API_TOKEN="$API_TOKEN" scripts/map-report-load.js
```

The repository currently has no backend/API endpoint or k6 script, so this plan is not an executed load test.

## Mobile screen timing

- Device: record emulator model, API level, host CPU/RAM, emulator graphics mode, app commit/build variant, and dataset size.
- Measure cold launch, warm launch, map first render, and report list first render as separate timings.
- Use Android Studio Profiler / system trace and repeat at least 30 times per scenario; report p50 and p95.
- Do not combine API latency with UI render latency. Capture network timing separately if the app gains a backend.

## Limitations

The current project seeds local demo reports and persists them in SharedPreferences. It does not expose a production-like API, so the 100-VU API target cannot be validated against this repository alone. Emulator timings are not representative of physical UM devices or real mobile networks.

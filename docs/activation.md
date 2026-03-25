# NomadPlan Activation

## Overview

NomadPlan now uses an offline activation flow:

- production activations use a machine-bound Ed25519-signed token
- developer overrides use a separate Ed25519-signed developer serial
- the private signing keys stay outside the app

The desktop app ships only the public verification keys.

## Runtime

Production activation is enforced before welcome/document load.

Developer override is available only when the app is started with:

```powershell
java -Dnomadplan.devActivation=true -jar projectlibre_build\packages\nomadplan-1.9.8.jar
```

or the equivalent classpath launch.

## Stored state

Activation state is stored at:

```text
~/.projectlibre/activation/activation.json
```

## Generator tool

Use the external helper:

```powershell
java scripts/NomadPlanActivationTool.java generate-token `
  --private-key C:\path\to\production-private.pem `
  --serial CUST-001 `
  --installation-id ABCD-EF12-3456-7890 `
  --expires 2026-12-31T23:59:59Z `
  --edition standalone `
  --licensee "Customer Name"
```

```powershell
java scripts/NomadPlanActivationTool.java generate-dev-serial `
  --private-key C:\path\to\developer-private.pem `
  --serial-id local-dev
```

Notes:

- `--installation-id` and `--installation-hash` are interchangeable; the tool normalizes either into the hash format expected by the app.
- production tokens are emitted as `NMP1.<payload>.<signature>`
- developer serials are emitted as `DEV1.<serialId>.<signature>`

## App flow

1. Start the app.
2. Copy the `Installation ID` from the Activation dialog.
3. Generate a token or developer serial externally.
4. Paste the serial and activation code into the dialog.
5. The app stores the validated state locally.

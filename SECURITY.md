# Security Policy

## Supported version

Security fixes are applied to the latest release on the default branch.

## Reporting a vulnerability

Please do not open a public issue for a suspected vulnerability.

Use the repository's Security tab to submit a private vulnerability report. Include:

- affected endpoint or component;
- reproduction steps;
- expected and observed behavior;
- potential impact;
- any suggested remediation.

Secrets found in the repository should be considered compromised and rotated before history cleanup.

## Security expectations

- Never commit database passwords, JWT secrets, tokens, or private keys.
- Use environment variables or a managed secret store.
- Keep JWT signing secrets at least 32 random bytes.
- Run the application with a least-privileged database account.
- Review Dependabot and CodeQL alerts before each release.

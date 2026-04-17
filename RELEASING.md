
## Automated release process (recommended)

Releases are performed through the **Release** GitHub Actions workflow, which is
triggered manually from the GitHub Actions UI.  The workflow:

1. Sets the POM version to the chosen release version.
2. Updates the `project.build.outputTimestamp` property (reproducible builds).
3. Builds and GPG-signs the artifacts, then uploads them to the
   [Central Publishing Portal](https://central.sonatype.com/).
4. Commits the release version and creates a `vX.Y.Z` git tag.
5. Bumps the POM version to the next development SNAPSHOT on `master`.
6. Pushes the commits and tag, then creates a GitHub release.

### One-time repository setup

#### GPG signing key

You do **not** need to upload your personal GPG key.  It is strongly recommended
to create a dedicated signing key whose sole purpose is signing Jaxen releases:

```
# Create a new key (select "RSA and RSA" (option 1), 4096 bits, no expiry, any name/email)
gpg --full-generate-key

# The output will show a "pub" block and a "sub" block, each followed by a
# long hex fingerprint.  Copy the fingerprint from the "pub" line (the primary
# key, not the "sub" subkey) — it is 40 hex characters and can be used as
# <KEY_ID> in the commands below.
#
# Publish the public key to a keyserver so the Central Portal can verify it:
gpg --keyserver keys.openpgp.org --send-keys <KEY_ID>
```

The Central Publishing Portal verifies artifact signatures by looking up the
signing key on public keyservers.  Uploading to `keys.openpgp.org` is
sufficient — no additional registration of the key with Sonatype is required.
Allow a few minutes for the key to propagate before running your first release.

#### GitHub Personal Access Token (`RELEASE_TOKEN`)

A GitHub Personal Access Token (PAT) with the **Contents: write** permission is
needed when `master` is a protected branch, so the workflow can push the
post-release version-bump commit and the `vX.Y.Z` tag directly.  If the branch
is *not* protected this secret can be omitted.

**Fine-grained PAT (recommended)**

1. Go to **GitHub → Settings → Developer settings →
   [Personal access tokens → Fine-grained tokens](https://github.com/settings/tokens?type=beta)**.
2. Click **Generate new token**.
3. Set a meaningful **Token name** (e.g. `jaxen-release`).
4. Under **Repository access** choose **Only select repositories** and pick
   `jaxen-xpath/jaxen`.
5. Under **Permissions → Repository permissions** set **Contents** to
   **Read and write**.
6. Click **Generate token** and copy the value immediately — it is shown only
   once.

**Classic PAT (alternative)**

1. Go to **GitHub → Settings → Developer settings →
   [Personal access tokens → Tokens (classic)](https://github.com/settings/tokens)**.
2. Click **Generate new token (classic)**.
3. Give it a descriptive **Note** (e.g. `jaxen-release`).
4. Select the **`repo`** scope (which includes `Contents: write`).
5. Click **Generate token** and copy the value immediately.

Store the token as the `RELEASE_TOKEN` secret (see the table below).

#### Central Portal credentials

To publish artifacts you need a token from the
[Central Publishing Portal](https://central.sonatype.com/).

To generate a token:
1. Log in to [central.sonatype.com](https://central.sonatype.com/).
2. Click your username (top-right) → **View Account**.
3. Select **Generate User Token**.
4. Copy the *username* and *password* values shown.

Use the token username as `CENTRAL_USERNAME` and the token password as
`CENTRAL_TOKEN` in the secrets below.

#### Security of GitHub Actions secrets

GitHub Actions secrets are encrypted at rest using libsodium sealed boxes and
are never printed in workflow logs — GitHub automatically redacts any value that
matches a stored secret.  Secrets are only injected into the workflow process
environment; they are never written to disk and are not accessible to forked
repositories.  The blast radius of a compromised secret is further limited by the
fact that only users with **write access** can trigger this workflow.

If you suspect a secret has been exposed, revoke and replace it:
* **Central Portal token** — generate a new token in the
  [Central Publishing Portal](https://central.sonatype.com/) and update the secret.
* **GPG key** — revoke the key on the keyservers and generate a replacement.
* **RELEASE_TOKEN** — delete and regenerate the GitHub PAT.

#### Repository secrets

Before the workflow can run you must add the following **repository** secrets in
[**Settings → Secrets and variables → Actions → Repository secrets**](https://github.com/jaxen-xpath/jaxen/settings/secrets/actions).

Export the GPG private key now so you can paste it immediately into the secret:

```
gpg --armor --export-secret-keys <KEY_ID>
```

Copy the full output (including the `-----BEGIN PGP PRIVATE KEY BLOCK-----`
header and footer) as the value of the `GPG_PRIVATE_KEY` secret in the table
below.

| Secret name        | Description |
|--------------------|-------------|
| `GPG_PRIVATE_KEY`  | ASCII-armoured private key of the **dedicated** release signing key (see above) |
| `GPG_PASSPHRASE`   | Passphrase for that signing key |
| `CENTRAL_USERNAME` | Central Publishing Portal **User Token** username (see above — not your account username) |
| `CENTRAL_TOKEN`    | Central Publishing Portal **User Token** password (see above — not your account password) |
| `RELEASE_TOKEN`    | GitHub Personal Access Token with `Contents: write` scope; required when `master` is a protected branch so the workflow can push the post-release version-bump commit directly. Omit if the branch is not protected. |

### Running a release

Only GitHub users with **write access** (or higher) to this repository can
trigger `workflow_dispatch` workflows.  Repository owners and admins always
qualify; add outside collaborators as needed before attempting a release.

1. Go to [**Actions → Release → Run workflow**](https://github.com/jaxen-xpath/jaxen/actions/workflows/release.yml) on GitHub.
2. Fill in the two inputs:
   * **Version to release** – the version being released, e.g. `2.0.1`
   * **Next development version** – the next SNAPSHOT version, e.g. `2.0.2-SNAPSHOT`
3. Click **Run workflow**.

The workflow uploads the artifacts to the
[Central Publishing Portal](https://central.sonatype.com/).  Once it completes
successfully, log in to [central.sonatype.com](https://central.sonatype.com/)
and verify the deployment, then publish it.

Once the binary is available on Maven Central, regenerate the project site:

```
mvn site:stage
```

Upload the generated content to IBiblio as before.

---

## Version conventions

* **`master`** always carries a SNAPSHOT version representing the *next*
  planned release (e.g. `2.0.2-SNAPSHOT`).
* **Tags** (`vX.Y.Z`) mark released commits and contain the bare release version.
* There are no long-lived release branches; the tag is the permanent record.

---

## Manual release process (fallback)

If the automated workflow cannot be used, you will need:

* GPG installed with credentials configured.
* Permissions on the [Central Publishing Portal](https://central.sonatype.com/)
  to publish jaxen.

```
$ export GPG_TTY=$(tty)
$ git checkout master
$ git pull
# Set the release version (e.g. 2.0.1) in all pom.xml files and update
# project.build.outputTimestamp to the current UTC time in pom.xml, then commit.
$ mvn install -Prelease
$ mvn deploy -Prelease
```

Log in to [central.sonatype.com](https://central.sonatype.com/) and publish the
deployment.

Tag the release commit:

```
$ git tag -a vX.Y.Z -m "Release X.Y.Z"
$ git push origin vX.Y.Z
```

Bump `master` to the next SNAPSHOT version in all pom.xml files and push.

Create a [GitHub release](https://github.com/jaxen-xpath/jaxen/releases/new) in the form `vX.Y.Z`.

Once the binary is available on Maven Central, run `mvn site:stage` and upload
the generated content to IBiblio.


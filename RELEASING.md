
## Automated release process (recommended)

Releases are performed through the **Release** GitHub Actions workflow, which is
triggered manually from the GitHub Actions UI.  The workflow:

1. Sets the POM version to the chosen release version.
2. Updates the `project.build.outputTimestamp` property (reproducible builds).
3. Updates `README.md` and the `index.xml` site pages with the new release version
   (dependency snippets and current-version text).
4. Builds and GPG-signs the artifacts, then uploads them to the
   [Central Publishing Portal](https://central.sonatype.com/).
5. Commits the release version and creates a `vX.Y.Z` git tag.
6. Bumps the POM version to the next development SNAPSHOT.
7. Pushes the tag and a release branch, opens a pull request targeting `master`,
   then creates a GitHub release with attached
   release archives:
   * `core/target/jaxen-X.Y.Z-bin.zip`
   * `core/target/jaxen-X.Y.Z-bin.tar.gz`
   * `core/target/jaxen-X.Y.Z-bin.tar.bz2`
   * `core/target/jaxen-X.Y.Z-src.zip`
   * `core/target/jaxen-X.Y.Z-src.tar.gz`
   * `core/target/jaxen-X.Y.Z-src.tar.bz2`
   * `core/target/jaxen-X.Y.Z-core-src.zip`

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

### Running a release

Only GitHub users with **write access** (or higher) to this repository can
trigger `workflow_dispatch` workflows.  Repository owners and admins always
qualify; add outside collaborators as needed before attempting a release.

1. Go to [**Actions → Release**](https://github.com/jaxen-xpath/jaxen/actions/workflows/release.yml) on GitHub.
2. Click the **Run workflow** dropdown (top-right of the workflow runs list).
3. Ensure **Branch: master** is selected.
4. Fill in the two inputs:
   * **Version to release** – the version being released, e.g. `2.0.1`
   * **Next development version** – the next SNAPSHOT version, e.g. `2.0.2-SNAPSHOT`
5. Click the green **Run workflow** button.

The workflow uploads the artifacts to the
[Central Publishing Portal](https://central.sonatype.com/).  Once it completes
successfully, log in to [central.sonatype.com](https://central.sonatype.com/)
and verify the deployment, then publish it.

When the workflow reaches the git handoff stage, it pushes the release tag and
opens a pull request from `release/X.Y.Z` into `master` with the release and
post-release version-bump commits. Merge that PR to update `master`.

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
# Set the release version (e.g. 2.0.1) in all pom.xml files, update
# project.build.outputTimestamp to the current UTC time in pom.xml, and update
# README.md and the index.xml site pages with the new version, then commit.
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

## Publish the Site

Update the release notes on the GitHub tag and in src/site/xdoc/releases.xml
and src/site/xdoc/status.xml. In `releases.xml`, use GitHub release asset
URLs in the form:

`https://github.com/jaxen-xpath/jaxen/releases/download/vX.Y.Z/jaxen-X.Y.Z-<artifact>`

The GitHub release will prepopulate with a list of PR titles, but you'll 
usually want to summarize the important points manually.

Regenerate the project site:

```
mvn site:stage
```

Upload the generated content to IBiblio using sftp.



## Automated release process (recommended)

Releases are performed through the **Release** GitHub Actions workflow, which is
triggered manually from the GitHub Actions UI.  The workflow:

1. Sets the POM version to the chosen release version.
2. Updates the `project.build.outputTimestamp` property (reproducible builds).
3. Builds and GPG-signs the artifacts, then stages them to Sonatype OSSRH.
4. Commits the release version and creates a `vX.Y.Z` git tag.
5. Bumps the POM version to the next development SNAPSHOT on `master`.
6. Pushes the commits and tag, then creates a GitHub release.

### One-time repository setup

#### GPG signing key

You do **not** need to upload your personal GPG key.  It is strongly recommended
to create a dedicated signing key whose sole purpose is signing Jaxen releases:

```
# Create a new key (use key type RSA, 4096 bits, no expiry, any name/email)
gpg --full-generate-key

# Note the 16-hex-char key ID shown at the end of the output, then publish
# the public key to multiple keyservers so Sonatype OSSRH can verify it:
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>
gpg --keyserver keys.openpgp.org     --send-keys <KEY_ID>

# Export the private key in ASCII-armour form to store as a secret
gpg --armor --export-secret-keys <KEY_ID>
```

Copy the full output (including the `-----BEGIN PGP PRIVATE KEY BLOCK-----`
header and footer) as the value of the `GPG_PRIVATE_KEY` secret below.

Sonatype OSSRH verifies artifact signatures by looking up the signing key on
public keyservers.  Uploading to `keyserver.ubuntu.com` and `keys.openpgp.org`
is sufficient — no additional registration of the key with Sonatype is required.
Allow a few minutes for the key to propagate before running your first release.

#### Repository secrets

Before the workflow can run you must add the following secrets in
**Settings → Secrets and variables → Actions**:

| Secret name       | Description |
|-------------------|-------------|
| `GPG_PRIVATE_KEY` | ASCII-armoured private key of the **dedicated** release signing key (see above) |
| `GPG_PASSPHRASE`  | Passphrase for that signing key |
| `OSSRH_USERNAME`  | Sonatype OSSRH username |
| `OSSRH_TOKEN`     | Sonatype OSSRH token (or password) |
| `RELEASE_TOKEN`   | GitHub Personal Access Token with `Contents: write` scope; required when `master` is a protected branch so the workflow can push the post-release version-bump commit directly. Omit if the branch is not protected. |

### Running a release

Only GitHub users with **write access** (or higher) to this repository can
trigger `workflow_dispatch` workflows.  Repository owners and admins always
qualify; add outside collaborators as needed before attempting a release.

1. Go to **Actions → Release → Run workflow** on GitHub.
2. Fill in the two inputs:
   * **Version to release** – the version being released, e.g. `2.0.1`
   * **Next development version** – the next SNAPSHOT version, e.g. `2.0.2-SNAPSHOT`
3. Click **Run workflow**.

The workflow stages the artifacts to Sonatype OSSRH.  Once it completes
successfully, log in to [OSSRH](https://oss.sonatype.org/#welcome) and release
(close + release) the staging repository.

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
* Permissions on Sonatype OSSRH to release jaxen.

```
$ export GPG_TTY=$(tty)
$ git checkout master
$ git pull
# Set the release version (e.g. 2.0.1) in all pom.xml files and update
# project.build.outputTimestamp to the current UTC time in pom.xml, then commit.
$ mvn install -Prelease
$ mvn deploy -Prelease -DskipRemoteStaging -DaltStagingDirectory=/tmp/jaxen-deploy -Dmaven.install.skip
$ mvn deploy -Prelease -DaltStagingDirectory=/tmp/jaxen-deploy -Dmaven.install.skip
```

Log in to [OSSRH](https://oss.sonatype.org/#welcome) and release the repository.

Tag the release commit:

```
$ git tag -a vX.Y.Z -m "Release X.Y.Z"
$ git push origin vX.Y.Z
```

Bump `master` to the next SNAPSHOT version in all pom.xml files and push.

Create a GitHub release in the form `vX.Y.Z`.

Once the binary is available on Maven Central, run `mvn site:stage` and upload
the generated content to IBiblio.


Jaxen hasn't been released since Codehaus went away. A new release process that goes straight to Maven Central is in development, but has not yet been fully debugged. For now these are incomplete notes.

* You will need to install GPG and set up GPG credentials
* You will need permissions on Sonatype to release jaxen.

Send a PR removing the SNAPSHOT from the version. Do not merge this yet.

In the jaxen directory:

```
$ export GPG_TTY=$(tty)
$ git checkout master
$ git pull
$ mvn install -Prelease 
_Enter your GPG password when prompted_
$ mvn deploy -Prelease -DskipRemoteStaging -DaltStagingDirectory=/tmp/jaxen-deploy -Dmaven.install.skip
$ mvn deploy -Prelease -DaltStagingDirectory=/tmp/jaxen-deploy -Dmaven.install.skip
```

If that doesn't work, try 

```
$ mvn install -Prelease -DskipTests -Dadditionalparam="-Xdoclint:none"
_Enter your GPG password when prompted_
$ mvn deploy -Prelease -DskipRemoteStaging -DskipTests -Dadditionalparam="-Xdoclint:none" -DaltStagingDirectory=/tmp/jaxen-deploy -Dmaven.install.skip
$ mvn deploy -Prelease -DskipTests -Dadditionalparam="-Xdoclint:none" -DaltStagingDirectory=/tmp/jaxen-deploy -Dmaven.install.skip
```

Then login to [https://oss.sonatype.org/#stagingRepositories](https://oss.sonatype.org/#stagingRepositories) and release the repository. 

Now merge the version PR on Github.

Create a release on Github in the form v1.2.0. 

Once the binary is available on Maven Central, run `mvn:site` and upload the generated content to IBiblio. 
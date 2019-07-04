
* You will need to install GPG and set up GPG credentials
* You will need permissions on Sonatype OSSRH to release jaxen.

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

Once that's done, login to [OSSRH](https://oss.sonatype.org/#welcome) and release the repository. 

Now merge the version PR on Github.

Create a release on Github in the form v2.0.0. 

Once the binary is available on Maven Central, run `mvn site:stage` and upload the generated content to IBiblio. 


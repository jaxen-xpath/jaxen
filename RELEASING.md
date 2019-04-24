Warning: some details may be incomplete:

* You will need to install GPG and set up GPG credentials
* You will need permissions on Sonatype OSSRH to release jaxen.

Tagging a release. 

How to push a snapshot.


How to push a release.

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

Once the binary is available on Maven Central, run `mvn:site` and upload the generated content to IBiblio. 

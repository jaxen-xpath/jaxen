Jaxen hasn't been released since Codehaus went away. A new release process that goes straight to Maven Central is in development but has not yet been tested. For now these are just notes that may or may not work.

* You will need to install GPG and set up GPG credentials
* You will need permissions on Sonatype to release jaxen.

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

If that doesn't work, try 

```
$ mvn install -Prelease -DskipTests -Dadditionalparam="-Xdoclint:none"
_Enter your GPG password when prompted_
$ mvn deploy -Prelease -DskipRemoteStaging -DskipTests -Dadditionalparam="-Xdoclint:none" -DaltStagingDirectory=/tmp/jaxen-deploy -Dmaven.install.skip
$ mvn deploy -Prelease -DskipTests -Dadditionalparam="-Xdoclint:none" -DaltStagingDirectory=/tmp/jaxen-deploy -Dmaven.install.skip
```

Once the binary is available on Maven Central, run `mvn:site` and upload the generated content to IBiblio. 
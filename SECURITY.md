# Security Policy

## Reporting Security Issues

If it's a real issue you've personally discovered and can explain, feel free to drop me an email.

If it's some security tool logging a warning, that is 95% likely not to be a security issue but rather a bug in the tool. You can file that here after you have investigated if you are willing to vouch that it is a true security issue, but be aware that these tools are almost never correct when analyzing Jaxen.

## What is **NOT** a Security Bug in Jaxen

1. Anything in your dependency tree whose source code is not in this repo. You control your classpath. Jaxen doesn't. If you don't like what's in the classpath, change it.
2. Properly implementing XML 1.0 according to the specification.
3. Properly implementing XPath 1.0 according to the specification.
4. Being able to load a URL from Java code.

## Probably Not Security Bugs in Jaxen

* Problems that only appear when your code (not Jaxen's) accepts untrusted, unvalidated user input

## Possible Security Bugs in Jaxen

If you can find one, none are currently known to exist:

* XPath expressions that cause infinite loops in the parser or exponential performance problems.
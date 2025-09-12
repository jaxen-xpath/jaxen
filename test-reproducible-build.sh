#!/bin/bash
# Test script to verify reproducible builds
# This script builds the project twice and compares the resulting artifacts

set -e

echo "Testing reproducible builds for Jaxen..."

# Set Java environment
export JAVA_HOME=/usr/lib/jvm/temurin-8-jdk-amd64

# Clean and build first time
echo "Building first time..."
mvn clean package -Dmaven.javadoc.skip=true -q
mkdir -p /tmp/build1
cp core/target/jaxen-2.0.0.jar /tmp/build1/
cp core/target/jaxen-2.0.0-sources.jar /tmp/build1/
if [ -f integration-tests/target/jaxen-integration-2.0.0.jar ]; then
    cp integration-tests/target/jaxen-integration-2.0.0.jar /tmp/build1/
fi

# Wait a moment to ensure different timestamps if not reproducible
sleep 2

# Clean and build second time
echo "Building second time..."
mvn clean package -Dmaven.javadoc.skip=true -q
mkdir -p /tmp/build2
cp core/target/jaxen-2.0.0.jar /tmp/build2/
cp core/target/jaxen-2.0.0-sources.jar /tmp/build2/
if [ -f integration-tests/target/jaxen-integration-2.0.0.jar ]; then
    cp integration-tests/target/jaxen-integration-2.0.0.jar /tmp/build2/
fi

# Compare artifacts
echo "Comparing artifacts..."
FAILED=0

for jar in jaxen-2.0.0.jar jaxen-2.0.0-sources.jar jaxen-integration-2.0.0.jar; do
    if [ -f "/tmp/build1/$jar" ] && [ -f "/tmp/build2/$jar" ]; then
        if diff "/tmp/build1/$jar" "/tmp/build2/$jar" > /dev/null; then
            echo "✓ $jar: identical"
        else
            echo "✗ $jar: different"
            FAILED=1
        fi
    fi
done

# Clean up
rm -rf /tmp/build1 /tmp/build2

if [ $FAILED -eq 0 ]; then
    echo ""
    echo "✅ All builds are reproducible!"
    exit 0
else
    echo ""
    echo "❌ Builds are not reproducible!"
    exit 1
fi
#!/bin/bash
# Verify that Jaxen builds are reproducible
# This script builds the project twice and compares the resulting artifacts

set -e

echo "Verifying Jaxen reproducible builds..."

# Ensure Java 8 is used
export JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/temurin-8-jdk-amd64}

# Create temporary directories for builds
BUILD_DIR1=$(mktemp -d)
BUILD_DIR2=$(mktemp -d)

# Copy source to both directories
cp -r . "$BUILD_DIR1/"
cp -r . "$BUILD_DIR2/"

echo "Building first time in $BUILD_DIR1..."
cd "$BUILD_DIR1"
mvn clean package -Dmaven.javadoc.skip=true -q

echo "Building second time in $BUILD_DIR2..."
cd "$BUILD_DIR2"
mvn clean package -Dmaven.javadoc.skip=true -q

echo "Comparing artifacts..."
HASH1=$(sha256sum "$BUILD_DIR1/core/target/jaxen-2.0.0.jar" | cut -d' ' -f1)
HASH2=$(sha256sum "$BUILD_DIR2/core/target/jaxen-2.0.0.jar" | cut -d' ' -f1)

echo "First build hash:  $HASH1"
echo "Second build hash: $HASH2"

if [ "$HASH1" = "$HASH2" ]; then
    echo "✓ SUCCESS: Builds are reproducible!"
    exit 0
else
    echo "✗ FAILURE: Builds are not reproducible!"
    exit 1
fi

# Cleanup
rm -rf "$BUILD_DIR1" "$BUILD_DIR2"
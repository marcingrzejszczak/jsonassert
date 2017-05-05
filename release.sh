#!/usr/bin/env bash
git fetch --tags && ./gradlew clean build && ./gradlew createRelease && ./gradlew uploadArchives && ./gradlew closeAndReleaseRepository && git push origin master --tags
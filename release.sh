#!/usr/bin/env bash
git fetch && ./gradlew clean build && ./gradlew createRelease && ./gradlew uploadArchives && ./gradlew closeRepository && ./gradlew promoteRepository && ./gradlew pushRelease
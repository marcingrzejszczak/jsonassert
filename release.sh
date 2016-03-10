#!/usr/bin/env bash
./gradlew clean build && ./gradlew createRelease && ./gradlew uploadArchives && ./gradlew closeRepository && ./gradlew promoteRepository && ./gradlew pushRelease
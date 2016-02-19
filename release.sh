#!/usr/bin/env bash
./gradlew createRelease
./gradlew uploadArchives
./gradlew closeRepository
./gradlew promoteRepository
./gradlew pushRelease
#!/usr/bin/env bash

set -o errexit

export DEV_VERSION="${DEV_VERSION:?You must set the next dev version}"
export RELEASE_VERSION="${RELEASE_VERSION:?You must set the next release version}"

echo "[RELEASE] Will release [${RELEASE_VERSION}] and then bump to dev version [${DEV_VERSION}]"

echo "[RELEASE] Fetching tags..."
git fetch --tags

echo "[RELEASE] Preparing for the release..."
./mvnw -B -Dtag=v"${RELEASE_VERSION}" release:clean release:prepare -DreleaseVersion="${RELEASE_VERSION}" -DdevelopmentVersion="${DEV_VERSION}"

echo "[RELEASE] Doing the actual release..."
  ./mvnw -B -Dtag=v"${RELEASE_VERSION}" -DreleaseVersion="${RELEASE_VERSION}" -DdevelopmentVersion="${DEV_VERSION}" -Dgoals=deploy release:perform -Pcentral

echo "[RELEASE] Pushing tags..."
git push origin main --tags
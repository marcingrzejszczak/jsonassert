#!/usr/bin/env bash

set -o errexit

export DEV_VERSION="${DEV_VERSION:?You must set the next dev version}"
export RELEASE_VERSION="${RELEASE_VERSION:?You must set the next release version}"

echo "Will release [${RELEASE_VERSION}] and then bump to dev version [${DEV_VERSION}]"
git fetch --tags && \
  ./mvnw -B -Dtag=v"${RELEASE_VERSION}" release:clean release:prepare -DreleaseVersion="${RELEASE_VERSION}" -DdevelopmentVersion="${DEV_VERSION}" && \
  ./mvnw -B -Dtag=v"${RELEASE_VERSION}" -DreleaseVersion="${RELEASE_VERSION}" -DdevelopmentVersion="${DEV_VERSION}" -Dgoals=deploy release:perform && \
  git push origin master --tags
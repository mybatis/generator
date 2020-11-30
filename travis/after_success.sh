#!/bin/bash

# Get Commit Message
commit_message=$(git log --format=%B -n 1)
echo "Current commit detected: ${commit_message}"

# We build for several JDKs on Travis.
# Some actions, like analyzing the code (Coveralls) and uploading
# artifacts on a Maven repository, should only be made for one version.
 

# If the version is 1.8, then perform the following actions.
# 1. Notify Coveralls.
# 2. Deploy site (disabled as solution not complete).

# Parameters
# 1. Use -q option to only display Maven errors and warnings.
# 2. Use --settings to force the usage of our "settings.xml" file.

if [ $TRAVIS_REPO_SLUG == "mybatis/generator" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ] && [[ "$commit_message" != *"[maven-release-plugin]"* ]]; then

  if [ $TRAVIS_JDK_VERSION == "openjdk8" ]; then

    # Deploy to Coveralls
    ./mvnw clean test jacoco:report coveralls:report -q --settings ../mvn/settings.xml
    echo -e "Successfully ran coveralls under Travis job ${TRAVIS_JOB_NUMBER}"

    # Deploy to site
    # various issues exist currently in building this so comment for now
    # ./mvnw site site:deploy -q --settings ../mvn/settings.xml
    # echo -e "Successfully deploy site under Travis job ${TRAVIS_JOB_NUMBER}"
  else
    echo "Java Version does not support additonal activity for travis CI"
  fi
else
  echo "Travis Pull Request: $TRAVIS_PULL_REQUEST"
  echo "Travis Branch: $TRAVIS_BRANCH"
  echo "Travis build skipped"
fi

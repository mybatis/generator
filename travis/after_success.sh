#!/bin/bash

# Get Project Repo
mybatis_repo=$(git config --get remote.origin.url 2>&1)
echo "Repo detected: ${mybatis_repo}"

# Get Commit Message
commit_message=$(git log --format=%B -n 1)
echo "Current commit detected: ${commit_message}"

# Get the Java version.
# Java 1.5 will give 15.
# Java 1.6 will give 16.
# Java 1.7 will give 17.
# Java 1.8 will give 18.
VER=`java -version 2>&1 | sed 's/java version "\(.*\)\.\(.*\)\..*"/\1\2/; 1q'`
echo "Java detected: ${VER}"

# We build for several JDKs on Travis.
# Some actions, like analyzing the code (Coveralls) and uploading
# artifacts on a Maven repository, should only be made for one version.
 
# If the version is 1.8, then perform the following actions.
# 1. Upload artifacts to Sonatype.
#    a. Use -q option to only display Maven errors and warnings.
#    b. Use --settings to force the usage of our "settings.xml" file.
# 2. Notify Coveralls.
#    a. Use -q option to only display Maven errors and warnings.
# 3. Deploy site (disabled)
#    a. Use -q option to only display Maven errors and warnings.

if [ "$mybatis_repo" == "https://github.com/mybatis/generator.git" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ] && [[ "$commit_message" != *"[maven-release-plugin]"* ]]; then
  if [ $VER == "18" ]; then
    # Deploy to Sonatype
    mvn clean deploy -q --settings ../travis/settings.xml
    echo -e "Successfully deployed SNAPSHOT artifacts to Sonatype under Travis job ${TRAVIS_JOB_NUMBER}"

	# Deploy to Coveralls
    mvn clean test jacoco:report coveralls:report -q
    echo -e "Successfully ran coveralls under Travis job ${TRAVIS_JOB_NUMBER}"

	# Deploy to site
	# various issues exist currently in building this so comment for now
	# mvn site site:deploy -q
	# echo -e "Successfully deploy site under Travis job ${TRAVIS_JOB_NUMBER}"
  fi
else
  echo "Travis build skipped"
fi
#!/usr/bin/env sh

set -e

find . -name pom.xml -print0 | xargs -0 sed -i -E \
-e "s:<revision>dev-SNAPSHOT</revision>:<revision>${REVISION}</revision>:g" \
-e "s:<version>dev-SNAPSHOT</version>:<version>${ACROSS_FRAMEWORK_VERSION}</version>:g" \
-e "s:<across-framework.version>dev-SNAPSHOT</across-framework.version>:<across-framework.version>${ACROSS_FRAMEWORK_VERSION}</across-framework.version>:g" \

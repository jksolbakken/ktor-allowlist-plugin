#!/usr/bin/env bash

group="no/jksolbakken"
artifact="ktor-allowlist-plugin"
dir="$GITHUB_WORKSPACE/build/bundle/$group/$artifact/$version"

if [[ -n $VERSION ]]; then
  version=$VERSION
else
  version="notimportant"
fi

rm -rf $dir
mkdir -p $dir

cp $GITHUB_WORKSPACE/build/libs/$artifact*.jar $dir
cp $GITHUB_WORKSPACE/build/publications/maven/pom-default.xml $dir/$artifact-$version.pom

for f in $(ls $dir); do
  md5sum --quiet $dir/$f >$dir/$f.md5
  sha1sum --quiet $dir/$f >$dir/$f.sha1
done

cd $GITHUB_WORKSPACE/build/bundle && zip -r mvnbundle.zip *


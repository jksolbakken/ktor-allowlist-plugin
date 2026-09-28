#!/usr/bin/env bash

group="no/jksolbakken"
artifact="ktor-allowlist-plugin"
version="0.1.0"
dir="../build/bundle/$group/$artifact/$version"

rm -rf $dir
mkdir -p $dir

cp ../build/libs/$artifact*.jar $dir
cp ../build/publications/maven/pom-default.xml $dir/$artifact-$version.pom

for f in $(ls $dir); do
  md5sum --quiet $dir/$f >$dir/$f.md5
  sha1sum --quiet $dir/$f >$dir/$f.sha1
done

cd ../build/bundle && zip -r mvnbundle.zip *


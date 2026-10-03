#!/usr/bin/env bash

set -eo pipefail

if [[ -n $VERSION ]]; then
  version=$VERSION
else
  version="notimportant"
fi

group="no/jksolbakken"
artifact="ktor-allowlist-plugin"
dir="$GITHUB_WORKSPACE/build/bundle/$group/$artifact/$version"

rm -rf "$dir"
mkdir -p "$dir"

cp "$GITHUB_WORKSPACE"/build/libs/$artifact*.jar $dir
cp "$GITHUB_WORKSPACE"/build/publications/maven/pom-default.xml $dir/$artifact-$version.pom

for f in ls "$dir"/*; do
  [[ -e "$f" ]] || break
  md5sum --quiet $dir/$f >$dir/$f.md5
  sha1sum --quiet $dir/$f >$dir/$f.sha1
  echo "$GPG_PASSPHRASE" | gpg --passphrase-fd 0 -v --pinentry-mode loopback -ab "$dir"/"$f"
done

cd "$GITHUB_WORKSPACE"/build/bundle && zip -r mvnbundle.zip ./*

#!/bin/sh
set -eu
cd "$(dirname "$0")"
javac -encoding UTF-8 PublicPreviewServer.java
java PublicPreviewServer

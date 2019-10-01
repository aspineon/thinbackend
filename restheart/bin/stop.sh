#!/bin/bash
set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

java -server -cp $DIR/../target/restheart.jar org.restheart.Shutdowner $1
sleep 2

#!/bin/sh

#clone riot-android and checkout to develop branch
git clone -v --progress https://github.com/vector-im/riot-android riot-android
cd riot-android
git fetch origin/develop
git checkout -b develop origin/develop

#download dependencies and compile the apk
gradle clean assembleDebug

#copy it to out directory
mkdir out
cp vector/build/outputs/apk/vector-app-debug.apk out/Vector.apk

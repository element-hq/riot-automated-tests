#!/bin/sh

#clone riot-ios and checkout to develop branch
git clone -v --progress https://github.com/vector-im/riot-ios riot-ios
cd riot-ios
git fetch origin/develop
git checkout -b develop origin/develop

#chmod -R 777 ../riot-ios

./use-dev-pods.sh
../buildipa.sh clean
rm -rf out/ || true
rm Podfile.lock || true
rm -rf Pods/ || true
rm -rf Vector.xcworkspace/ || true

pod install

../buildipa.sh
mkdir out
./checkipa.sh out/Vector.ipa

#!/bin/sh

path_to_apk="buildapk/riot-android/out/Vector.apk"
path_to_ipa="buildipa/riot-ios/out/Vector.ipa"

#Get id of android and ios devices from devices.yaml
. parse_yaml.sh
eval $(parse_yaml src/test/java/config/devices.yaml "config_")
echo $config_androiddevice1__udid
echo $config_androiddevice2__udid
echo $config_iosdevice1__udid
echo $config_iosdevice2__udid

#Download, and compile riot-ios /develop .ipa
cd buildipa
./buildipaDevelopment.sh

#Download, and compile riot-android /develop .apk
cd ../buildapk
./buildapkDevelopment.sh

#Remove old and install new .ipa on iOS devices
cd ..
ideviceinstaller --udid $config_iosdevice1__udid --uninstall im.vector.app
ideviceinstaller --udid $config_iosdevice1__udid --install $path_to_ipa
#ideviceinstaller --udid $config_iosdevice2__udid --uninstall im.vector.app
#ideviceinstaller --udid $config_iosdevice2__udid --install $path_to_ipa


##Remove old and install new .apk on Android devices
#TODO
adb -s $config_androiddevice1__udid uninstall im.vector.alpha
adb -s $config_androiddevice1__udid install $path_to_apk
#adb -s $config_androiddevice2__udid uninstall im.vector.alpha
#adb -s $config_androiddevice2__udid install $path_to_apk

#Launch testsuite




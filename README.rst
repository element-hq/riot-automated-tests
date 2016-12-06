Vector-automated-tests
==========

Vector/automated-tests is an automated tests project for vector mobiles applications :
- https://github.com/vector-im/vector-ios
- https://github.com/vector-im/vector-android

The goal is to make regression tests on the GUI using emulator or real devices.
In order to play the tests on the iOS client, a Mac OS environnement with XCode is needed. Mac OS can also play the tests on the Android client.
A Windows environnement will be able to play the tests on Android client only.

The automated tests are performed by using the open-source project Appium licensed under the Apache Licence, Version 2.0. Please refer to http://appium.io/ to set-up your environnement.
Tests are written in JAVA.

You can build the app from source as per below:

Build instructions
==================
TODO

Developing
==========
TODO

Install on Mac OS
=================

Test development
----------------

- Install JDK
- Install Eclipse IDE for developper
- Install TestNG Eclipse plugin (http://testng.org/doc/download.html). Usefull to launch tests from Eclipse.
- Install Maven
- Get the sources from github, then use maven install to recover the dependencies.
- If you want to explore the application under test UI in real time (mandatory to develop automated tests), launch UIAutomatorViewer in $ANDROID_HOME/tools/uiautomatorviewer

Appium Server
-------------

- Install Node.js for Mac OS (https://nodejs.org/en/download/). After installation, verify that node.js is set up by sending npm -version in terminal.
- Install Appium Server with sudo npm install -g appium. Verify that appium is installed by sending "appium" in terminal : a server with default port must start.
- Some tests support two devices, so two appium servers are needed. You can create two .sh file to open those quickly: For the first one: "appium -p 4723" and the second one: "appium -p 4724". Change the permission to make those executable "chmod 755 file.sh".

Test environnement for Android
------------------------------

- Install Android SDK (https://developer.android.com/studio/index.html?hl=sk). Make sure environnement variables are set with: "echo export "PATH=$home/Library/Android/sdk/platform-tools:${PATH}" >> ~/.bash_profile" and "echo export "PATH=$home/Library/Android/sdk/tools:${PATH}" >> ~/.bash_profile". Send "adb version" in terminal to make sure installation is correct.
- 
Test environnement for iOS
--------------------------

- Install XCode Command Line Tools
- Enable UI Automation on the device(s) under test. On the iPhone Settings -> Developer -> check Enable UI Automation
- Authorize ios for testing
npm install -g authorize-ios
sudo authorize-ios
- Install ideviceinstaller
brew install ideviceinstaller
- Install ios-deploy (not sure if necessary)
which ios-deploy
- workaround for start app issue: sudo chmod -R 777 /var/db/lockdown/


Copyright & License
==================

Copyright (c) 2014-2016 OpenMarket Ltd

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

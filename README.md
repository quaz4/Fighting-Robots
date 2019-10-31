# Fighting-Robots
A multithreaded robot fighting game written in Java and C.

Written by William Stewart (18349788) for Software Engineering Concepts (COMP3003)

## Building/Running
### Requirements
The JAVA_HOME environment variable must be first set to a valid path.
e.g. `export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/bin`.
You can check if this is set by running `echo $JAVA_HOME`.

In order for the native c code to be found, it must be added to the LD_LIBRARY_PATH.
The `/Fighting-Robots/native_ai/build/libs/nativeimp/shared` directory needs to be added to this environment variable.
e.g. `export LD_LIBRARY_PATH=/your/path/to/Fighting-Robots/native_ai/build/libs/nativeimp/shared`.

### Running
Assuming all requirements are met, simply running `./gradlew run` should build and run the application, no other steps required.

### Distribution
After running `./gradlew build` a distributable will be available in `/Fighting-Robots/main_application/build/distributions/`.

## Game Config
### Robots
The robots can be configured in the `Fighting-Robots/main_application/src/main/java/art/willstew/arena/javafx/FightingRobotsApp.java` file.

### Arena
The grid size can be configured in the `Fighting-Robots/main_application/src/main/java/art/willstew/arena/javafx/FightingRobotsApp.java` file. This can be done by changing the values on gridWith and gridHeight.

## Things to note
To ensure my app ran correctly on my local development machine, I had to specify that it used GTK2 and not GTK3. This can be seen in main_application/build.gradle
```groovy
// There is an issue with GTK 3 on my local machine, I need to run using GTK 2 in order to resolve it
// GTK 3 doesn't render the window decorations and I can't click on parts of the app
applicationDefaultJvmArgs = ["-Djdk.gtk.version=2"]
```

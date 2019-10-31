# Fighting-Robots
Written by William Stewart

## Building the app

## Running the app

### Things to note
To ensure my app ran correctly on my local development machine, I had to specify that it used GTK2 and not GTK3. This can be seen in main_application/build.gradle

```groovy
// There is an issue with GTK 3 on my local machine, I need to run using GTK 2 in order to resolve it
// GTK 3 doesn't render the window decorations and I can't click on parts of the app
applicationDefaultJvmArgs = ["-Djdk.gtk.version=2"]
```
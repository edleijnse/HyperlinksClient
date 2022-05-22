# HyperlinksClient
To make the JavaFX file runnable (e.g.for Windows) you can define a shortcut (you have to download and install the javafx sdk)
target:
"C:\Program Files\Java\jdk-13.0.2\bin\java.exe" --module-path "C:\Program Files\javafx-sdk-18.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar HyperlinksClient.main.jar
start in:
"C:\Program Files\Java\jdk-13.0.2\bin\java.exe" --module-path "C:\Program Files\javafx-sdk-18.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar HyperlinksClient.main.jar
In IntelliJ:
Open Module Settings:
Artifacts
Add .jar file
after this: build artifact.
The .jar file will appear in the "out" folder.

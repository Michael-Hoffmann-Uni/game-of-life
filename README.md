# Game of Life
## Summary
This small project is a simple implementation of [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life). It is possible to load existing worlds from file or configure own worlds. It supports a CLI and a graphical user interface working with JavaFX and follows the model-view-controller pattern.

## Configuration
The app was created for practice reasons only and is not tied to a build tool. To run the GUI from your IDE you need to have installed [JavaFX](https://openjfx.io/) and add JavaFX as a library to the project in your IDE. Depending on your IDE you might have to configure VM-options. 
#### Example for IntelliJ
- Go to: Run -> Edit Configurations... and select GameOfLifeGUI on the left hand side
- In the textbox for VM-options put: --module-path [PATH TO JAVAFX lib FOLDER] --add-modules javafx.controls,javafx.fxml
- Change the path according to you JavaFX installation
#### Run application
- To start the GUI run GameOfLife in the package src.jpp.gol.ui
- To start the CLI run GameOfLife in the package src.jpp.gol.console

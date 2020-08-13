package jpp.gol.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jpp.gol.io.StandardWorldLoader;
import jpp.gol.logic.GameLogic;
import jpp.gol.logic.ObservableGameLogicDecorator;
import jpp.gol.logic.StandardGameLogic;
import jpp.gol.model.CellState;
import jpp.gol.model.World;
import jpp.gol.rules.Rules;
import jpp.gol.rules.StandardRules;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class GameOfLife extends Application {
    private HashMap<String, CoordRectangle> rectMap = new HashMap<>();
    private boolean isRunning = false;
    private LifeRunner lifeRunner;
    private ObservableGameLogicDecorator oglDecorator;
    private World world;
    private double midBoxWidth;
    private double midBoxHeight;

    private BorderPane boarderPane = new BorderPane();
    private HBox topBox = new HBox();
    private HBox botBox = new HBox();
    private HBox midBox = new HBox();
    private Pane field = new Pane();
    private VBox frameV = new VBox();
    private HBox frameH = new HBox();
    private TextField txtWidth = new TextField("Width");
    private TextField txtHeight = new TextField("Height");
    private Button btnNew = new Button("New");
    private Button btnLoad = new Button("Load");
    private Button btnExit = new Button("Exit");
    private Button btnStart = new Button("Start");
    private Button btnPause = new Button("Pause");
    private Label lblSpace1 = new Label("                   ");
    private Label lblAbsp = new Label("Time between iterations (in ms):");
    private Button btnMinus50 = new Button("-50");
    private Label lblSpeed = new Label("500");
    private Button btnPlus50 = new Button("+50");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //blocking gui when choosing file
        final Node block = (Node) boarderPane;
        //create world
        world = new World(10, 10);
        //put world in decorator with standard logic and standard rules
        oglDecorator = new ObservableGameLogicDecorator(new StandardGameLogic(world, new StandardRules()));
        if (!isRunning)
            newField(oglDecorator);

        //------------- create topBar --------------------------
        topBox.getChildren().add(txtWidth);
        txtWidth.setPrefWidth(150);
        topBox.getChildren().add(txtHeight);
        txtHeight.setPrefWidth(150);
        topBox.getChildren().add(btnNew);
        btnNew.setPrefWidth(60);
        btnNew.setOnMouseClicked(e -> this.newWorld());
        topBox.getChildren().add(btnLoad);
        btnLoad.setPrefWidth(60);
        btnLoad.setOnMouseClicked(e -> loadFile(block));
        topBox.getChildren().add(btnExit);
        btnExit.setPrefWidth(60);
        btnExit.setOnMouseClicked(e -> this.exit(stage));
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setSpacing(20);
        topBox.setPrefHeight(50);
        topBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-padding: 20;");
        boarderPane.setTop(topBox);

        //--------------create botBar ---------------------------
        botBox.getChildren().add(btnStart);
        btnStart.setPrefWidth(60);
        btnStart.setOnMouseClicked(e -> this.startLife());
        botBox.getChildren().add(btnPause);
        btnPause.setPrefWidth(60);
        btnPause.setOnMouseClicked(e -> this.stopLife());
        btnPause.setDisable(true);
        botBox.getChildren().add(lblSpace1);
        lblSpace1.setVisible(false);
        botBox.getChildren().add(lblAbsp);
        botBox.getChildren().add(btnMinus50);
        btnMinus50.setPrefWidth(40);
        btnMinus50.setOnMouseClicked(e -> this.adjustSpeed(-50));
        botBox.getChildren().add(lblSpeed);
        lblSpeed.setPrefWidth(50);
        lblSpeed.setAlignment(Pos.CENTER);
        lblSpeed.setFont(Font.font(lblSpeed.getFont().getName(), FontWeight.BOLD, 20));
        botBox.getChildren().add(btnPlus50);
        btnPlus50.setPrefWidth(40);
        btnPlus50.setOnMouseClicked(e -> this.adjustSpeed(50));
        botBox.setAlignment(Pos.CENTER_LEFT);
        botBox.setSpacing(20);
        botBox.setPrefHeight(50);
        botBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-padding: 20;");
        boarderPane.setBottom(botBox);

        //--------------- create field --------------------------
        //frame field
        frameH.getChildren().add(field);
        frameH.setAlignment(Pos.CENTER);
        frameV.getChildren().add(frameH);
        frameV.setAlignment(Pos.CENTER);
        //put frame with field in midBox
        midBox.getChildren().add(frameV);
        midBox.setAlignment(Pos.CENTER);
        boarderPane.setCenter(midBox);

        //--------------- midBox-Listener for debugging -------------------------------
        midBox.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldMidBoxWidth, Number newMidBoxWidth) {
                midBoxWidth = (double) newMidBoxWidth;
                //System.out.println("Width: " + midBoxWidth);
            }
        });
        midBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldMidBoxHeight, Number newMidBoxHeight) {
                midBoxHeight = (double) newMidBoxHeight;
                //System.out.println("Height: " + midBoxHeight);
            }
        });

        //--------------- roundup -------------------------------
        boarderPane.setStyle("-fx-background-color: black;");
        Scene mainScene = new Scene(boarderPane, 1200, 800);
        stage.setScene(mainScene);
        stage.setTitle("Game Of Life");
        stage.show();

    }

    public void newField(ObservableGameLogicDecorator oglDecorator) {
        World world = oglDecorator.getWorld();
        int width = world.getWidth();
        int height = world.getHeight();
        rectMap.clear();
        field.getChildren().clear();

        //Set Observables
        ObservableNumberValue bWidth = Bindings.min(midBox.heightProperty().divide(world.getHeight()), midBox.widthProperty().divide(world.getWidth()));
        ObservableNumberValue bHeight = Bindings.min(midBox.heightProperty().divide(world.getHeight()), midBox.widthProperty().divide(world.getWidth()));
        ObservableNumberValue xStart = Bindings.subtract(field.widthProperty(), Bindings.multiply(bWidth, world.getWidth())).divide(2);
        ObservableNumberValue yStart = Bindings.subtract(field.heightProperty(), Bindings.multiply(bHeight, world.getHeight())).divide(2);


        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rectMap.put(j + "," + i, new CoordRectangle(50, 50, j, i));
                CoordRectangle rect1 = rectMap.get(j + "," + i);
                //make clickable
                rect1.setPickOnBounds(true);
                //set dynamic size
                rect1.widthProperty().bind(bWidth);
                rect1.heightProperty().bind(bHeight);
                //set dynamic position
                rect1.layoutXProperty().bind(Bindings.add(xStart, rect1.widthProperty().multiply(j)));
                rect1.layoutYProperty().bind(Bindings.add(yStart, rect1.heightProperty().multiply(i)));
                //set mouseClick
                rect1.setOnMouseClicked(e -> this.invert(e));
                //set stroke color
                rect1.setStroke(Color.BLACK);
                //set color
                if (world.get(j, i).equals(CellState.DEAD)) {
                    rect1.setFill(Color.WHITE);
                } else {
                    rect1.setFill(Color.BLACK);
                }
                field.getChildren().add(rect1);
            }
        }
    }

    private void invert(MouseEvent e) {
        CoordRectangle cRect = (CoordRectangle) e.getSource();
        int x = cRect.getXCoord();
        int y = cRect.getYCoord();
        World world = oglDecorator.getWorld();
        if (world.get(x, y).equals(CellState.DEAD)) {
            cRect.setFill(Color.BLACK);
            world.set(x, y, CellState.ALIVE);
        } else {
            cRect.setFill(Color.WHITE);
            world.set(x, y, CellState.DEAD);
        }
        oglDecorator.setWorld(world);
    }

    public void startLife() {
        //disable buttons and field
        field.setMouseTransparent(true);
        txtWidth.setDisable(true);
        txtHeight.setDisable(true);
        btnNew.setDisable(true);
        btnLoad.setDisable(true);
        btnStart.setDisable(true);
        btnMinus50.setDisable(true);
        btnPlus50.setDisable(true);
        btnPause.setDisable(false);
        int speed = Integer.parseInt(lblSpeed.textProperty().get());
        lifeRunner = new LifeRunner(oglDecorator, speed, this::lifeUpdateCallback);
    }

    public void stopLife() {
        lifeRunner.stop();
        lifeRunner = null;
        //enable buttons again
        field.setMouseTransparent(false);
        txtWidth.setDisable(false);
        txtHeight.setDisable(false);
        btnNew.setDisable(false);
        btnLoad.setDisable(false);
        btnStart.setDisable(false);
        btnMinus50.setDisable(false);
        btnPlus50.setDisable(false);
        btnPause.setDisable(true);
    }

    public void lifeUpdateCallback(ObservableGameLogicDecorator oglDecorator) {
        rectMap.clear();
        this.oglDecorator = oglDecorator;
        newField(oglDecorator);
    }

    public void exit(Stage stage) {
        if (lifeRunner != null)
            lifeRunner.stop();
        stage.close();
    }

    public void adjustSpeed(int speed) {
        int currentSpeed = Integer.parseInt(lblSpeed.textProperty().get());
        int newSpeed = currentSpeed + speed;
        if (newSpeed >= 50 && newSpeed <= 2000)
            lblSpeed.textProperty().setValue(String.valueOf(newSpeed));
    }

    public void newWorld() {
        String regex = "[1-9]+[0-9]*";
        String strWidth = txtWidth.textProperty().get();
        String strHeight = txtHeight.textProperty().get();
        if (strWidth.matches(regex) && strHeight.matches(regex)) {
            int width = Integer.parseInt(strWidth);
            int height = Integer.parseInt(strHeight);
            if (width <= 50 && height <= 50) {
                World world = new World(width, height);
                oglDecorator.setWorld(world);
                newField(oglDecorator);
            }
        }
    }

    public void loadFile(Node block) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import File");
        //chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = chooser.showOpenDialog(block.getScene().getWindow());

        Alert alertInvData = new Alert(Alert.AlertType.ERROR);
        alertInvData.setTitle("Invalid File");
        alertInvData.setContentText("The File you tried to load is not a valid Game Of Life file.");
        try {
            StandardWorldLoader standardLoader = new StandardWorldLoader();
            InputStream fileStream = new FileInputStream(file);
            oglDecorator.setWorld(standardLoader.load(fileStream));
            newField(oglDecorator);
        } catch (Exception e) {
            if (file != null)
                alertInvData.showAndWait();
        }
    }
}

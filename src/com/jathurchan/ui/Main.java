package com.jathurchan.ui;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;


public class Main extends Application {
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            root.setTop(createToolbar());
            root.setBottom(createStatusbar());
            root.setCenter(createMainContent());
            Scene scene = new Scene(root,1500,800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("The JavaFX audio processor");
            primaryStage.show();
        } catch(Exception e) {e.printStackTrace();}
    }

    private Node createToolbar(){

        // Initialization of the ToolBar
        ToolBar tb = new ToolBar();

        // -- First part --

        // Audio in
        Label lAudIn = new Label("Audio in: ");
        tb.getItems().add(lAudIn);
        ComboBox<String> cbAudIn = new ComboBox<>();
        cbAudIn.getItems().addAll("Item 1", "Item 2", "Item 3");
        tb.getItems().add(cbAudIn);

        // Audio out
        Label lAudOut = new Label("Audio in: ");
        tb.getItems().add(lAudOut);
        ComboBox<String> cbAudOut = new ComboBox<>();
        cbAudOut.getItems().addAll("Item 1", "Item 2", "Item 3");
        tb.getItems().add(cbAudOut);


        tb.getItems().add(new Separator()); // add a separator


        // -- 2nd part --

        // Sample frequency
        ComboBox<String> cbFreq = new ComboBox<>();
        cbFreq.getItems().addAll("Item 1", "Item 2", "Item 3");
        tb.getItems().add(cbFreq);
        Label lHz = new Label(" Hz");
        tb.getItems().add(lHz);


        tb.getItems().add(new Separator()); // add 2 separators
        tb.getItems().add(new Separator());


        // -- 3rd part --

        // Buffer size
        Label lBuffS = new Label("Audio in: ");
        tb.getItems().add(lBuffS);
        ComboBox<String> cbBuffS = new ComboBox<>();
        cbBuffS.getItems().addAll("Item 1", "Item 2", "Item 3");
        tb.getItems().add(cbBuffS);

        // Start
        Button startButton = new Button("Start");
        tb.getItems().add(startButton);
        startButton.setOnAction(event -> System.out.println("Start!"));

        // Compute FFT
        Button computeButton = new Button("Compute FFT");
        tb.getItems().add(computeButton);
        computeButton.setOnAction(event -> System.out.println("Start!"));


        return tb;
    }
    private Node createStatusbar(){
        HBox statusbar = new HBox();
        statusbar.getChildren().addAll(new Label("Name:"), new TextField("    "));
        return statusbar;
    }
    private Node createMainContent(){
        Group g = new Group();
        // ici en utilisant g.getChildren().add(...) vous pouvez ajouter tout  ́el ́ement graphique souhait ́e de type Node
        return g;
    } }

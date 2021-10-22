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
        Button button = new Button("appuyez !");
        ToolBar tb = new ToolBar(button, new Label("ceci est un label"), new Separator());
        button.setOnAction(event -> System.out.println("appui!"));
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll("Item 1", "Item 2", "Item 3");
        tb.getItems().add(cb);
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

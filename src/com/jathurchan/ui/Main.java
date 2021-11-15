package com.jathurchan.ui;

import com.jathurchan.audio.AudioIO;
import com.jathurchan.audio.AudioProcessor;
import javafx.application.Application;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;


public class Main extends Application {


    // ---- Variables ----

    private SignalView sigView = new SignalView(new NumberAxis(), new NumberAxis());
    private String inputMixerName, outputMixerName;
    private int sampleRate;
    private int frameSize;
    private AudioProcessor audioProcessor;


    // ---- Getter and Setter Methods ----
    public AudioProcessor getAudioProcessor() {
        return audioProcessor;
    }


    // ---- Other Important Methods ----

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

        String[] mixersNames = AudioIO.getAudioMixersNames();   // Get the mixers names available on the computer

        // Audio in
        Label lAudIn = new Label("Audio in: ");
        tb.getItems().add(lAudIn);
        ComboBox<String> cbAudIn = new ComboBox<>();
        cbAudIn.getItems().addAll(mixersNames);
        tb.getItems().add(cbAudIn);
        cbAudIn.setOnAction((event) -> {    // Update the input mixer when selected (listening for the selection)
            inputMixerName = cbAudIn.getValue();
            System.out.println(inputMixerName);
        });

        // Audio out
        Label lAudOut = new Label("Audio out: ");
        tb.getItems().add(lAudOut);
        ComboBox<String> cbAudOut = new ComboBox<>();
        cbAudOut.getItems().addAll(mixersNames);
        tb.getItems().add(cbAudOut);
        cbAudOut.setOnAction((event) -> {    // Update the output mixer when selected (listening for the selection)
            outputMixerName = cbAudOut.getValue();
            System.out.println(outputMixerName);
        });


        tb.getItems().add(new Separator()); // add a separator


        // -- 2nd part --

        // Sample frequency
        ComboBox<Integer> cbFreq = new ComboBox<>();
        cbFreq.getItems().addAll(48000, 44100, 8000);
        tb.getItems().add(cbFreq);
        Label lHz = new Label(" Hz");
        tb.getItems().add(lHz);
        cbFreq.setOnAction((event) -> {    // Update the sample rate when selected (listening for the selection)
            sampleRate = cbFreq.getValue();
            System.out.println(sampleRate);
        });


        tb.getItems().add(new Separator()); // add 2 separators
        tb.getItems().add(new Separator());


        // -- 3rd part --

        // Buffer size
        Label lBuffS = new Label("Buffer size: ");
        tb.getItems().add(lBuffS);
        ComboBox<Integer> cbBuffS = new ComboBox<>();
        cbBuffS.getItems().addAll( 32000, 16000, 8000, 1024, 512);
        tb.getItems().add(cbBuffS);
        cbBuffS.setOnAction((event) -> {    // Update the frame size when selected (listening for the selection)
            frameSize = cbBuffS.getValue();
            System.out.println(frameSize);
        });

        // Start
        Button startButton = new Button("Start");
        tb.getItems().add(startButton);
        startButton.setOnAction((event) -> {
            try {
                audioProcessor = AudioIO.startAudioProcessing(inputMixerName, outputMixerName, sampleRate, frameSize);  // New thread created
                sigView.updateData(audioProcessor.getOutputSignal());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Compute FFT
        Button computeButton = new Button("Compute FFT");
        tb.getItems().add(computeButton);
        computeButton.setOnAction(event -> System.out.println("Start!"));

        // Terminate
        Button termButton = new Button("Terminate");
        tb.getItems().add(termButton);
        termButton.setOnAction((event) -> {
            try {
                AudioIO.stopAudioProcessing(audioProcessor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        return tb;
    }


    private Node createStatusbar(){ // Show the status (Audio started / stopped)
        HBox statusbar = new HBox();
        statusbar.getChildren().addAll(new Label("Name:"), new TextField("    "));
        return statusbar;
    }


    private Node createMainContent(){
        Group g = new Group();

        g.getChildren().add(sigView);   // Signal View

        return g;
    }

}

package com.jathurchan.ui;


/*
    This document helped me a lot:
    https://docs.oracle.com/javafx/2/charts/line-chart.htm
 */


// ---- Libraries ----

import com.jathurchan.audio.AudioSignal;
import javafx.scene.chart.*;
import javafx.scene.chart.Axis;
import javafx.animation.AnimationTimer;


public class SignalView extends LineChart<Number, Number> {


    // ---- Variables ----

    private XYChart.Series<Number, Number> series;


    // ---- Constructor ----

    public SignalView(Axis<Number> xAxis, Axis<Number> yAxis) { // Found by exploring the implementation of the super class

        super(xAxis, yAxis);

        // Defining the axes
        xAxis.setLabel("n");
        yAxis.setLabel("x[n]");


        // Setting title of line chart
        this.setTitle("Audio Signal Graph");

        // Defining a series to display data
         series = new XYChart.Series<>();
         series.setName("Input Signal");

         // Add series to chart
        this.getData().add(series);
    }


    public void updateData(AudioSignal audSig) {

        for (int i=0; i<audSig.getFrameSize(); i++){
            series.getData().add(new XYChart.Data<>(i, audSig.getSample(i)));
        }

        // Add series to chart
        this.getData().add(series);
    };

}

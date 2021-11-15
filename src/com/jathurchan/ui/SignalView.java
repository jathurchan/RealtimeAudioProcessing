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

import javax.swing.*;


public class SignalView extends LineChart<Number, Number> {


    // ---- Constructor ----

    public SignalView(Axis<Number> xAxis, Axis<Number> yAxis) { // Found by exploring the implementation of the super class
        super(xAxis, yAxis);
        xAxis.setLabel("n");
        yAxis.setLabel("x[n]");
        super.setTitle("Audio Signal Graph");
    }



    public void updateData(AudioSignal audSig, SignalView sigView) {
        new AnimationTimer() {
            @Override
            // Handle Method (inspired by https://www.educba.com/javafx-animationtimer/)
            public void handle(long l)  {

                XYChart.Series data = new XYChart.Series<Number, Number>();

                for (int i=0; i<audSig.getFrameSize(); i++){
                    data.getData().add(new XYChart.Data(i, audSig.getSample(i)));
                }

                sigView.getData().add(data);

            }
        };
    }

}

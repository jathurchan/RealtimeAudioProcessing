package com.jathurchan.audio;


import javax.sound.sampled.*;


public class AudioProcessor implements Runnable {


    // ---- Variables ----

    private AudioSignal inputSignal, outputSignal;
    private TargetDataLine audioInput;
    private SourceDataLine audioOutput;
    private boolean isThreadRunning; // to "terminate" thread


    // ---- Constructor ----

    public AudioProcessor(TargetDataLine audioInput,  SourceDataLine audioOutput, int frameSize) {  // Chose frameSize to be an integer
        this.audioInput = audioInput;
        this.audioOutput = audioOutput;
        this.inputSignal = new AudioSignal(frameSize);
        this.outputSignal = new AudioSignal(frameSize);
    }


    // ---- Method that continuously process the audio data (between Read and Write) ----

    @Override
    public void run() {
        isThreadRunning = true;
        while (isThreadRunning) {
            inputSignal.recordFrom(audioInput);

            outputSignal.setFrom(inputSignal);  // Simply copy from inputSignal to ouputSignal


            outputSignal.playTo(audioOutput);
        }
    }


    // ---- Terminate Audio Thread ----

    public void terminateAudioThread() {
        isThreadRunning = false;    // enough to exit the while (in the run method)
    }


    // ---- Getter and Setter methods ----

    public AudioSignal getInputSignal(){
        return inputSignal;
    }

    public AudioSignal getOutputSignal(){
        return outputSignal;
    }

    public TargetDataLine getAudioInput(){
        return audioInput;
    }

    public SourceDataLine getAudioOutput(){
        return audioOutput;
    }

    // ---- For testing ----
    public static void main(String[] args) {
        try {
            TargetDataLine inLine = AudioIO.obtainAudioInput("Default Audio Device", 16000);
            SourceDataLine outLine = AudioIO.obtainAudioOutput("Default Audio Device", 16000);
            AudioProcessor as = new AudioProcessor(inLine, outLine, 1024);
            inLine.open();
            inLine.start();
            outLine.open();
            outLine.start();
            new Thread(as).start();
            System.out.println("A new thread has been created!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

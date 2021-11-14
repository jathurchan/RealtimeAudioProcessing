package com.jathurchan.audio;

import javax.sound.sampled.*;


public class AudioSignal {


    // ---- Variables ----

    private double[] sampleBuffer;  // floating point representation of audio sample
    private double dBLevel; // signal level in dB
    private int frameSize;


    // ---- Constructor ----

    public AudioSignal(int frameSize) {     // Chose to define frameSize as an integer
        sampleBuffer = new double[frameSize];
        this.frameSize = frameSize;
    }


    // ---- Getter & Setter Methods ----

    public double getSample(int i) {
        return sampleBuffer[i];
    }

    public void setSample(int i, double value)  {
        sampleBuffer[i] = value;
    }

    public double getdBlevel()  {
        return dBLevel;
    }

    public int getFrameSize()   {
        return frameSize;
    }

    // ---- Important methods ----

    public void setFrom(AudioSignal other) {

        // Check whether the other AudioSignal has a length higher than the one of this signal
        if(other.getFrameSize() >= this.frameSize) {

            // Update the sampleBuffer
            for (int i=0; i<this.frameSize; i++) {
                this.sampleBuffer[i] = other.getSample(i);
            }

            // Update the dbLevel
            this.dBLevel = other.getdBlevel();

        } else {
            System.out.println("setFrom: the length of the other signal is not enough");
        }
    }

    public boolean recordFrom(TargetDataLine audioInput) {
        byte[] byteBuffer = new byte[sampleBuffer.length*2];    // 16 bit samples
        if (audioInput.read(byteBuffer, 0, byteBuffer.length) == -1) return false;
        for (int i=0; i<sampleBuffer.length; i++) {
            sampleBuffer[i] = ((byteBuffer[2*i]<<8) + byteBuffer[2*i+1]) / 32768.0; // big endian
        }

        // -- Update Signal Level in dB --

        // 1st version

        /*double peakAmp = 0;
        for (double amp: sampleBuffer)  {
            if (Math.abs(amp) > peakAmp)    {
                peakAmp = Math.abs(amp);
            }
        }
        dBLevel = 20 * Math.log10(peakAmp);*/

        // 2nd version (discovered Methods inherited from interface javax.sound.sampled.DataLine)

        dBLevel = audioInput.getLevel();

        return true;
    }

    public boolean playTo(SourceDataLine audioOutput) { // It is enough to invert the process in recordFrom
        byte[] byteBuffer = new byte[sampleBuffer.length*2];    // 16 bit samples (used once again but here to store information before using write)

        for (int i=0; i<sampleBuffer.length; i++) {

            double unscaled = sampleBuffer[i] * 32768.0;

            byteBuffer[2*i] = (byte) (unscaled / 256);   // still big endian ( MSB then LSB)
            byteBuffer[2*i+1] = (byte) (unscaled % 256); // SEE THE IMAGE playToExplanation.JPEG in images/

        }

        if (audioOutput.write(byteBuffer, 0, byteBuffer.length) == -1) return false;
        return true;
    }


    public static void main(String args[])  {

        // Define the audio Format

        AudioFormat format = new AudioFormat(8000, 16, 1, true, true);

        try {
            TargetDataLine tLine = AudioSystem.getTargetDataLine(format);
            tLine.open();
            tLine.start();

            AudioSignal myAudio = new AudioSignal(32000);
            myAudio.recordFrom(tLine);

            // Print the audio signal to test
            for (int i=0; i<myAudio.getFrameSize(); i++){
                System.out.print(myAudio.getSample(i));
                System.out.print(" ");
            }
            System.out.println("");

            SourceDataLine sLine = AudioSystem.getSourceDataLine(format);
            sLine.open();
            sLine.start();

            myAudio.playTo(sLine);



        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}

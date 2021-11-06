package com.jathurchan.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioSignal {

    private double[] sampleBuffer;  // floating point representation of audio samples
    private double dBlevel; // current signal level

    private int frameSize;

    public AudioSignal(int frameSize) {
        this.frameSize = frameSize;
    }

    public void setFrom(AudioSignal other) {

    }

    public boolean recordFrom(TargetDataLine audioInput) {
        byte[] byteBuffer = new byte[sampleBuffer.length*2];    // 16 bit samples
        if (audioInput.read(byteBuffer, 0, byteBuffer.length) == -1) return false;
        for (int i=0; i<sampleBuffer.length; i++) {
            sampleBuffer[i] = ((byteBuffer[2*i]<<8) + byteBuffer[2*i+1]) / 32768.0; // big endian
        }

        // Set Signal Level in dB
        double peakAmp = 0;
        for (double amp: sampleBuffer)  {
            if (Math.abs(amp) > peakAmp)    {
                peakAmp = Math.abs(amp);
            }
        }
        dBlevel = 20 * Math.log10(peakAmp);
        return true;
    }

    public boolean playTo(SourceDataLine audioOutput) {

        // Open and start the SourceDataLine
        try {
            AudioFormat format = new AudioFormat(8000, 8, 1, true, true);
            TargetDataLine line = AudioSystem.getTargetDataLine(format);
            audioOutput.open(format, sampleBuffer.length);
            line.open(format, sampleBuffer.length);
            line.start();
        } catch (Exception error)   {
            return false;
        }

        // Buffer with bytes from a sound wave
        byte[] byteBuffer = new byte[sampleBuffer.length];
        for (int i=0; i<sampleBuffer.length; i++) {
            byteBuffer[i] = (byte ) sampleBuffer[i];
        }

        // Write the buffer to the audio output
        audioOutput.write( byteBuffer, 0, sampleBuffer.length);

        return true;
    }


    // Getter and Setter Methods

    public double getSample(int i) {    // ---- To improve by handling out of bound errors
        return sampleBuffer[i];
    }

    public void setSample(int i, double value)  {
        sampleBuffer[i] = value;
    }

    public double getdBlevel()  {
        return dBlevel;
    }

    public int getFrameSize()   {
        return frameSize;
    }

    // public Complex[] computeFFT()

    public static void main(String args[])  {

        //  Record a new sound
    }
}

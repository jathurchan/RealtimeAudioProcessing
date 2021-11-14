package com.jathurchan.audio;


import javax.sound.sampled.*;
import java.util.Arrays;


public class AudioIO {


    // ---- Methods to get information ----

    public static void printAudioMixers() { // Display every mixer available on the current system
        System.out.println("Mixers:");
        Arrays.stream(AudioSystem.getMixerInfo())
                .forEach(e -> System.out.println("- name=\"" + e.getName()
                        + "\" description=\"" + e.getDescription() + " by " + e.getVendor() + "\""));
    }

    public static Mixer.Info getMixerInfo(String mixerName) {   // Get the Mixer.Info whose name matches the given string
        return Arrays.stream(AudioSystem.getMixerInfo())
                .filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
    }


    // ---- Method to select the INPUT ----
    public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate) throws LineUnavailableException {

        // Define the audio format (16 bit samples)
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, true);

        // Get the Mixer.Info from the string
        Mixer.Info mixerInfo = getMixerInfo(mixerName);

        // Use the 2nd method getTargetDataLine(AudioFormat format, Mixer.Info mixerinfo)
        return AudioSystem.getTargetDataLine(format, mixerInfo);
    }


    // ---- Method to select the OUTPUT ----
    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate) throws LineUnavailableException {

        // Define the audio format (16 bit samples)
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, true);

        // Get the Mixer.Info from the string
        Mixer.Info mixerInfo = getMixerInfo(mixerName);

        // Use the 2nd method getTargetDataLine(AudioFormat format, Mixer.Info mixerinfo)
        return AudioSystem.getSourceDataLine(format, mixerInfo);
    }





}

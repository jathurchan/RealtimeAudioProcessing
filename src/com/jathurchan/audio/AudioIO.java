package com.jathurchan.audio;


import javax.sound.sampled.*;
import java.awt.image.AreaAveragingScaleFilter;
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

    public static String[] getAudioMixersNames() {  // Get a list : names of available mixers (FOR THE UI)

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        String[] mixersNames = new String[mixers.length];

        for (int i=0; i<mixers.length; i++) {
            mixersNames[i] = mixers[i].getName();
        }

        return mixersNames;

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


    // ---- Method to START the Audio Processing ----
    public static AudioProcessor startAudioProcessing(String inputMixer, String outputMixer, int sampleRate, int frameSize) throws LineUnavailableException{

        // Obtain and Instantiate the appropriate Target/Source Datalines
        TargetDataLine audioInput = obtainAudioInput(inputMixer, sampleRate);
        SourceDataLine audioOutput = obtainAudioOutput(inputMixer, sampleRate);
        AudioProcessor audioPro = new AudioProcessor(audioInput, audioOutput, frameSize);
        audioInput.open();
        audioInput.start();
        audioOutput.open();
        audioOutput.start();

        //  Create a thread and start it
        Thread myThread = new Thread(audioPro);
        myThread.start();

        return audioPro;    // Chose to return the audioProcessor in order to stop the right thread later using stopAudioProcessing
    }


    // ---- Method to STOP the Audio Processing ----
    public static void stopAudioProcessing(AudioProcessor audioPro){
        audioPro.terminateAudioThread();
    }

    // ---- For testing ----
    public static void main(String args[]) {

        // Get the list of mixers
        printAudioMixers();

        /*
        Got on my computer:
        Mixers:
            - name="Port shaki’s headphone hihi ð" description="shaki’s headphone hihi ð by Apple Inc."
            - name="Port shaki’s headphone hihi ð" description="shaki’s headphone hihi ð by Apple Inc."
            - name="Port MacBook Air Microphone" description="MacBook Air Microphone by Apple Inc."
            - name="Port MacBook Air Speakers" description="MacBook Air Speakers by Apple Inc."
            - name="Default Audio Device" description="Direct Audio Device: Default Audio Device by Unknown Vendor"
            - name="shaki’s headphone hihi ð" description="Direct Audio Device: shaki’s headphone hihi ð by Apple Inc."
            - name="shaki’s headphone hihi ð" description="Direct Audio Device: shaki’s headphone hihi ð by Apple Inc."
            - name="MacBook Air Microphone" description="Direct Audio Device: MacBook Air Microphone by Apple Inc."
            - name="MacBook Air Speakers" description="Direct Audio Device: MacBook Air Speakers by Apple Inc."

         */
        try {
            TargetDataLine tLine = obtainAudioInput("MacBook Air Microphone", 8000);
            SourceDataLine sLine = obtainAudioOutput("MacBook Air Speakers", 8000);

            tLine.open();
            tLine.start();

            AudioSignal myAudio = new AudioSignal(32000);
            myAudio.recordFrom(tLine);

            sLine.open();
            sLine.start();

            myAudio.playTo(sLine);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // To test getAudioMixersNames
        /*String[] arr = getAudioMixersNames();
        Arrays.stream(arr)
                .forEach( e -> System.out.println(e));*/

    }





}

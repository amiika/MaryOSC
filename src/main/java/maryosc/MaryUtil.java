package maryosc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.config.MaryConfig;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;
import marytts.util.MaryRuntimeUtils;
import marytts.util.data.audio.AudioPlayer;
import marytts.util.data.audio.MaryAudioUtils;

public class MaryUtil {

    private MaryInterface marytts;
    private AudioPlayer ap;
    private boolean debug;
    private Locale loc;
    private String[] voices;

    public MaryUtil(String voiceName,
                    String locale,
                    boolean debug) {
        this.debug = debug;
        try {
            marytts = new LocalMaryInterface();
            this.voices = getSortedVoices();
            this.loc = setLocale(locale);
            if (voiceName == null) {
                voiceName = voices[0];
            }
            if (debug) System.out.println("Setting voice: " + voiceName);
            marytts.setVoice(voiceName);
            ap = new AudioPlayer();
        } catch (MaryConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    public void setDebug() {
        this.debug = !this.debug;
    }

    public void setVoice(String voice) {
        if(debug) System.out.println("Setting voice to: "+voice);
        try {
            this.marytts.setVoice(voice);
        } catch(IllegalArgumentException ex) {
            System.out.println("No such voice!");
        }
    }

    public void setVoice(int voice) {
        if(voice>=voices.length) voice = voice % voices.length;
        if(debug) System.out.println("Setting voice "+voice+": "+voices[voice]);
        this.marytts.setVoice(voices[voice]);
    }

    public Locale setLocale(String locale) {
        if (locale != null) {
            Locale loc = Locale.forLanguageTag(locale);
            if (loc != null && MaryConfig.getLanguageConfig(loc) != null) {
                this.marytts.setLocale(loc);
            } else {
                if (debug) System.out.println("Error: " + locale + " is unsupported!");
            }
        }
        return loc;
    }

    private String[] getSortedVoices() {
        Set<String> voiceSet = this.marytts.getAvailableVoices();
        String[] voices = voiceSet.toArray(new String[voiceSet.size()]);
        Arrays.sort(voices);
        return voices;
    }

    public void printLocales() {
        for (Locale l : this.marytts.getAvailableLocales()) {
            System.out.println(l.toLanguageTag());
        }
    }

    public void printEffects() {
        for (AudioEffect e : AudioEffects.getEffects()) {
            System.out.println(e.getName());
            System.out.println(e.getHelpText());
            System.out.println();
        }
    }

    public void printVoices() {
        for (int i = 0; i < this.voices.length; i++) {
            String v = this.voices[i];
            System.out.println("" + i + ": " + v);
            String styles = MaryRuntimeUtils.getStyles(v);
            if (styles != null && !styles.isEmpty()) {
                System.out.print("Voice styles: ");
                System.out.println(styles);
            }
        }
        System.out.println("\nCurrent voice: "+this.marytts.getVoice());
    }

    public boolean isSpeaking() {
        return marytts.isStreamingAudio() || ap.isAlive();
    }

    public void setAudioEffects(String effects) {
        if (debug) System.out.println("Setting effects: " + effects);
        marytts.setAudioEffects(effects);
    }

    public void say(String input) {
        if (debug) System.out.println("Saying: " + input);
        try {
            AudioInputStream audio = marytts.generateAudio(input);
            ap.setAudio(audio);
            ap.start();
            ap = new AudioPlayer();
        } catch (SynthesisException ex) {
            System.err.println("Error saying phrase.");
        }
    }

    public void save(String path,
                     String input) {
        AudioInputStream audio;
        if (path.contains("~")) {
            path = path.replace("~", System.getProperty("user.home"));
        }
        try {
            audio = marytts.generateAudio(input);
            MaryAudioUtils.writeWavFile(
                MaryAudioUtils.getSamplesAsDoubleArray(audio),
                path, audio.getFormat());
            if (debug) System.out.println("Saved: " + path);
            audio.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SynthesisException e) {
            e.printStackTrace();
        }
    }

}


package com.example.timbaer.ka_ching;

import android.app.Activity;
import android.os.Bundle;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.AudioFormat;
import android.os.Handler;
import java.util.List  ;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import android.util.Log;

public class Audio extends Activity {
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    private final double duration = 0.1; // seconds
    private final int sampleRate = 8000;
    private final int numSamples = (int) Math.round(duration * sampleRate);
    private final double sample[] = new double[numSamples];
    private double freqOfTone = 440; // hz

    private final byte generatedSnd[] = new byte[2 * numSamples];

    Handler handler = new Handler();

    @Override
    protected void onResume() {
        super.onResume();

        final Thread thread = new Thread(new Runnable() {
            public void run() {
                genTone();
                handler.post(new Runnable() {

                    public void run() {
                        playSound();
                    }
                });
            }
        });
        thread.start();
    }

    void genTone() {
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    void playSound() {
        final AudioTrack audioTrack;
        try {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                    AudioTrack.MODE_STATIC);
        } catch (Exception e) {
            return;
        }
        try {
            audioTrack.write(generatedSnd, 0, generatedSnd.length);
        } catch (Exception e) {
            return;
        }
        try {
            audioTrack.play();
            Log.d("UNINIT", "PLAYED");
        } catch (IllegalStateException e) {
            Log.d("UNINIT", e.toString());
        }
        audioTrack.release();
    }

    void playData(List<Integer> data) {
        List<Integer> normalizeData = normalize(data);
        for (Integer point: normalizeData) {
            freqOfTone = point;
            Thread playSound = new Thread(new Runnable() {
                @Override
                public void run() {
                    genTone();
                }
            });
            playSound.start();
            try {
                playSound.join();
            } catch (InterruptedException e) {
                Log.d("PlaySoundInterrupt", e.toString());
            }
            playSound();
        }
    }

    List<Integer> normalize(List<Integer> data) {
        int min = data.get(0);
        int max = data.get(0);
        for (Integer point: data) {
            if (point > max) {
                max = point;
            }
            if (point < min) {
                min = point;
            }
        }

        int range = (int) Math.round(0.8*(max - min));

        List<Integer> normalizeData = new ArrayList<>();

        for (Integer point: data) {
            normalizeData.add(600 * ((point - min) / range) + 400);
        }

        return normalizeData;
    }
}

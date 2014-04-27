package com.example.audiostream;

import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;

public class AudioReciever {

	private boolean state = true;
	private boolean isPLaying ;

	private static AudioReciever instance;
	
	private 

	AudioReciever() {

	}

	public static AudioReciever getInstance() {
		if (instance == null) {
			instance = new AudioReciever();
			
		}
		return instance;
	}

	private ArrayList<byte[]> buffer = new ArrayList<byte[]>();

	public void fillAudioBuffer(byte[] audio) {
		buffer.clear();
		buffer.add(audio);
	}

	public boolean isPlaying(){
		return isPLaying;
	}
	private int sampleRate = 44100;
	private int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	public void play() {
		isPLaying = true;
		new Thread() {
			@Override
			public void run() {
				try {
					int  bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, audioFormat);
					AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 11025, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
							bufferSize, AudioTrack.MODE_STREAM);
					audioTrack.play();
					byte[] bs = null;
					while (state) {
						if (!buffer.isEmpty()) {
							bs = buffer.get(0);
							buffer.remove(0);
							Log.e("AUD_FRAME", ""+bs +" "+bs.length);
							audioTrack.write(bs, 0, bs.length);
						}
					}
				}

				catch (Throwable t) {
					Log.e("AudioTrack", "Playback Failed");
				}
			}
		}.start();
	}
	
	public void stop(){
		state = false;
		isPLaying = false;
	}

}

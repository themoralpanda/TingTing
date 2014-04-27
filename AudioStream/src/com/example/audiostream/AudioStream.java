package com.example.audiostream;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.util.Log;
import android.widget.Toast;

public class AudioStream {

	public static DatagramSocket socket;
	AudioRecord recorder;

	private int sampleRate = 22050;//11025
	private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig,
			audioFormat);
	private boolean status = true;

	private ArrayList<byte[]> store = new ArrayList<byte[]>();

	public void stop() {
		status = false;
		recorder.release();
	}

	private byte[] arr;

	public byte[] getBytes() {
		if (store.size() > 0) {
			arr = store.get(0);
			store.remove(0);
			return arr;
		}
		return null;
	}
	
	public int getBufferSize(){
		return bufferSize;
	}

	private static AudioStream audioStream;
private MainActivity act;
	AudioStream(MainActivity at) {
		act = at;
		
	}

	

	public void startStreaming() {

		Thread streamThread = new Thread(new Runnable() {

			@Override
			public void run() {
				status = true;
				startStream();

			}
		});
		streamThread.start();
	}
	int bufferSize = 0;
	private void startStream() {
		
		// for (int rate : new int[] {16000, 22050, 44100}) { // add the rates
		// you wish to check against
		bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig,
				audioFormat);
		
//		bufferSize = 4410;
		// if (bufferSize > 0) {
		// break;

		// }
		// }

		recorder = new AudioRecord(
				MediaRecorder.AudioSource.VOICE_COMMUNICATION, sampleRate,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
				bufferSize);

		byte[] tempBuffer = new byte[bufferSize];
		/*
		 * AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
		 * sampleRate, AudioFormat.CHANNEL_OUT_MONO,
		 * AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
		 * audioTrack.play(); byte[] bs = null;
		 */
		recorder.startRecording();
		int read = 0;
		while (status == true) {
			read = recorder.read(tempBuffer, 0, bufferSize);
			if (read > 0)
				
//				store.clear();
//				store.add(tempBuffer);
			
			if (stream) {
				
				if(!bufferUpdated){
					try {
						dos.writeShort((short)bufferSize);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bufferUpdated = true;
				}
				if (tempBuffer != null) {
					try {
						
						dos.writeInt((int)System.currentTimeMillis());
						
						dos.write(tempBuffer);
						Log.e("BYte_LENGTH ", tempBuffer.length+"SYstemTIme: "+System.nanoTime());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	private BufferedInputStream reader;
	private DataOutputStream dos;

	public void openSocket() {
		try {
			Socket socket = new Socket("192.168.115.142", 42732+act.portIncInt);
			reader = new BufferedInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			reciever.start();
//			sender.start();
		} catch (UnknownHostException e) {
			
			Toast.makeText(act, e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			
			Toast.makeText(act, e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	
	private boolean bufferUpdated;
	

	private boolean stream;
	private Thread reciever = new Thread() {
		public void run() {
			while (!stream) {
				try {
					stream = reader.available() > 0;
					
					Log.e("READER", "SIGNAL "+stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	};


}

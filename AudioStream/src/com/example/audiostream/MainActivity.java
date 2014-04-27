package com.example.audiostream;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private AudioStream recorder;
	public int portIncInt;
	private EditText num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		num = (EditText) findViewById(R.id.number);
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.button1:
			String number = num.getText().toString().trim();
			if(number.equals("")){
				Toast.makeText(MainActivity.this, "Empty field", Toast.LENGTH_LONG).show();
				num.setError("Empty field");
				return;
			}
			recorder = new AudioStream(this);
			recorder.startStreaming();
			portIncInt = Integer.valueOf(number);
			
			new Thread(){
				public void run() {
					recorder.openSocket();
				};
			}.start();
			
			break;

		default:
			break;
		}
	}
	
	
}

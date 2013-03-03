package com.remind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	
	MediaPlayer mp1;
	

	@Override
	public void onReceive(Context context, Intent intent) {

		Toast.makeText(context, "Alarm Ringing", 3000).show();
		System.out.println("Receiver is "+intent.getStringExtra("remind"));
		
		 Intent newIntent = new Intent(context, Ring.class);
		 
         newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         newIntent.putExtra("alarm", "ring");
         newIntent.putExtra("remind", intent.getStringExtra("remind"));
         newIntent.putExtra("remind_id", intent.getStringExtra("remind_id"));
         context.startActivity(newIntent);
	}

}

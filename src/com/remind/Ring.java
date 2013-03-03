package com.remind;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Ring extends Activity{

	MediaPlayer mp1;
	static MediaPlayer temp;
	private DataHelper dH;
	private SQLiteDatabase db;
	
	Calendar myCaln = Calendar.getInstance();
	String id=null,reminder=null;
	PendingIntent sender;
	AlarmManager am;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	//ignore orientation change
	super.onConfigurationChanged(newConfig);
	mp1=MediaPlayer.create(getApplicationContext(), R.raw.ulti);
	System.out.println("Mp1 is "+mp1);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		try
        {
        	this.dH = new DataHelper(this);
         	db=dH.db;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	String err = e.toString();
        	System.out.println(err);
        }
		
		final Bundle bundle=getIntent().getExtras();
		System.out.println(bundle.getString("remind"));
        if(bundle.getString("alarm")!=null)
        {
        	if(savedInstanceState==null) 
        	{
        	mp1=MediaPlayer.create(getApplicationContext(), R.raw.ulti);
        	mp1.setLooping(true);
    		mp1.start();
    		temp=mp1;
    		String ns = Context.NOTIFICATION_SERVICE;
    	    NotificationManager nMgr = (NotificationManager) this.getSystemService(ns);
    	    nMgr.cancel(1);
        	}
        	
    		new AlertDialog.Builder(Ring.this)
    		.setTitle("Reminder")
    		.setIcon(R.drawable.belly)
    		.setMessage(bundle.getString("remind"))
    		.setPositiveButton("Close", new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				mp1=(MediaPlayer)temp;
    				mp1.stop();
    				Ring.this.dH.delete(bundle.getString("remind_id"));
    				latestReminderOnTop();
    				//Intent newIntent = new Intent(getApplicationContext(), ReminderActivity.class);
    				finish();
    			}
    		}).show();
    		
        }
	}
	
	void latestReminderOnTop(){
		String time=null;
		List<String> list=dH.topReminder();
		Iterator iter=list.iterator();
		if(iter.hasNext()){
			//Reminder Id
			id=list.get(0);
			//Reminder Name
			reminder=list.get(1);
			//Reminder Time
			time=list.get(2);
		
		String st[];
		st=time.split("-| |:");
		
		myCaln.set(Calendar.YEAR, Integer.parseInt(st[0]));
		myCaln.set(Calendar.MONTH, Integer.parseInt(st[1]));
		myCaln.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st[2]));
		
		myCaln.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st[3]));
		myCaln.set(Calendar.MINUTE, Integer.parseInt(st[4]));
		
		Intent alarmintent = new Intent(getApplicationContext(),AlarmReceiver.class);
		alarmintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		alarmintent.putExtra("remind", reminder);
		alarmintent.putExtra("remind_id", id);
		 sender = PendingIntent.getBroadcast(getApplicationContext(), 1, alarmintent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 PendingIntent self = PendingIntent.getBroadcast(
					getApplicationContext(), 0, new Intent(getApplicationContext(),ReminderActivity.class),
					0);
		
		 am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, myCaln.getTimeInMillis(), sender);

		}
	}

}

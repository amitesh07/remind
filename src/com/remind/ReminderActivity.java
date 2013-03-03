package com.remind;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderActivity extends Activity {

	DateFormat dateform = DateFormat.getDateTimeInstance();
	Calendar myCaln = Calendar.getInstance();
	private NotificationManager nm;
	EditText tv;
	ArrayList<String> reminders=null;
	private DataHelper dH;
	private SQLiteDatabase db;
	PendingIntent sender;
	AlarmManager am;
	String id=null,remind=null;
	/** Called when the activity is first created. */

	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			myCaln.set(Calendar.YEAR, year);
			myCaln.set(Calendar.MONTH, monthOfYear);
			myCaln.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		}
	};
	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			myCaln.set(Calendar.HOUR_OF_DAY, hourOfDay);
			myCaln.set(Calendar.MINUTE, minute);
			
			
			insertDb();
			letsgo();
		}
	};
	private ImageView iv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.main);
		iv=(ImageView)findViewById(R.id.imageView2);
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
        
        
		iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 1, 1, "Add");
		menu.add(0, 2, 2, "View");
		menu.add(0, 3, 3, "Delete");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			tv = new EditText(this);
			new AlertDialog.Builder(ReminderActivity.this)
					.setView(tv)
					.setTitle("Reminder")
					.setMessage("Set Reminder")
					.setIcon(R.drawable.remind)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									new TimePickerDialog(
											ReminderActivity.this,
											t,
											myCaln.get(Calendar.HOUR_OF_DAY),
											myCaln.get(Calendar.MINUTE), true)
											.show();
									new DatePickerDialog(ReminderActivity.this,
											d, myCaln.get(Calendar.YEAR),
											myCaln.get(Calendar.MONTH), myCaln
													.get(Calendar.DAY_OF_MONTH))
											.show();
									
								}
							}).show();
			break;

		case 2:
			Intent intent=new Intent(this,ReminderList.class);
			intent.putExtra("view","yes");
			startActivity(intent);
			break;

		case 3:
			Intent intent2=new Intent(this,ReminderList.class);
			intent2.putExtra("delete","yes");
			startActivity(intent2);
			break;
		}
		return true;
	}
	
	

	void latestReminderOnTop(){
		String time=null;
		List<String> list=dH.topReminder();
		Iterator iter=list.iterator();
		if(iter.hasNext()){
			//Reminder Id
			id=list.get(0);
			//Reminder Name
			remind=list.get(1);
			//Reminder Time
			time=list.get(2);
		}
		String st[];
		st=time.split("-| |:");
		
		myCaln.set(Calendar.YEAR, Integer.parseInt(st[0]));
		myCaln.set(Calendar.MONTH, Integer.parseInt(st[1]));
		myCaln.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st[2]));
		
		myCaln.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st[3]));
		myCaln.set(Calendar.MINUTE, Integer.parseInt(st[4]));
		
	}
	
	void insertDb()
	{
		
		dH.insert(tv.getText().toString(), myCaln
				.get(Calendar.DAY_OF_MONTH), myCaln
				.get(Calendar.MONTH),myCaln
				.get(Calendar.YEAR),myCaln
				.get(Calendar.HOUR_OF_DAY),myCaln
				.get(Calendar.MINUTE));
		
		latestReminderOnTop();
	}
	
	void letsgo() {
		myCaln.set(Calendar.SECOND, 01);
		
		Intent alarmintent = new Intent(getApplicationContext(),AlarmReceiver.class);
		alarmintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		alarmintent.putExtra("remind", remind);
		alarmintent.putExtra("remind_id", id);
		 sender = PendingIntent.getBroadcast(getApplicationContext(), 1, alarmintent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 PendingIntent self = PendingIntent.getBroadcast(
					getApplicationContext(), 0, new Intent(getApplicationContext(),ReminderActivity.class),
					0);
		
		 am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, myCaln.getTimeInMillis(), sender);
		Toast.makeText(getApplicationContext(), "Reminder set", 3000).show();
		

		nm = (NotificationManager) getApplicationContext().getSystemService(
				Context.NOTIFICATION_SERVICE);
		CharSequence from = "Reminder";
		CharSequence message = tv.getText().toString();
		Notification notif = new Notification(R.drawable.ic_launcher,"Reminder Set. . .", myCaln.getTimeInMillis());
		notif.setLatestEventInfo(getApplicationContext(), from, message,self );
		nm.notify(1, notif);

	}
	
}
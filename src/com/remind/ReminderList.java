package com.remind;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ReminderList extends ListActivity implements View.OnClickListener{
	private DataHelper dH;
	private SQLiteDatabase db;
	List<String> UserId = new ArrayList<String>(),temp= new ArrayList<String>(),remind= new ArrayList<String>();
	String del=null,view=null;
	ReminderActivity r=new ReminderActivity();
	ArrayAdapter<String> adapter;
	int flag=0,loc=0;
	Calendar myCaln = Calendar.getInstance();
	String id=null,reminder=null;
	PendingIntent sender;
	AlarmManager am;
	CustomListViewAdapter adapterr;
	ImageButton Iview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
	
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
		
		try {
			temp = ReminderList.this.dH.select(null,null,null,null,null, null,null);
			
			System.out.println("Size of temp "+temp.size());
			
			if(temp.size()!=0)
			{
			remind=temp.subList(0,(temp.size()/2)); 
			UserId=temp.subList(temp.size()/2, temp.size());
			}else
			if(temp.size()==0)
				{remind.add("No Reminders");
				UserId.add("empty");
				System.out.println("List is Empty");
				}
			

			/*for(int i=0;i<UserId.size();i++)
			{s[i]= UserId.get(i);
			System.out.println(UserId.toString());}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error is "+e);
		}
		
		
		adapterr = new CustomListViewAdapter(this,R.layout.listcont, remind,UserId);
		
		 //adapter = new ArrayAdapter<String>(ReminderList.this, android.R.layout.simple_list_item_1,remind);
			
		 //adapterr.setNotifyOnChange(true);
		 setListAdapter(adapterr);		
//		if(flag==2)
//			{
//			System.out.println("Flag k andar hoon");
//			flag=0;
//		remind.remove(loc);
//		UserId.remove(loc);
//			}
		//adapter.notifyDataSetChanged();
		 
				
	}

/*
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		final int pos=position;
		
		Bundle bundle =getIntent().getExtras();
		del= bundle.getString("delete");
		view= bundle.getString("view");
		//Log.e("id: ", "> " + v.getId());
		

		if(del!=null && !remind.get(position).toString().equals("No Reminders"))
		{
		new AlertDialog.Builder(ReminderList.this)
		.setTitle("Delete")
		.setMessage("Are u sure u want to delete "+remind.get(position)+" reminder")
		.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Toast.makeText(getApplicationContext(), remind.get(pos)+" Reminder deleted", 3000).show();
				//r.cancelRem();
		/*		
			ReminderList.this.dH.delete(UserId.get(pos).toString());
				//adapter.notifyDataSetChanged();
				
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.cancel(PendingIntent.getBroadcast(
						getApplicationContext(), 1, new Intent(getApplicationContext(),
								AlarmReceiver.class),
						PendingIntent.FLAG_UPDATE_CURRENT));
				
				String ns = Context.NOTIFICATION_SERVICE;
	    	    NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
	    	    nMgr.cancel(1);
	    	    
				Toast.makeText(getApplicationContext(), remind.get(pos)+" Reminder deleted", 3000).show();
				
				remind.remove(pos);
				System.out.println("Reminder Removed");
				//UserId.remove(loc);
				
			
				adapterr.notifyDataSetChanged();
				setListAdapter(adapterr);
				refresh();
				flag=1;
				loc=pos;*/
				
				
//				Intent i=new Intent(getApplicationContext(),ReminderList.class);
//				startActivity(i);
	/*		}
		}).show();
		
		}else
			
			if(view!=null && !remind.get(position).toString().equals("No Reminders")){
				String s=dH.getTimeById(UserId.get(pos));
				
				new AlertDialog.Builder(ReminderList.this)
				.setTitle("Reminder")
				.setMessage(s)
				.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();	
			}
		Toast.makeText(
				getApplicationContext(),
				"you clicked on " + UserId.get(position) + " " + "at position "
						+ position+1, 3000).show();
		
	}
	*/
	
	public void refresh(){
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
		
		try {
			temp = ReminderList.this.dH.select(null,null,null,null,null, null,null);
			
			remind=temp.subList(0,(temp.size()/2)); 
			UserId=temp.subList(temp.size()/2, temp.size());
			
			if(temp.size()==0)
				{remind.add("No Reminders");
				adapterr = new CustomListViewAdapter(this,R.layout.listcont, remind,UserId);
				}

			/*for(int i=0;i<UserId.size();i++)
			{s[i]= UserId.get(i);
			System.out.println(UserId.toString());}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error is "+e);
		}
		
		
//		 adapter = new ArrayAdapter<String>(ReminderList.this, android.R.layout.simple_list_item_1,remind);
			
		 adapterr.setNotifyOnChange(true);
		 setListAdapter(adapterr);
		 
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		refresh();
		adapterr.notifyDataSetChanged();
		System.out.println("I m in Click method");
		Log.d("Listener", "I m in ");
		
	}

	
}

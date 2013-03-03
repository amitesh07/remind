package com.remind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
public class CustomListViewAdapter extends ArrayAdapter<String> implements OnClickListener{
 
    Context context;
    List<String> remitem=new ArrayList<String>();
    List<String> id=new ArrayList<String>();
    ViewHolder holder;
    private DataHelper dH;
	private SQLiteDatabase db;
 
    public CustomListViewAdapter(Context context, int resourceId,
            List<String> items, List<String> UserId) {
        super(context, resourceId, items);
        this.context = context;
        this.remitem=items;
        
        System.out.println("UserId is "+UserId);
        
        
        for(String s : UserId){
            System.out.println("adding UserId..............."+s);

        	this.id.add(s);
       }
        
        try
        {
        	this.dH = new DataHelper(getContext());
         	db=dH.db;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	String err = e.toString();
        	System.out.println(err);
        }
        
    }
 
    /*private view holder class*/
    private class ViewHolder {
        
        TextView Rem;
        ImageView view;
        ImageView del;
        
    }
 
	public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        View v=convertView;
        
        if (convertView == null) {
        	LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listcont, null);
            holder = new ViewHolder();
            
            holder.Rem = (TextView) convertView.findViewById(R.id.text);
            holder.view= (ImageView)convertView.findViewById(R.id.view);
            holder.del= (ImageView)convertView.findViewById(R.id.delete);
            
//            holder.view.setFocusable(true);
//            holder.view.setClickable(true);
//            
          
            
            /*holder.view.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				//holder.Rem.setText("Farzi");
    				Toast.makeText(getContext(), "Clicked", Toast.LENGTH_LONG);
    			}
    		});
*/
            
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
    
        
        holder.Rem.setText(remitem.get(position));
        holder.Rem.setTextColor(Color.BLACK);
        
        convertView.setBackgroundColor(Color.rgb(100, 148,237));
        
        //holder.view.setTag(id.get(position));
        //holder.del.setTag(id.get(position).toString());
        
        
        
//        if(remitem.get(position).contains("abc"))
//        	holder.view.setImageResource(R.drawable.close);
        holder.view.setOnClickListener(new MyClickListener(position));
        holder.del.setOnClickListener(new MyDelClickListener(position));
        //convertView.setOnClickListener(new MyClickListener(position));
        
 
        return convertView;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(getContext(), "Clicked", Toast.LENGTH_LONG);
		
	}
	
//View Class
private class MyClickListener implements OnClickListener {
	
    private int position;

    public MyClickListener(int position) {
       this.position = position;
    }

    public void onClick(View v) {
    	
    	//Checking if the List is empty 
    	//If yes so pop up a message  
    	if((id.get(getPosition()).equals("empty")))
    	{
    		Toast.makeText(getContext(), "No Reminders", Toast.LENGTH_SHORT).show();
    	}
    	else{
    		//If List is not empty, fetch the details from the database and display 
    	String s=dH.getTimeById(id.get(getPosition()));
		
		new AlertDialog.Builder(getContext())
		.setTitle("Reminder")
		.setMessage(s)
		.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show();
		
    	}
       System.out.println("position " + getPosition() + " clicked.");
       //Toast.makeText(getContext(), getPosition()+" View Clicked"+ id.get(getPosition()), Toast.LENGTH_SHORT).show();
    }

    public int getPosition() {
      return position;
    }
	}


//Delete Class
private class MyDelClickListener implements OnClickListener {
	
    private int position;
    int i=0;

    public MyDelClickListener(int position) {
       this.position = position;
    }

    public void onClick(View v) {
    	
   
   
       System.out.println("position " + getPosition() + " clicked");
      // Toast.makeText(getContext(), getPosition()+" Del Clicked ", Toast.LENGTH_SHORT).show();
       
       
	   System.out.println("Id item is "+id);
	   
	   //Checking if the list is not Empty 
	   //then only Delete the rows
	   if(!(id.get(getPosition()).equals("empty")))
       {
    	System.out.println("value equal "+id.get(getPosition()));   
    	dH.delete(id.get(getPosition()));
       	id.remove(getPosition());
       	remitem.remove(getPosition());
       }
	   else
	   {
		   //Pop up
		   //Nothing to Delete
		   Toast.makeText(getContext(), "Nothing to Delete", Toast.LENGTH_SHORT).show();
	   }
	   
	   //Checking whether the last value in the list is deleted and the list size is zero
	   //and id attribute is empty
	   //then add the No Reminder text in the list to the view
	   //as well as cancel all the existing Notifications
		   if(remitem.size()==0 && id.isEmpty())
		   {
			   remitem.add("No Reminders");
			   id.add("empty");
			   
			   AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
				am.cancel(PendingIntent.getBroadcast(getContext(), 1, new Intent(getContext(),
								AlarmReceiver.class),PendingIntent.FLAG_UPDATE_CURRENT));
			   
				String ns = Context.NOTIFICATION_SERVICE;
	    	    NotificationManager nMgr = (NotificationManager) getContext().getSystemService(ns);
	    	    nMgr.cancel(1);
		   }


       //id.remove(getPosition());

//       CustomListViewAdapter.this.remove(remitem.get(getPosition()));
       CustomListViewAdapter.this.notifyDataSetChanged();
       //remitem.remove(getPosition());

       //new CustomListViewAdapter(getContext(),R.layout.listcont, remitem,id);
       //notifyDataSetChanged();
    }

    public int getPosition() {
      return position;
    }
    
	}



}


package com.remind;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DataHelper {
	 
	   static final String DATABASE_NAME = "Reminder.db";
	   static final int DATABASE_VERSION = 1;
	   static final String TABLE_NAME1 = "Reminder";
	   private Context context;
	   public SQLiteDatabase db;
	   
	   public DataHelper(Context context) {
	      this.context = context;
	      OpenHelper openHelper = new OpenHelper(this.context);
	      this.db = openHelper.getWritableDatabase();
	   
	   }
	   
	   public void insert(String Remind,Integer dd,Integer mm, Integer yy, Integer hr, Integer min) {
		try{ 
		   String date=yy + "-" + mm + "-" + dd + " " + hr+ ":" + min;
		   db.execSQL("INSERT INTO "
                   + TABLE_NAME1
                   + " VALUES ("+null+",'" + Remind + "','" + date +"' );");
			
		}catch(Exception e)
	      {e.printStackTrace();
  	    
		String err = e.toString();
  	    System.out.println(err);
  	      	 
	      }
       }
	   
	   public void delete(String Remind) {
			try{ 
			   
			   db.execSQL("Delete from "
	                   + TABLE_NAME1
	                   + " Where id= '" + Remind +"';");
				
			}catch(Exception e)
		      {e.printStackTrace();
	  	    
			String err = e.toString();
	  	    System.out.println(err);
	  	      	 
		      }
	       }
	   
	   public String getTimeById(String id){
		   String time=null;
		   Cursor cursor=db.rawQuery("select remdate from Reminder where id = '"+id+"'",null);
		      if (cursor.moveToFirst())
		      {
		    	  time=cursor.getString(0);
		      }
		   return time;
		   
	   }
	   
	   public List<String> topReminder(){
		   
		   List<String> list = new ArrayList<String>();
		      Cursor cursor=db.rawQuery("select * from Reminder order by `remdate` asc limit 1",null);
		      if (cursor.moveToFirst()) 
		      {
		    	  list.add(cursor.getString(0).toString());
		    	  list.add(cursor.getString(1).toString());
		    	  list.add(cursor.getString(2).toString());
		      }  
		   return list;
	   }
	   
	   public List<String> select( String[] columns, String selection, String[] 
			                                    selectionArgs, String groupBy, 
			                                    String having, String sortBy, String sortOption) 
	   {
		      List<String> list1 = new ArrayList<String>();
		      Cursor cursor = this.db.query(TABLE_NAME1, columns, selection, selectionArgs, groupBy,
						having, sortBy,sortOption);
		      if (cursor.moveToFirst()) 
		      {
		    	  do 
		    	  {
		    		  list1.add(cursor.getString(1).toString());
		    	  } while (cursor.moveToNext());
		    	 
		    	  cursor.moveToFirst();
		    	 
		    	  do 
		    	  {
		    		  list1.add(cursor.getString(0).toString());
		    	  } while (cursor.moveToNext());
		    	   
		    	  
		    	  if (cursor != null && !cursor.isClosed()) 
		    	  {
		    		  cursor.close();
		    	  }
		      }
		      return list1;
	   }
	   
	   public List<String> getReminder( String[] columns, String selection, String[] 
	                          			                                    selectionArgs, String groupBy, 
	                          			                                    String having, String sortBy, String sortOption) 
	                          	   {
	                          		      List<String> list1 = new ArrayList<String>();
	                          		      Cursor cursor = this.db.query(TABLE_NAME1, columns, selection, selectionArgs, groupBy,
	                          						having, sortBy,sortOption);
	                          		      if (cursor.moveToFirst()) 
	                          		      {
	                          		    	  do 
	                          		    	  {
	                          		    		  list1.add(cursor.getString(1));
	                          		    		list1.add(cursor.getString(2));
//	                          		    		list1.add(cursor.getString(2));
//	                          		    		list1.add(cursor.getString(3));
//	                          		    		list1.add(cursor.getString(4));
//	                          		    		list1.add(cursor.getString(5));
	                          		    	  } while (cursor.moveToNext());
	                          		    	 
	                          		    	  if (cursor != null && !cursor.isClosed()) 
	                          		    	  {
	                          		    		  cursor.close();
	                          		    	  }
	                          		      }
	                          		      return list1;
	                          	   }
	   //For Fetching the data with datatime sequence SELECT DATE_FORMAT(created_at,'%r') from customers where id='8'; insert into test(time,name) values('2006-10-04 22:23:01','she')
	   //select * from Reminder order by `remdate` asc limit 1    //select count(*) from Reminder
	   
	   private static class OpenHelper extends SQLiteOpenHelper {
	 
	      OpenHelper(Context context) {
	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      }
	 
	      @Override
	      public void onCreate(SQLiteDatabase db) {
	         db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 +
	          "(id INTEGER PRIMARY KEY, reminder VARCHAR2(20),remdate datetime)");
	      }
	 
	      @Override
	      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
	         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
             onCreate(db);
	      }
	   }
}


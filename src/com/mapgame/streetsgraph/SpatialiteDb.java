package com.mapgame.streetsgraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jsqlite.Database;
import jsqlite.Exception;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SpatialiteDb extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "dbb.sqlite";
	
	private Context context;
	
	private Database db;

	public SpatialiteDb(Context context) {
		super(context, DATABASE_NAME, null, 1);
		this.context = context;
		try {
			if(!DataBaseExists()) {
				copyDataBase();
			}
			
			File dbFile = new File(context.getDatabasePath(DATABASE_NAME).getAbsolutePath());
			
			db = new jsqlite.Database();
			db.open(dbFile.getAbsolutePath(),
					jsqlite.Constants.SQLITE_OPEN_READWRITE | jsqlite.Constants.SQLITE_OPEN_CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public Database getDb() {
		return db;
	}
	
	 /**
	  * Copies your database from your local assets-folder to the just created empty database in the
	  * system folder, from where it can be accessed and handled.
	  * This is done by transfering bytestream.
	  * */
	private void copyDataBase() throws IOException {
	 
		this.getReadableDatabase();
			
		//Open your local db as the input stream
		InputStream myInput = context.getAssets().open("databases/" + DATABASE_NAME);
		 
		// Path to the just created empty db
		String outFileName = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
		 
		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		 
		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}
		 
		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
	
	 /**
	  * Check if the database already exist to avoid re-copying the file each time you open the application.
	  * @return true if it exists, false if it doesn't
	  */
	private boolean DataBaseExists(){
	 
		SQLiteDatabase checkDB = null;
		 
		try{
			checkDB = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getAbsolutePath(), 
					null, SQLiteDatabase.OPEN_READONLY);		 
		}catch(SQLiteException e){
			//database does't exist yet.
		}
		 
		if(checkDB != null){
			checkDB.close(); 
		}
		 
		return checkDB != null ? true : false;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
	}

}

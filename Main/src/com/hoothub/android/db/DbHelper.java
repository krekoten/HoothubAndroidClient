/**
 * 
 */
package com.hoothub.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Мар'ян Крекотень (Marjan Krekoten')
 *
 */
public class DbHelper extends SQLiteOpenHelper {

	static int VERSION = 1;
	
	public DbHelper(Context context, String name) {
		super(context, name, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

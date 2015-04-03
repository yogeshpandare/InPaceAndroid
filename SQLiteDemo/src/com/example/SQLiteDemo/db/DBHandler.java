/**
 * 
 */
package com.example.SQLiteDemo.db;

import java.util.ArrayList;
import java.util.List;

import com.example.SQLiteDemo.vo.NameVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Prabu
 *
 */
public class DBHandler extends SQLiteOpenHelper implements CRUDOperations{
	
	// Database Version
	private static final int DATABASE_VERSION = 1;
	
	// Database Name
	private static final String DATABASE_NAME = "demoDb";
	
	// Table name
	private static final String TABLE_NAME = "names";
	
	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_FIRST_NAME = "firstName";
	private static final String KEY_LAST_NAME = "lastName";
	
	/**
	 * @param context
	 */
	public DBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRST_NAME + " TEXT,"
				+ KEY_LAST_NAME + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);
	}

	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Adding new name
	 * @see com.example.SQLiteDemo.db.CRUDOperations#addNameVO(com.example.SQLiteDemo.vo.NameVO)
	 * 
	 * Method accepts NameVO object as parameter. 
	 * We need to build parameters using NameVO object. 
	 * Once we inserted data in database we need to close the database connection.
	 */
	@Override
	public void addNameVO(NameVO name) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_FIRST_NAME, name.getFirstName()); // First Name
		values.put(KEY_LAST_NAME, name.getLastName()); // Last Name
		// Inserting Row
		db.insert(TABLE_NAME, null, values);
		// Closing database connection
		db.close(); 
	}

	/**
	 * Getting single name
	 * @see com.example.SQLiteDemo.db.CRUDOperations#getNameVO(int)
	 * 
	 * Will read single nameVO row. 
	 * It accepts id as parameter and will return the matched row from the database.
	 */
	@Override
	public NameVO getNameVO(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
				KEY_FIRST_NAME, KEY_LAST_NAME }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		NameVO nameVO = new NameVO(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return nameVO
		return nameVO;
	}

	/**
	 * Gettig all names
	 * @see com.example.SQLiteDemo.db.CRUDOperations#getAllNameVO()
	 * 
	 * Will return all nameVOs from database in array list format of NameVO class type. 
	 */
	@Override
	public List<NameVO> getAllNameVO() {
		List<NameVO> nameVoList = new ArrayList<NameVO>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				NameVO nameVO = new NameVO();
				nameVO.setId(Integer.parseInt(cursor.getString(0)));
				nameVO.setFirstName(cursor.getString(1));
				nameVO.setLastName(cursor.getString(2));
				// Adding NameVO to list
				nameVoList.add(nameVO);
			} while (cursor.moveToNext());
		}
		// return NameVO list
		return nameVoList;
	}

	/**
	 * Getting names Count
	 * @see com.example.SQLiteDemo.db.CRUDOperations#getNameVOCount()
	 * 
	 * Will return total number of NameVOs in SQLite database.
	 */
	@Override
	public int getNameVOCount() {
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

	/**
	 * Updating single entry
	 * @see com.example.SQLiteDemo.db.CRUDOperations#updateNameVO(com.example.SQLiteDemo.vo.NameVO)
	 * 
	 * Will update single nameVO in database. 
	 * This method accepts NameVo class object as parameter.
	 */
	@Override
	public int updateNameVO(NameVO name) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_FIRST_NAME, name.getFirstName());
		values.put(KEY_LAST_NAME, name.getLastName());
		// updating row
		return db.update(TABLE_NAME, values, KEY_ID + " = ?",
				new String[] { String.valueOf(name.getId()) });
	}

	/**
	 * Deleting single entry 
	 * @see com.example.SQLiteDemo.db.CRUDOperations#deleteNameVO(com.example.SQLiteDemo.vo.NameVO)
	 * 
	 * Will delete single nameVO from database.
	 */
	@Override
	public void deleteNameVO(NameVO name) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID + " = ?",
				new String[] { String.valueOf(name.getId()) });
		db.close();
	}

	@Override
	public void deleteAllNames(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}
}

package com.quanturium.cursorsectionadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CursorSectionDirectoryDatabase extends SQLiteOpenHelper
{
	private static CursorSectionDirectoryDatabase	mInstance			= null;

	public final static String						TAG					= "DirectoryDatabase";

	private final static String						DATABASE_NAME		= "Directory.db";
	private static final int						DATABASE_VERSION	= 1;

	public final static String						COL_ID				= "_id";
	public final static String						COL_FIRSTNAME		= "firstname";
	public final static String						COL_LASTNAME		= "lastname";

	private static final String						TABLE_NAME			= "Directory";
	private static final String						TABLE_CREATE		= "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_FIRSTNAME + ", " + COL_LASTNAME + ")";

	public static CursorSectionDirectoryDatabase getInstance(Context ctx)
	{
		if (mInstance == null)
			mInstance = new CursorSectionDirectoryDatabase(ctx.getApplicationContext());

		return mInstance;
	}

	private CursorSectionDirectoryDatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public long addContact(String firstName, String lastName)
	{
		ContentValues values = new ContentValues();
		values.put(COL_FIRSTNAME, firstName);
		values.put(COL_LASTNAME, lastName);

		SQLiteDatabase db = this.getWritableDatabase();
		long id = db.insert(TABLE_NAME, null, values);
		db.close();
		return id;
	}

	public void deleteContacts()
	{
		Log.w(TAG, "deleting all events in database");

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

	public Cursor fetchContactsByFirstname()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		return db.query(TABLE_NAME, null, null, null, null, null, COL_FIRSTNAME + " ASC");
	}
}
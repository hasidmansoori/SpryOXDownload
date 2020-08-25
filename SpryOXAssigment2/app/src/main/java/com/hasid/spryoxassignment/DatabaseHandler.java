package com.hasid.spryoxassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "linkManager";
    private static final String TABLE_LINK = "links";
    private static final String KEY_LINK = "link";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be pa ssed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LINK + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_LINK + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }    private static final String KEY_ID = "id";


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINK);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addLink(Link link) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LINK, link.getLink()); // Link

        // Inserting Row
        db.insert(TABLE_LINK, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    Link getLink(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LINK, new String[] { KEY_ID,
                        KEY_LINK }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Link link = new Link(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        // return contact
        return link;
    }

    // code to get all contacts in a list view
    public List<Link> getAllLink() {
        List<Link> contactList = new ArrayList<Link>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LINK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Link contact = new Link();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setLink(cursor.getString(1));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateLink(Link link) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LINK, link.getLink());

        // updating row
        return db.update(TABLE_LINK, values, KEY_ID + " = ?",
                new String[] { String.valueOf(link.getID()) });
    }

    // Deleting single contact
    public void deleteLink(Link link) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LINK, KEY_ID + " = ?",
                new String[] { String.valueOf(link.getID()) });
        db.close();
    }

}

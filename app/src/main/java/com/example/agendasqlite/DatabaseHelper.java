package com.example.agendasqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "_id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_STREET = "street";
    public static final String CONTACTS_COLUMN_CITY = "place";
    public static final String CONTACTS_COLUMN_PHONE = "phone";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        " (" + CONTACTS_COLUMN_ID + " integer primary key autoincrement," +
                        CONTACTS_COLUMN_NAME + " text," +
                        CONTACTS_COLUMN_PHONE + " text," +
                        CONTACTS_COLUMN_EMAIL + " text," +
                        CONTACTS_COLUMN_NAME + " text," +
                        CONTACTS_COLUMN_CITY + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact (Contato c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, c.get_nome());
        contentValues.put(CONTACTS_COLUMN_PHONE, c.get_num_tel());
        contentValues.put(CONTACTS_COLUMN_EMAIL, c.get_email());
        contentValues.put(CONTACTS_COLUMN_STREET, c.get_logradouro());
        contentValues.put(CONTACTS_COLUMN_CITY, c.get_cidade());
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Contato c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, c.get_nome());
        contentValues.put(CONTACTS_COLUMN_PHONE, c.get_num_tel());
        contentValues.put(CONTACTS_COLUMN_EMAIL, c.get_email());
        contentValues.put(CONTACTS_COLUMN_STREET, c.get_logradouro());
        contentValues.put(CONTACTS_COLUMN_CITY, c.get_cidade());
        if (
                db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(c.get_id())}) > 0)
            return true;
        else
            return false;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACTS_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteAll () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACTS_TABLE_NAME,
                null,
                null);
    }

    public ArrayList<String> getAllContacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<Contato> getContactsList() {
        ArrayList<Contato> lista = new ArrayList<Contato>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Contato c = new Contato();
            c.set_nome(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            c.set_id(Integer.parseInt(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID))));
            c.set_email(res.getString(res.getColumnIndex(CONTACTS_COLUMN_EMAIL)));
            c.set_cidade(res.getString(res.getColumnIndex(CONTACTS_COLUMN_CITY)));
            c.set_logradouro(res.getString(res.getColumnIndex(CONTACTS_COLUMN_STREET)));

            lista.add(c);
            res.moveToNext();
        }

        return lista;
    }
}

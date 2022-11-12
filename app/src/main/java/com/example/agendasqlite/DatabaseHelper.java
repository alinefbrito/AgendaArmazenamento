package com.example.agendasqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contatos";
    public static final String CONTACTS_COLUMN_ID = "_id";
    public static final String CONTACTS_COLUMN_NAME = "nome";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_STREET = "rua";
    public static final String CONTACTS_COLUMN_CITY = "cidade";
    public static final String CONTACTS_COLUMN_PHONE = "telefone";


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        "( " + CONTACTS_COLUMN_ID + " integer primary key autoincrement," +
                        CONTACTS_COLUMN_NAME + " text , " +
                        CONTACTS_COLUMN_PHONE + " text ," +
                        CONTACTS_COLUMN_EMAIL + " text, " +
                        CONTACTS_COLUMN_STREET + " text, " +
                        CONTACTS_COLUMN_CITY + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " +
                CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact(Contato c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, c.get_nome());
        contentValues.put(CONTACTS_COLUMN_PHONE, c.get_num_tel());
        contentValues.put(CONTACTS_COLUMN_EMAIL, c.get_email());
        contentValues.put(CONTACTS_COLUMN_STREET, c.get_logradouro());
        contentValues.put(CONTACTS_COLUMN_CITY, c.get_cidade());
        return db.insert(CONTACTS_TABLE_NAME,
                null,
                contentValues) > 0;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " +
                CONTACTS_TABLE_NAME + " where " +
                CONTACTS_COLUMN_ID + "=" + id + "",
                null);
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        return (int) DatabaseUtils.queryNumEntries(db,
                CONTACTS_TABLE_NAME);
    }

    public boolean updateContact(Contato c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, c.get_nome());
        contentValues.put(CONTACTS_COLUMN_PHONE, c.get_num_tel());
        contentValues.put(CONTACTS_COLUMN_EMAIL, c.get_email());
        contentValues.put(CONTACTS_COLUMN_STREET, c.get_logradouro());
        contentValues.put(CONTACTS_COLUMN_CITY, c.get_cidade());
        db.update(CONTACTS_TABLE_NAME,
                contentValues,
                CONTACTS_COLUMN_ID + " = ?  ",
                new String[]{Integer.toString(c.get_id())});
        return true;
    }

    public void deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME,
                CONTACTS_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME,
                null,
                null);
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllNames() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " +
                CONTACTS_TABLE_NAME + " from " +
                CONTACTS_TABLE_NAME,
                null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(
                    res.getString(
                            res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<Contato> getContactsList() {
        ArrayList<Contato> lista = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " +
                        CONTACTS_TABLE_NAME,
                null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Contato c = new Contato();
            int i = res.getColumnIndex(CONTACTS_COLUMN_NAME);
            c.set_nome(res.getString(i));
            i = res.getColumnIndex(CONTACTS_COLUMN_ID);
            c.set_id(Integer.parseInt(res.getString(i)));
            i = res.getColumnIndex(CONTACTS_COLUMN_EMAIL);
            c.set_email(res.getString(i));
            i = res.getColumnIndex(CONTACTS_COLUMN_CITY);
            c.set_cidade(res.getString(i));
            i = res.getColumnIndex(CONTACTS_COLUMN_STREET);
            c.set_logradouro(res.getString(i));
            i = res.getColumnIndex(CONTACTS_COLUMN_PHONE);
            c.set_num_tel(res.getString(i));

            lista.add(c);
            res.moveToNext();
        }
        res.close();
        return lista;
    }
}

package com.example.notariaapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notaria.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_CITAS = "citas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SOLICITANTE = "solicitante";
    private static final String COLUMN_NOTARIO = "notario";
    private static final String COLUMN_SALA = "sala";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_DESCRIPCION = "descripcion";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CITAS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SOLICITANTE + " TEXT, " +
                COLUMN_NOTARIO + " TEXT, " +
                COLUMN_SALA + " TEXT, " +
                COLUMN_FECHA + " TEXT, " +
                COLUMN_HORA + " TEXT, " +
                COLUMN_DESCRIPCION + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITAS);
        onCreate(db);
    }

    public boolean insertCita(String solicitante, String notario, String sala, String fecha, String hora, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOLICITANTE, solicitante);
        values.put(COLUMN_NOTARIO, notario);
        values.put(COLUMN_SALA, sala);
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_HORA, hora);
        values.put(COLUMN_DESCRIPCION, descripcion);
        long result = db.insert(TABLE_CITAS, null, values);
        db.close();
        return result != -1;
    }

    public ArrayList<String> getAllCitas() {
        ArrayList<String> citas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CITAS, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String cita =
                        "Solicitante: " + cursor.getString(cursor.getColumnIndex(COLUMN_SOLICITANTE)) + "\n" +
                                "Notario: " + cursor.getString(cursor.getColumnIndex(COLUMN_NOTARIO)) + "\n" +
                                "Sala: " + cursor.getString(cursor.getColumnIndex(COLUMN_SALA)) + "\n" +
                                "Fecha: " + cursor.getString(cursor.getColumnIndex(COLUMN_FECHA)) + "\n" +
                                "Hora: " + cursor.getString(cursor.getColumnIndex(COLUMN_HORA)) + "\n" +
                                "Descripción: " + cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPCION));
                citas.add(cita);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return citas;
    }

    public void deleteCita(String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CITAS + " WHERE " + COLUMN_DESCRIPCION + " = ?", new String[]{descripcion});
        db.close();
    }
}
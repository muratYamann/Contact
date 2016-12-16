
package com.yamankod.rehberim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

   public  static String TAG = "_DBHelper";
   private static final String DATABASE_NAME   = "RehberimDB";

   // Contacts table name
   private static final String TABLE_COUNTRIES = "Rehber";

   public DBHelper(Context context) {
      super(context, DATABASE_NAME, null, 1);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      String sql = "CREATE TABLE " + TABLE_COUNTRIES + "(id INTEGER PRIMARY KEY,rehber_name TEXT,rehber_number TEXT" + ")";
      Log.d("DBHelper", "SQL : " + sql);
      db.execSQL(sql);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);

      onCreate(db);
   }

   public void insertRehber(Kisi kisi) {
      SQLiteDatabase db = this.getWritableDatabase();

      ContentValues values = new ContentValues();
      values.put("rehber_name", kisi.getIsim());
      values.put("rehber_number", kisi.getNumara());

      db.insert(TABLE_COUNTRIES, null, values);
      db.close();
   }

   public List<Kisi> getAllCountries() {
      List<Kisi> kisiler = new ArrayList<Kisi>();
      SQLiteDatabase db = this.getWritableDatabase();

      // String sqlQuery = "SELECT  * FROM " + TABLE_COUNTRIES;
      // Cursor cursor = db.rawQuery(sqlQuery, null);

      Cursor cursor = db.query(TABLE_COUNTRIES, new String[]{"id", "rehber_name", "rehber_number"}, null, null, null, null, null);
      while (cursor.moveToNext()) {

         Log.d(TAG, "getAllCountries: id--"+cursor.getInt(0)+"--Name:"+cursor.getInt(1));

         Kisi kisi = new Kisi();
         kisi.setId(cursor.getInt(0));
         kisi.setIsim(cursor.getString(1));
         kisi.setNumara(cursor.getString(2));
         kisiler.add(kisi);
      }

      return kisiler;
   }

}

package com.yamankod.rehberim;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by murat on 16.12.2016.
 */

public class Database_Activity_main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi_database_main);


        final ListView customListView = (ListView) findViewById(R.id.listview_database);
        DBHelper dbHelper = new DBHelper(getApplicationContext());



        List<Kisi> kisiler = dbHelper.getAllCountries();
        MyListAdapter myListAdapter = new MyListAdapter(Database_Activity_main.this, kisiler);
        customListView.setAdapter(myListAdapter);


        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(getApplicationContext(),
                        ((Kisi) customListView.getAdapter().getItem(position)).getNumara(),
                        Toast.LENGTH_LONG).show();

            }
        });
    }


}

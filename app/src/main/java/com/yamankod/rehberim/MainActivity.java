
package com.yamankod.rehberim;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	final static int PERMISSIONS_REQUEST_CODE = 1;
	final List<Kisi> kisiler=new ArrayList<Kisi>();
	public static String TAG ="_Main";

	private Button btnDabase;

	String phoneNumber;
	ArrayList<String> rehberNumara = new ArrayList<String>();
	ArrayList<String>rehberName = new ArrayList<>();




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getPermissionReadContact();
		getPermissionCallContact();

		btnDabase  =(Button)findViewById(R.id.btnVeriTabani);


		//kişileri ekleme
		getNumber(this.getContentResolver());

		final ListView listemiz = (ListView) findViewById(R.id.liste);
		OzelAdapter adaptorumuz=new OzelAdapter(this, kisiler);
		listemiz.setAdapter(adaptorumuz);

		listemiz.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

				final int numara =position;


				AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
				builder1.setMessage("Arama Yapmak istediginizden emin misiniz?.");
				builder1.setCancelable(true);

				builder1.setPositiveButton(
						"Evet",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Toast.makeText(MainActivity.this,rehberNumara.get(numara), Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + rehberNumara.get(numara)));
								startActivity(intent);
								dialog.cancel();
							}
						});

				builder1.setNegativeButton(
						"Hayir",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Toast.makeText(getBaseContext(),"Arama iptal", Toast.LENGTH_SHORT)
										.show();
								dialog.cancel();
							}
						});

				AlertDialog alert11 = builder1.create();
				alert11.show();


			}
		});




		btnDabase.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(TAG, "onClick:");

				Toast.makeText(MainActivity.this, "Veri tabanından çekiliyor ...", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(MainActivity.this,Database_Activity_main.class);
				startActivity(i);

			}
		});





	}


	public void getNumber(ContentResolver cr) {

		Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

		while (phones.moveToNext()) {
			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			rehberName.add(name);
			rehberNumara.add(phoneNumber);

			if (name != null) {
				kisiler.add(new Kisi(name, phoneNumber));
			}else {
				kisiler.add(new Kisi("isim Kayıtlı deil",phoneNumber));
			}



			//Veri Tabanı işlemleri
			DBHelper dbHelper = new DBHelper(getApplicationContext());

			//rehber veri tabanına yedekleme işleminin sadece 1 defa yapılması için shared konrolü yapılacak
			SharedPreferences settings = getSharedPreferences("SQL", 0);
			boolean firstTime = settings.getBoolean("firstTime", true);

		//	if (firstTime) {
				dbHelper.insertRehber(new Kisi(name, phoneNumber));

				Log.d(TAG, "savedatabase: name:"+name +"phoneNumber: "+phoneNumber);

				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("firstTime", false);
				editor.commit();
		//	}




		}
		phones.close();
	}


	//izin işlemleri

	public void getPermissionReadContact() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
				!= PackageManager.PERMISSION_GRANTED) {

			if (shouldShowRequestPermissionRationale(
					Manifest.permission.READ_CONTACTS)) {
			}
			requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
					PERMISSIONS_REQUEST_CODE);
		}
	}

	public void getPermissionCallContact() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
				!= PackageManager.PERMISSION_GRANTED) {

			if (shouldShowRequestPermissionRationale(
					Manifest.permission.CALL_PHONE)) {
			}
			requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
					PERMISSIONS_REQUEST_CODE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String permissions[],
										   @NonNull int[] grantResults) {
		if (requestCode == PERMISSIONS_REQUEST_CODE) {
			if (grantResults.length == 1 &&
					grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

}
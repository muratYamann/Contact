
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

		//rehber veri tabanına yedekleme işleminin sadece 1 defa yapılması için shared  ile kontrolü yapılacak
		SharedPreferences settings = getSharedPreferences("SQL", 0);
		boolean firstTime = settings.getBoolean("firstTime", true);


		if (firstTime) {

			DBHelper dbHelper = new DBHelper(getApplicationContext());

			while (phones.moveToNext()) {
				String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

				rehberName.add(name);
				rehberNumara.add(phoneNumber);

				if (name != null) {
					kisiler.add(new Kisi(name, phoneNumber));
				} else {
					kisiler.add(new Kisi("isim Kayıtlı deil", phoneNumber));
				}

				//Veri Tabanı işlemleri
				dbHelper.insertRehber(new Kisi(name, phoneNumber));
				Log.d(TAG, "savedatabase: name:" + name + "phoneNumber: " + phoneNumber);


			}

			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("firstTime", false);
			editor.commit();
			//	}
			phones.close();

		}else {

			while (phones.moveToNext()) {
				String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

				rehberName.add(name);
				rehberNumara.add(phoneNumber);

				if (name != null) {
					kisiler.add(new Kisi(name, phoneNumber));
				} else {
					kisiler.add(new Kisi("isim Kayıtlı deil", phoneNumber));
				}

			}

			phones.close();


		}



	}


	//verileri  başka bir yere gondermek istersen bu e-mail vb olabilir. MainActivity clasına kodunu ,Activity_main .xml inede
	//butonumuzu yerleştirip çağırıp sendEmail fonksiyonunu çagırman yeterli
	//EXTRA_TEXT in  ikinci parametresine Arraylistlere verdimiz verileri ekliyorsun .
	protected void sendEmail() {
		Log.i("Send email", "");
		String[] TO = {""};
		String[] CC = {""};
		Intent emailIntent = new Intent(Intent.ACTION_SEND);

		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rehberim");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			finish();
			Log.d(TAG,"Finished sending email...");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
		}

   }
}
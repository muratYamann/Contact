
package com.yamankod.rehberim;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter extends BaseAdapter {

   private LayoutInflater inflater;
   private List<Kisi> kisilistesi;

   public MyListAdapter(Activity activity, List<Kisi> countries) {
      inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      kisilistesi = countries;
   }

   @Override
   public int getCount() {
      return kisilistesi.size();
   }

   @Override
   public Object getItem(int position) {
      return kisilistesi.get(position);
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      View vi = convertView;
      if (convertView == null)
         vi = inflater.inflate(R.layout.listview_row, null); // create layout from

      TextView textView = (TextView) vi.findViewById(R.id.row_textview); // user name

      Kisi kisi = kisilistesi.get(position);

      textView.setText(kisi.getIsim());
      return vi;
   }
}

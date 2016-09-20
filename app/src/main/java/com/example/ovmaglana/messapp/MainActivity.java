package com.example.ovmaglana.messapp;

import com.example.ovmaglana.messapp.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> MOBILE_OS = new ArrayList<String>();
        SQLiteDatabase database = openOrCreateDatabase("messages", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS incoming (id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR); ");
        Cursor c = database.rawQuery("SELECT message FROM incoming;", null);
        int count = c.getCount(); // 2
        String[] list = new String[count]; //2

            int i = 0;
            while (c.moveToNext()) {
                list[i] = c.getString(0);
                i++;
            }


        setListAdapter(new MobileArrayAdapter(this, list));
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

    }

}
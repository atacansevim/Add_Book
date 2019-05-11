package com.example.add_art_1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<Bitmap> artImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listView = (ListView) findViewById(R.id.ListView);

        final ArrayList<String> artName = new ArrayList<String>();
        artImage = new ArrayList<Bitmap>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,artName);
        listView.setAdapter(arrayAdapter);

        try{
            Main2Activity.database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            Main2Activity.database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR,image BLOB)");
            Cursor cursor = Main2Activity.database.rawQuery("SELECT * FROM arts",null);

            int nameIx = cursor.getColumnIndex("name");
            int imageIx = cursor.getColumnIndex("image");

            cursor.moveToFirst();

            while (cursor != null)
            {
                artName.add(cursor.getString(nameIx));
                byte[] byteArray = cursor.getBlob(imageIx);
                Bitmap image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                artImage.add(image);

                cursor.moveToNext();

                arrayAdapter.notifyDataSetChanged();

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("info","old");
                intent.putExtra("name",artName.get(i));
                intent.putExtra("position",i);

                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_Art_1) {
            Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

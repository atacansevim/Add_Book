package com.example.add_art_1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {
    ImageView imageView;
    EditText editText;
    static SQLiteDatabase database;
    Bitmap selectedimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.button);
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        if(info.equalsIgnoreCase("new"))
        {
            Bitmap background = BitmapFactory.decodeResource(getApplication().getResources(),R.drawable.ic_launcher_background);
            imageView.setImageBitmap(background);
            button.setVisibility(View.VISIBLE);
            editText.setText("");

        }
        else
        {
            String name = intent.getStringExtra("name");
            editText.setText(name);
            int position = intent.getIntExtra("position",0);
            imageView.setImageBitmap(MainActivity.artImage.get(position));


            button.setVisibility(View.INVISIBLE);
        }

    }

    public void save(View view)
    {
        String artname = editText.getText().toString();// isimi değişekni

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();// output stream nesnesi
        selectedimage.compress(Bitmap.CompressFormat.PNG,50,outputStream);// resmi ssıkıştırma
        byte[] bytearray = outputStream.toByteArray();// outputstream to byte array database kayıt olacak değer

        try {
            database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS arts(name VARCHAR,image BLOB)");
            String sqls = "INSERT INTO arts(name,image) VALUES(?,?)";
            SQLiteStatement statement = database.compileStatement(sqls);
            statement.bindString(1,artname);
            statement.bindBlob(2,bytearray);
            statement.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);

    }
    public void select(View view)
    {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) // izin kontorol ypksa
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);

        }// izin önceden varda
        else
        {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//kullanıcının fotoğraflarına erişme
            startActivityForResult(intent,1);


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 2)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//kullanıcının fotoğraflarına erişme
                startActivityForResult(intent,1);


            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)//RequestCode Verdiğimiz Code yukarıdaki
    {// foto null değil isee resultcode = result veriyor mu kontorlu
        if(requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            Uri image = data.getData();
            try {
                selectedimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                imageView.setImageBitmap(selectedimage);
            }
            catch (IOException e)
            {

                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}

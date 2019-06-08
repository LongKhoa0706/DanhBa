package vn.sefvi.ps09105_lamvanthong_lab4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    Button btnNtGd, btnXemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        suKien();
    }








    private void anhXa() {
        btnNtGd = findViewById(R.id.btnNhanTinGoiDien);
        btnXemList = findViewById(R.id.btnThietKeList);
    }
    private void suKien() {
        btnNtGd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NtGdActivity.class);
                startActivity(intent);
            }
        });
        btnXemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ThietKeListActivity.class);
                startActivity(intent);
            }
        });
    }
}

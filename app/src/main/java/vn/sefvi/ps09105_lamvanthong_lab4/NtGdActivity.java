package vn.sefvi.ps09105_lamvanthong_lab4;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NtGdActivity extends AppCompatActivity {
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private final int REQUEST_READ_PHONE_STATE=1;

    private static final int REQUEST_CALL = 1;
    EditText edtSDT, edtNoiDungTinNhan;
    Button btnQuaySo, btnGoiLuon, btnGuiTn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nt_gd);
        anhXa();
        suKien();

    }

    private void suKien() {

        btnQuaySo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyQuaySo();
            }
        });
        btnGoiLuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyGoiLuon();
            }
        });
        btnGuiTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyNhanTin();
            }
        });

    }
    private void XuLyQuaySo(){
         Uri uri = Uri.parse("tel:"+edtSDT.getText().toString());
         Intent intent = new Intent(Intent.ACTION_DIAL);
         intent.setData(uri);
         startActivity(intent);
    }
    private void XuLyGoiLuon(){
        if (ContextCompat.checkSelfPermission(NtGdActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NtGdActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            Uri uri = Uri.parse("tel:" + edtSDT.getText().toString());
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(uri);
            startActivity(intent);
        }
    }
    private void XuLyNhanTin(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            if(checkPermission(Manifest.permission.SEND_SMS)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(edtSDT.getText().toString(), null, edtNoiDungTinNhan.getText().toString(), null, null);
                Toast.makeText(this, "Đã gủi thành công!", Toast.LENGTH_SHORT).show();
            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                Toast.makeText(this, "Chưa bật quyền tin nhắn", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void anhXa() {
        edtSDT = findViewById(R.id.edtSdt);
        edtNoiDungTinNhan = findViewById(R.id.edtNdTn);
        btnGoiLuon = findViewById(R.id.btnGL);
        btnQuaySo = findViewById(R.id.btnQuaySo);
        btnGuiTn = findViewById(R.id.btnNT);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                Toast.makeText(this, "Không cho phép truy cập", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_READ_PHONE_STATE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                Toast.makeText(this, "Không cho phép truy cập", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }


}

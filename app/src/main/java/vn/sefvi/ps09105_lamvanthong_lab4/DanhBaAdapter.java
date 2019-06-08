package vn.sefvi.ps09105_lamvanthong_lab4;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static vn.sefvi.ps09105_lamvanthong_lab4.ThietKeListActivity.DATABASE_NAME;
import static vn.sefvi.ps09105_lamvanthong_lab4.ThietKeListActivity.DB_PATH_SUFFIX;

public class DanhBaAdapter extends ArrayAdapter<DanhBa> {
    Activity context;
    int resource;
    List<DanhBa> list;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private final int REQUEST_READ_PHONE_STATE=1;
    private static final int REQUEST_CALL = 1;

    public DanhBaAdapter(Context context, int resource,  List<DanhBa> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.resource = resource;
        this.list = objects;
    }
    public View getView(final int i, View view, ViewGroup viewGroup){
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);

        TextView txtTen = row.findViewById(R.id.txtTen);
        TextView txtSdt = row.findViewById(R.id.txtSdt);
        ImageView btnChiTiet = row.findViewById(R.id.btnChiTiet);
        ImageView btnLvGoi = row.findViewById(R.id.btnLvGoi);
        ImageView btnLvTn = row.findViewById(R.id.btnLvTn);
        final DanhBa danhBa = this.list.get(i);
        txtTen.setText(danhBa.getTen());
        txtSdt.setText(danhBa.getPhone());

        btnChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chi Tiết: " + danhBa.getChitiet(), Toast.LENGTH_SHORT).show();
            }
        });

        btnLvGoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyGoiLuon(danhBa);
            }
        });

        btnLvTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyNhanTin(danhBa);
            }
        });


        return row;

    }

   
    private void xuLyGoiLuon(DanhBa danhBa){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            Uri uri = Uri.parse("tel:" + danhBa.getPhone());
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(uri);
            context.startActivity(intent);
        }

    }

    private void xuLyNhanTin(final DanhBa danhBa){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dlnhantin);
        final EditText edtDlNoiDung = dialog.findViewById(R.id.edtDlNoiDungTn);
        Button btnHuy = dialog.findViewById(R.id.btnDlHuy);
        Button btnNt = dialog.findViewById(R.id.btnDlGui);

        btnNt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                } else {
                    if(checkPermission(Manifest.permission.SEND_SMS)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(danhBa.getPhone(), null, edtDlNoiDung.getText().toString(), null, null);
                        Toast.makeText(context, "Đã gủi thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                        Toast.makeText(context, "Chưa bật quyền tin nhắn", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                Toast.makeText(context, "Không cho phép truy cập", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_READ_PHONE_STATE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                Toast.makeText(context, "Không cho phép truy cập", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(context, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }








}

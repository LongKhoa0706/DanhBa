package vn.sefvi.ps09105_lamvanthong_lab4;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ThietKeListActivity extends AppCompatActivity {
    public static String DATABASE_NAME="quanlydanhba.db";
    public static String DB_PATH_SUFFIX="/databases/";
    public  SQLiteDatabase database=null;
    ListView lvDanhba;
    ArrayList<DanhBa> dsDanhBa;
    ArrayAdapter danhbaAdapter;
    EditText txtten,txtma,txtsdt,txtghichu;
    Button btnluu;
    ImageView imgthem;
    Dialog hopthoai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_ke_list);
        xyLySaoChepCSDLTuAssetsVaoHeThongMobile();
        anhXa();
        sukien();
        docCsdlBangdanhba();



    }

    private void sukien() {
        imgthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hopthoai=new Dialog(ThietKeListActivity.this);
                hopthoai.setContentView(R.layout.layout);
                txtma=hopthoai.findViewById(R.id.txtmahopthoai);
                txtten=hopthoai.findViewById(R.id.txttenhopthoai);
                txtsdt=hopthoai.findViewById(R.id.txtsdthopthoai);
                txtghichu=hopthoai.findViewById(R.id.txtchitiethopthoai);
                btnluu=hopthoai.findViewById(R.id.btnluu);
                hopthoai.show();
                btnluu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hamthemnguoidung();
                        hopthoai.dismiss();
                    }
                });

            }
        });
    }

    private void xyLySaoChepCSDLTuAssetsVaoHeThongMobile()
    {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if(!dbFile.exists())
        {
            try
            {
                copyDataBaseFromAsset();
                Toast.makeText(this, "sao chep thành công", Toast.LENGTH_SHORT).show();

            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void copyDataBaseFromAsset() {
        try
        {
            InputStream myInput=getAssets().open(DATABASE_NAME);
            String outFileName=layDuongDanLuuTru();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX );
            if(!f.exists())
            {
                f.mkdir();
            }
            OutputStream myOutPut= new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length=myInput.read(buffer))>0)
            {
                myOutPut.write(buffer,0,length);

            }
            myOutPut.flush();
            myOutPut.close();
            myInput.close();

        }
        catch(Exception ex)
        {
            Log.e("Loi sao chep",ex.toString());

        }
    }

    private String layDuongDanLuuTru()
    {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
    private void docCsdlBangdanhba() {
        database =openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query("nguoidung",null,null,null,null,null,null);
        dsDanhBa.clear();
        while(cursor.moveToNext())
        {
            Integer mand=cursor.getInt(0);
            String tennd=cursor.getString(1);
            String sdt=cursor.getString(2);
            String chitiet=cursor.getString(3);

            DanhBa danhba= new DanhBa();
            danhba.setMa(mand);
            danhba.setTen(tennd);
            danhba.setPhone(sdt);
            danhba.setChitiet(chitiet);
            dsDanhBa.add(danhba);
        }
        cursor.close();
        danhbaAdapter.notifyDataSetChanged();
    }


    public void hamthemnguoidung() {
        ContentValues row = new ContentValues();
        row.put("mand",Integer.parseInt(txtma.getText().toString()));
        row.put("tennd",txtten.getText().toString());
        row.put("sdt",txtsdt.getText().toString());
        row.put("chitiet",txtghichu.getText().toString());
        long r=database.insert("nguoidung",null,row);
        Toast.makeText(ThietKeListActivity.this,"them thanh cong",Toast.LENGTH_SHORT).show();
        docCsdlBangdanhba();

    }



    private void anhXa() {
        imgthem=findViewById(R.id.imgthem);
        lvDanhba = findViewById(R.id.lvDanhBa);
        dsDanhBa = new ArrayList<DanhBa>();

        /*
        dsDanhBa.add(new DanhBa(1, "Lâm Văn Thông","0158975212"));
        dsDanhBa.add(new DanhBa(2, "Nguyễn Văn Tèo","166994944"));
        dsDanhBa.add(new DanhBa(3, "Nguyễn Văn A","48221554855"));
    */

        danhbaAdapter = new DanhBaAdapter(ThietKeListActivity.this,R.layout.item, dsDanhBa);
        lvDanhba.setAdapter(danhbaAdapter);
        lvDanhba.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogCapNhat(position);
            }
        });
      lvDanhba.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, long a) {
              final AlertDialog.Builder builder = new AlertDialog.Builder(ThietKeListActivity.this);
              builder.setMessage("Bạn có muốn xóa Danh Bạ "+ dsDanhBa.get(i).getTen() +" không?");
              builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    int kq =  database.delete("nguoidung", "mand=?", new String[]{String.valueOf(dsDanhBa.get(i).getMa())});
                    if (kq == 0){
                        Toast.makeText(ThietKeListActivity.this, "Xóa Không Thành Công", Toast.LENGTH_SHORT).show();
                    }else {
                        docCsdlBangdanhba();
                        Toast.makeText(ThietKeListActivity.this, "Đã Xóa Thành Công", Toast.LENGTH_SHORT).show();
                    }

                  }
              });
              builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                  }
              });
              builder.show();

              return false;
          }
      });
    }
    public void showDialogCapNhat(final  int i){
        final Dialog dialogCapNhat = new Dialog(ThietKeListActivity.this);
        dialogCapNhat.setContentView(R.layout.dialog_capnhat);
        final EditText edtTenCapNhat = dialogCapNhat.findViewById(R.id.txttenhopthoaicapnhat);
        final EditText edtSdtCapNhat = dialogCapNhat.findViewById(R.id.txtsdthopthoaicapnhat);
        final EditText edtChitietCapNhat = dialogCapNhat.findViewById(R.id.txtchitiethopthoaicapnhat);

        Button btnLuuCapNhat = dialogCapNhat.findViewById(R.id.btnluucapnhat);
        final DanhBa danhBa = dsDanhBa.get(i);
        edtTenCapNhat.setText(danhBa.getTen());
        edtSdtCapNhat.setText(danhBa.getPhone());
        edtChitietCapNhat.setText(danhBa.getChitiet());

        btnLuuCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values= new ContentValues();
                values.put("tennd",edtTenCapNhat.getText().toString());
                values.put("sdt",edtSdtCapNhat.getText().toString());
                values.put("chitiet",edtChitietCapNhat.getText().toString());
                int kq =  database.update("nguoidung",values,"mand=?", new String[]{String.valueOf(danhBa.getMa())});
                if(kq==0)
                {
                    Toast.makeText(ThietKeListActivity.this,"Cập nhật thất bại",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ThietKeListActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                    dialogCapNhat.dismiss();
                    docCsdlBangdanhba();
                }
            }
        });
        dialogCapNhat.show();
    }
}
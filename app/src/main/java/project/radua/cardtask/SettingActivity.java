package project.radua.cardtask;

import android.app.LauncherActivity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioButton radioButton5;
    Bitmap bitmap;
    Button button;
    Button button2;
    Button button3;
    Button button5;
    TextView textView;
    ImageView imageView11;
    int Speed = 600;
    SharedPreferences sp;
    SQLiteDatabase db;
    public int Position = 0;
    public int ishide = 0;
    private static final int PICTURE = 10086;
    private SQLiteHelper dbHelper;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton5 = findViewById(R.id.radioButton5);
        textView = findViewById(R.id.textView2);
        imageView11 = findViewById(R.id.imageView11);
        sp = getSharedPreferences("positions", Context.MODE_PRIVATE);
        Position = sp.getInt("positions",0);
        if (Position == 0){
            radioButton1.setChecked(true);
        }
        if (Position == 1){
            radioButton2.setChecked(true);
        }
        if (Position == 2) {
            radioButton3.setChecked(true);
        }
        ishide = sp.getInt("ishide",0);
            if (ishide == 0)
            {
                radioButton4.setChecked(true);
            }
            if (ishide == 1)
            {
                radioButton5.setChecked(true);
            }
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Position = 0;
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("positions",Position);
                editor.commit();
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Position = 1;
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("positions",Position);
                editor.commit();
            }
        });
        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Position = 2;
                SharedPreferences.Editor editor= sp.edit();
                editor.putInt("positions",Position);
                editor.commit();

            }
        });
        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ishide = 0;
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("ishide",ishide);
                editor.commit();
            }
        });
        radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ishide = 1;
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("ishide",ishide);
                editor.commit();
            }
        });
        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTexts = findViewById(R.id.editText);
                String s = editTexts.getText().toString();
                try {
                    Speed = Integer.parseInt(s);
                }catch (Exception e){

                }
                if (Speed < 200 ||Speed > 2000){
                    Snackbar.make(button,"请不要输入小于200或大于2000的速度值！",Snackbar.LENGTH_LONG).show();
                    editTexts.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) SettingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(editTexts.getWindowToken(),0);
                }else {
                    SharedPreferences sps = getSharedPreferences("speed", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sps.edit();
                    editor.putInt("speed",Speed);
                    editor.commit();
                    Snackbar.make(button,"设置成功",Snackbar.LENGTH_LONG).show();
                    editTexts.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) SettingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(editTexts.getWindowToken(),0);
                }
            }
        });
        button2 = findViewById(R.id.button3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.editText3);
                String s = editText.getText().toString();
                int re = 20;
                try{
                    re = Integer.parseInt(s);
                }catch (Exception e){

                }
                if(re > 200||re < 0){
                    Toast.makeText(getApplicationContext(),"超出极限值，请重新输入！",Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences sps = getSharedPreferences("round", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sps.edit();
                    editor.putInt("round",re);
                    editor.commit();
                    Snackbar.make(button,"设置成功",Snackbar.LENGTH_LONG).show();
                    editText.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) SettingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
                }
            }
        });

        button3 = findViewById(R.id.button4);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    //intent.addCategory(Intent.ACTION_OPEN_DOCUMENT);
                    startActivityForResult(intent,PICTURE);
            }
        });
        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView11.setImageResource(R.drawable.floatin);
                SharedPreferences sp = getSharedPreferences("pisturefloat",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("key",0);
                editor.commit();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null){
            this.finish();
            return;
        }
        Uri uri = data.getData();
        switch (requestCode){
            case PICTURE:
                ContentResolver cr = this.getContentResolver();
                try{
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    imageView11.setImageBitmap(bitmap);
                    db = dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("id",1);
                    cv.put("avatar",bitmabToBytes(bitmap));
                    db.insert("User", null, cv);
                    db.close();
                    SharedPreferences sp = getSharedPreferences("pisturefloat",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("key",1);
                    editor.commit();
                }catch (Exception e){

                }
            default:break;
        }
    }
    public byte[] bitmabToBytes(Bitmap bitmap){
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        }catch (Exception e){
        }finally {
            try {
                bitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
    @Override
    protected void onDestroy() {
        painting painting = new painting();
        if (painting.isServiceRunning(SettingActivity.this, "project.radua.cardtask.CardTaskFloatingWindow") == false) {
            SharedPreferences sp = getSharedPreferences("isrun",Context.MODE_PRIVATE);
            int isrun = sp.getInt("runmode",0);
            if (isrun == 1) {
                startFloatingService(new View(getApplicationContext()));
            }
        }
        super.onDestroy();
    }

    public void startFloatingService(View view) {
        Intent intenta = new Intent(SettingActivity.this, CardTaskFloatingWindow.class);

        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {
            startService(intenta);

        }

    }
}

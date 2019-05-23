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
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowInsets;
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
    Button button6;
    TextView textView;
    EditText editText2;
    EditText editText3;
    ImageView imageView11;
    DBOperate dbOperate;
    int Speed = 600;
    SharedPreferences sp;
    SQLiteDatabase db;
    public int Position = 0;
    public int ishide = 0;
    private static final int PICTURE = 10086;
    private SQLiteHelper dbHelper;
    private Context context;
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        init();
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
        button6 = findViewById(R.id.button6);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText4);
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(button6,"请输入大于0并且小于90的坐标值",Snackbar.LENGTH_SHORT).show();
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = editText2.getText().toString();
                String s2 = editText3.getText().toString();
                int x = Integer.parseInt(s1);
                int y = Integer.parseInt(s2);
                if (x<0||y<0){
                    Snackbar.make(button6,"请输入大于0并且小于90的坐标值",Snackbar.LENGTH_SHORT).show();
                }else if(x>90||y>90){
                    Snackbar.make(button6,"请输入大于0并且小于90的坐标值",Snackbar.LENGTH_SHORT).show();
                }else {
                    SharedPreferences sps = getSharedPreferences("pickcolor",Context.MODE_PRIVATE);
                    SharedPreferences.Editor spsedit= sps.edit();
                    spsedit.putInt("x",x);
                    spsedit.putInt("y",y);
                    spsedit.commit();
                    Snackbar.make(button6,"取色坐标保存成功！",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void init(){
        dbOperate=new DBOperate(this);
        byte[] imgData = dbOperate.readImage();
        try {
            Bitmap imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
            imageView11.setImageBitmap(imagebitmap);
            SharedPreferences sps = getSharedPreferences("pisturefloat", Context.MODE_PRIVATE);
            int asd = sps.getInt("key", 0);
            if (asd == 0) {
                imageView11.setImageResource(R.drawable.floatin);
            }
        }catch (Exception e){

        }
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
                    dbOperate.saveImage(bitmap);
                    SharedPreferences sp = getSharedPreferences("pisturefloat",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("key",1);
                    editor.commit();
                }catch (Exception e){

                }
            default:break;
        }
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

package project.radua.cardtask;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    RadioButton radioButton1;
    RadioButton radioButton2;
    Button button;
    TextView textView;
    int Speed = 600;
    SharedPreferences sp;
    public int Position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        textView = findViewById(R.id.textView2);
        sp = getSharedPreferences("positions", Context.MODE_PRIVATE);
        Position = sp.getInt("positions",0);
        if (Position == 0){
            radioButton1.setChecked(true);
        }
        if (Position == 1){
            radioButton2.setChecked(true);
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

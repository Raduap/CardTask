package project.radua.cardtask;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        final Switch swit = findViewById(R.id.switch1);
        if (!hasPermission()) {
            Snackbar.make(floatingActionButton, "请先检查权限是否开启！", Snackbar.LENGTH_INDEFINITE)
                    .setAction("开启权限", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CheckHasPermisson();
                            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
                        }
                    })
                    .show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckHasPermisson();

                painting paintin = new painting();

                if (paintin.isServiceRunning(getApplicationContext(),"project.radua.cardtask.CardTaskFloatingWindow")==false )
                {
                    startFloatingService(new View(getApplicationContext()));
                    Snackbar.make(floatingActionButton,"点击悬浮窗试一下吧！",Snackbar.LENGTH_LONG).show();

                }else {
                    Snackbar.make(floatingActionButton,"已经开启了悬浮窗",Snackbar.LENGTH_LONG).show();

                }
            }
        });

        //this.setFinishOnTouchOutside(true);
    }
    public void startFloatingService(View view) {
        Intent intenta = new Intent(MainActivity.this, CardTaskFloatingWindow.class);
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {
            startService(intenta);

        }
    }
    private int MY_PERMISSIONS_PACKAGE_USAGE_STATS;
    public void CheckHasPermisson(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if (!hasPermission()){
                Toast.makeText(this,"请打开权限",Toast.LENGTH_SHORT);
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_PACKAGE_USAGE_STATS
                );
            }
        }
    }

    private Boolean hasPermission(){
        AppOpsManager appops = (AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        int mode =0;
        mode = appops.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(),getPackageName());
        return  mode == AppOpsManager.MODE_ALLOWED;
    }
}

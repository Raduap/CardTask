package project.radua.cardtask;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckHasPermisson();

        painting paintin = new painting();
        if (paintin.isServiceRunning(getApplicationContext(),"project.radua.cardtask.CardTaskFloatingWindow")==false )
        {
            startFloatingService(new View(getApplicationContext()));
        }
        //this.setFinishOnTouchOutside(true);
    }

    public void startFloatingService(View view) {

        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {
            startService(new Intent(MainActivity.this, CardTaskFloatingWindow.class));
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

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            mode = appops.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(),getPackageName());
        }

        return  mode == AppOpsManager.MODE_ALLOWED;
    }
}

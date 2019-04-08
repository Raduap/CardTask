package project.radua.cardtask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int currentVersion = info.versionCode;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        int lastVersion = sp.getInt("VERSION_KEY",0);

        if (currentVersion>lastVersion){
            sp.edit().putInt("VERSION_KEY",currentVersion).commit();
            Intent intent=new Intent(this,WelcomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

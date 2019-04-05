package project.radua.cardtask;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class painting {
    Paint p = new Paint();


    //drawable转换为bitmap的专属方法
    public Bitmap drawableToBitamp(Drawable drawable) {
        Bitmap bitmap;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }

    //获取app包名列表
    public List<UsageStats> CreatAppList(Context context) {
        List<UsageStats> statr = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

            if (m != null) {
                long now = System.currentTimeMillis();
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 60 * 1000, now);
                String names = "";
                String nameone = "";
                View view = new View(context);
                String str = "";
                //这里写一个冒泡排序，进行app包名的由新到旧排序
                for (int i = 0; i < stats.size(); i++) {
                    for (int j = 0; j < stats.size() - 1; j++) {
                        if (stats.get(j).getLastTimeUsed() < stats.get(j + 1).getLastTimeUsed()) {
                            //str = stats.get(j).getPackageName();

                            UsageStats us = stats.get(j);

                            stats.set(j, stats.get(j + 1));

                            stats.set(j + 1, us);

                        }
                    }
                }

                for (int i=0;i < stats.size();i++)
                {
                    if (stats.get(i).getPackageName().contains("launcher"))
                    {
                        stats.remove(i);
                    }

                }

                for (int i=0;i < stats.size();i++)
                {
                    if (stats.get(i).getPackageName().equals("android")){
                        stats.remove(i);
                    }
                }
                //示例化的方法
                statr = stats;
                //最后复制进程名单

            }


        }

        return statr;
    }

    //根据指定包名获取图标信息
    public Drawable GetIcon(String string, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(string, PackageManager.GET_META_DATA);

            Drawable appIcon = pm.getApplicationIcon(info);
            return appIcon;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    //获取空bitmap
    public void CreatNewMainPicture() {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        painting paint = new painting();
    }

    //app专属取色
    public int GetColor(Bitmap bitmap) {
        int Colo = -1;
        Colo = bitmap.getPixel(40, 40);
        int a = Color.alpha(Colo);
        int r = Color.red(Colo);
        int g = Color.green(Colo);
        int b = Color.blue(Colo);
        int color = Color.argb(a, r, g, b);
        return color;
    }

    //跳转到指定的包名指向的App中去
    public void TurnToActivity(String PakageName, Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(PakageName);
        if (intent == null) return;
        context.startActivity(intent);
    }


    //判断是否运行service
    public  boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

}
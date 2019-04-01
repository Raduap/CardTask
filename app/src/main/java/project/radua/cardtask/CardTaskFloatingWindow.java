package project.radua.cardtask;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;


import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.print.PrintDocumentAdapter;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CardTaskFloatingWindow extends Service{
    public  static boolean isStarted = false;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button button;
    private ImageView imageViewer;
    ConstraintLayout touchLayout;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override

    public void onCreate() {
        super.onCreate();
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.width = 120;
        layoutParams.height = 160;
        int width = windowManager.getDefaultDisplay().getWidth();
        layoutParams.x =width+-60;
        layoutParams.gravity = Gravity.LEFT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS ;
        //CheckClick();

    }

    public void showFloatingWindow(){
        if (Settings.canDrawOverlays(this)){
            button = new Button(getApplicationContext());
            button.setBackgroundColor(PixelFormat.RGBA_8888);
            button.setBackground(getResources().getDrawable(R.drawable.floatin));
            windowManager.addView(button,layoutParams);
            button.setOnTouchListener(new FloatingOnTouchListener());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();

                    layoutParams.width =  windowManager.getDefaultDisplay().getWidth()/2;
                    layoutParams.x -= windowManager.getDefaultDisplay().getWidth()/4;
                    layoutParams.height = windowManager.getDefaultDisplay().getHeight()/2;
                    touchLayout = (ConstraintLayout)LayoutInflater.from(getApplicationContext()).inflate(R.layout.cardfloatlayout,null);
                    windowManager.addView(touchLayout,layoutParams);
                    CheckClick();

                }
            });

        }


    }


    public void CheckClick()
    {
        final View imageView = touchLayout.findViewById(R.id.imageView);
        //ImageView imageView1 = touchLayout.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",180f,360f);
                objectAnimator.setDuration(600);
                objectAnimator.start();
                PackageInit();
            }
        });

    }


    private class FloatingOnTouchListener implements View.OnTouchListener{
        private int x;
        private int y;

        @Override
        public  boolean onTouch(View view, MotionEvent event){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if (layoutParams.x <-50)
                    {
                        layoutParams.x = -40;
                    }
                    int widthr = windowManager.getDefaultDisplay().getWidth();

                    if (layoutParams.x >widthr-60)
                    {
                        layoutParams.x = widthr-60;
                    }

                    x = (int)event.getRawX();
                    y = (int)event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    if (layoutParams.x <-50)
                    {
                        layoutParams.x = -40;
                        movedX = 0;
                    }
                    int width = windowManager.getDefaultDisplay().getWidth();
                    if (layoutParams.x >width-60)
                    {
                        layoutParams.x = width-60;
                        movedX = 0;
                    }
                    int hidth = windowManager.getDefaultDisplay().getHeight();
                    if (layoutParams.y < -hidth/2 + 30)
                    {
                        layoutParams.y = -hidth/2 + 10;
                    }
                    if (layoutParams.y > hidth/2)
                    {
                        layoutParams.y = hidth/2;
                    }
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view,layoutParams);

                    break;
                default:
                    break;
            }
            return false;

        }



    }


    //测试包名以及获取图标的测试化步骤
    public void PackageInit(){
        ImageView imageView = touchLayout.findViewById(R.id.imageView);
        painting painting = new painting();
        List<UsageStats> stats = painting.CreatAppList(getApplicationContext());
        if (stats == null)return;
        String test = stats.get(6).getPackageName();
        int i =stats.size();
            Drawable drawable;
            Bitmap bitmap;
            drawable = painting.GetIcon(stats.get(6).getPackageName(),getApplicationContext());
            bitmap = painting.drawableToBitamp(drawable);
            Bitmap createbitmap = CreateMDicon(bitmap);
            imageView.setImageBitmap(createbitmap);
        painting.TurnToActivity(test,getApplicationContext());
        //Drawable  drawable;
        //drawable = painting.GetIcon(test,getApplicationContext());
        //Bitmap bitmap;
        //bitmap = painting.drawableToBitamp(drawable);
        //view.setImageBitmap(bitmap);
        //imageView.setImageBitmap(bitmap);
    }

    public void SinglePicture(){



    }

    private Boolean hasPermission(){
        AppOpsManager appops = (AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        int mode =0;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            mode = appops.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(),getPackageName());
        }

        return  mode == AppOpsManager.MODE_ALLOWED;
    }

    private int MY_PERMISSIONS_PACKAGE_USAGE_STATS;
    public void CheckHasPermisson(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if (!hasPermission()){
                Toast.makeText(this,"请打开权限",Toast.LENGTH_SHORT);
                Context context = getApplicationContext();
                context.startActivity(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        //MY_PERMISSIONS_PACKAGE_USAGE_STATS
                );
            }
        }
    }

    public Bitmap CreateMDicon(Bitmap bitmap)
    {
        int ColorIcon = -1;
        painting painting = new painting();
        ColorIcon = painting.GetColor(bitmap);
        Context context = getApplicationContext();
        WindowManager wm = windowManager;
        int width1 = wm.getDefaultDisplay().getWidth();
        int hidth1 = wm.getDefaultDisplay().getHeight();
        int width2 = bitmap.getWidth();
        int hidth2 = bitmap.getHeight();
        Bitmap bitmap1 = Bitmap.createBitmap(width2*2,hidth2*2,Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmap1);
        paint.setColor(ColorIcon);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0,width1,hidth1,paint);
        canvas.drawBitmap(bitmap,width2/4,hidth2/4,null);
        canvas.save();
        return bitmap1;
    }

}

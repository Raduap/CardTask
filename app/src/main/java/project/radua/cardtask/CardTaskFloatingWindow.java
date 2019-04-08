package project.radua.cardtask;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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
    private ImageView imageViewer ;
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
        int hidth = windowManager.getDefaultDisplay().getHeight();
        layoutParams.y = hidth/5;
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
                    layoutParams.width =  windowManager.getDefaultDisplay().getWidth();
                    layoutParams.x = 0;
                    layoutParams.height = windowManager.getDefaultDisplay().getHeight();
                    layoutParams.y = 0;
                    touchLayout = (ConstraintLayout)LayoutInflater.from(getApplicationContext()).inflate(R.layout.cardfloatlayout,null);
                    windowManager.addView(touchLayout,layoutParams);
                    windowManager.removeView(button);
                    CheckClick();

                }
            });

        }


    }

    public static void stopservice(Context c){
        Intent intent = new Intent(c,CardTaskFloatingWindow.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.stopService(intent);

    }

    public void CheckClick()
    {
        final View imageView = touchLayout.findViewById(R.id.imageView);
        final View imageview2 = touchLayout.findViewById(R.id.imageView2);
        final View imageview3 = touchLayout.findViewById(R.id.imageView5);
        final View imageview4 = touchLayout.findViewById(R.id.imageView6);
        final View imageview5 = touchLayout.findViewById(R.id.imageView7);
        final View imageview6 = touchLayout.findViewById(R.id.imageView8);
        final View imageview7 = touchLayout.findViewById(R.id.imageView9);
        final View imageview8 = touchLayout.findViewById(R.id.imageView10);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(imageView,"rotation",180f,-50f);
        objectAnimator1.setDuration(600);
        objectAnimator1.start();
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imageview5,"rotation",180f,-20f);
        objectAnimator2.setDuration(600);
        objectAnimator2.start();
        final ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(imageview6,"rotation",180f,10f);
        objectAnimator3.setDuration(600);
        objectAnimator3.start();
        ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(imageview7,"rotation",180f,40f);
        objectAnimator4.setDuration(600);
        objectAnimator4.start();
        ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(imageview8,"rotation",180f,70f);
        objectAnimator5.setDuration(600);
        objectAnimator5.start();
        final List<UsageStats> stats = new painting().CreatAppList(getApplicationContext());
        PackageInit(stats);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",-50f,310f);
                objectAnimator.setDuration(1200);
                objectAnimator.start();
                new painting().TurnToActivity(stats.get(4).getPackageName(),getApplicationContext());
                final ObjectAnimator objectAnimators = ObjectAnimator.ofFloat(imageView,"rotation",-50f,-180f);
                objectAnimators.setDuration(600);
                RotaImage(imageview5,-20f,-180f,600);
                RotaImage(imageview6,10f,-180f,600);
                RotaImage(imageview7,40f,-180f,600);
                RotaImage(imageview8,70f,-180f,600);
                objectAnimators.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) objectAnimators.getAnimatedValue("rotation");
                        if (value == -180f){

                            layoutParams.width = 120;
                            layoutParams.height = 160;
                            int width = windowManager.getDefaultDisplay().getWidth();
                            layoutParams.x =width+-60;
                            int hidth = windowManager.getDefaultDisplay().getHeight();
                            layoutParams.y = hidth/5;
                            try {
                                windowManager.removeView(touchLayout);
                                windowManager.addView(button,layoutParams);
                            }catch (Exception e){

                            }
                        }
                    }
                });
                objectAnimators.start();
            }
        });
        imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",-50f,-180f);
                objectAnimator.setDuration(600);
                RotaImage(imageview5,-20f,-180f,600);
                RotaImage(imageview6,10f,-180f,600);
                RotaImage(imageview7,40f,-180f,600);
                RotaImage(imageview8,70f,-180f,600);
                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) objectAnimator.getAnimatedValue("rotation");
                        if (value == -180f){
                            layoutParams.width = 120;
                            layoutParams.height = 160;
                            int width = windowManager.getDefaultDisplay().getWidth();
                            layoutParams.x =width+-60;
                            int hidth = windowManager.getDefaultDisplay().getHeight();
                            layoutParams.y = hidth/5;
                            try {
                                windowManager.removeViewImmediate(touchLayout);
                                windowManager.addView(button,layoutParams);
                            }catch (Exception e){

                            }
                        }
                    }
                });
                objectAnimator.start();

            }
        });

       imageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",-50f,-180f);
                objectAnimator.setDuration(600);
                RotaImage(imageview5,-20f,-180f,600);
                RotaImage(imageview6,10f,-180f,600);
                RotaImage(imageview7,40f,-180f,600);
                RotaImage(imageview8,70f,-180f,600);
                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) objectAnimator.getAnimatedValue("rotation");
                        if (value == -180f){
                            layoutParams.width = 120;
                            layoutParams.height = 160;
                            int width = windowManager.getDefaultDisplay().getWidth();
                            layoutParams.x =width+-60;
                            int hidth = windowManager.getDefaultDisplay().getHeight();
                            layoutParams.y = hidth/5;

                            try {
                                windowManager.removeView(touchLayout);
                                windowManager.addView(button,layoutParams);
                            }catch (Exception e){

                            }
                        }
                    }
                });
                objectAnimator.start();
            }
        });

        imageview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",-50f,-180f);
                objectAnimator.setDuration(600);
                RotaImage(imageview5,-20f,-180f,600);
                RotaImage(imageview6,10f,-180f,600);
                RotaImage(imageview7,40f,-180f,600);
                RotaImage(imageview8,70f,-180f,600);

                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) objectAnimator.getAnimatedValue("rotation");
                        if (value == -180f){
                            layoutParams.width = 120;
                            layoutParams.height = 160;
                            int width = windowManager.getDefaultDisplay().getWidth();
                            layoutParams.x =width+-60;
                            int hidth = windowManager.getDefaultDisplay().getHeight();
                            layoutParams.y = hidth/5;
                            try {
                                windowManager.removeView(touchLayout);
                                windowManager.addView(button,layoutParams);
                            }catch (Exception e){

                            }
                        }
                    }
                });
                objectAnimator.start();
            }
        });


        imageview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new painting().TurnToActivity(stats.get(3).getPackageName(),getApplicationContext());
                final ObjectAnimator objectAnimators = ObjectAnimator.ofFloat(imageView,"rotation",-50f,-180f);
                objectAnimators.setDuration(600);
                RotaImage(imageview5,-20f,-180f,600);
                RotaImage(imageview6,10f,-180f,600);
                RotaImage(imageview7,40f,-180f,600);
                RotaImage(imageview8,70f,-180f,600);
                objectAnimators.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) objectAnimators.getAnimatedValue("rotation");
                        if (value == -180f){
                            layoutParams.width = 120;
                            layoutParams.height = 160;
                            int width = windowManager.getDefaultDisplay().getWidth();
                            layoutParams.x =width+-60;
                            int hidth = windowManager.getDefaultDisplay().getHeight();
                            layoutParams.y = hidth/5;

                            try {
                                windowManager.removeView(touchLayout);
                                windowManager.addView(button,layoutParams);
                            }catch (Exception e){

                            }
                        }
                    }
                });
                objectAnimators.start();
            }
        });

        imageview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new painting().TurnToActivity(stats.get(2).getPackageName(),getApplicationContext());

                final ObjectAnimator objectAnimators = ObjectAnimator.ofFloat(imageView,"rotation",-50f,-180f);
                objectAnimators.setDuration(600);
                RotaImage(imageview5,-20f,-180f,600);
                RotaImage(imageview6,10f,-180f,600);
                RotaImage(imageview7,40f,-180f,600);
                RotaImage(imageview8,70f,-180f,600);
                objectAnimators.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) objectAnimators.getAnimatedValue("rotation");
                        if (value == -180f){
                            layoutParams.width = 120;
                            layoutParams.height = 160;
                            int width = windowManager.getDefaultDisplay().getWidth();
                            layoutParams.x =width+-60;
                            int hidth = windowManager.getDefaultDisplay().getHeight();
                            layoutParams.y = hidth/5;
                            try {
                                windowManager.removeView(touchLayout);
                                windowManager.addView(button,layoutParams);
                            }catch (Exception e){

                            }
                        }
                    }
                });
                objectAnimators.start();
            }
        });

        imageview7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new painting().TurnToActivity(stats.get(1).getPackageName(),getApplicationContext());

                final ObjectAnimator objectAnimators = ObjectAnimator.ofFloat(imageView,"rotation",-50f,-180f);
                objectAnimators.setDuration(600);
                RotaImage(imageview5,-20f,-180f,600);
                RotaImage(imageview6,10f,-180f,600);
                RotaImage(imageview7,40f,-180f,600);
                RotaImage(imageview8,70f,-180f,600);
                objectAnimators.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) objectAnimators.getAnimatedValue("rotation");
                        if (value == -180f){
                            layoutParams.width = 120;
                            layoutParams.height = 160;
                            int width = windowManager.getDefaultDisplay().getWidth();
                            layoutParams.x =width+-60;
                            int hidth = windowManager.getDefaultDisplay().getHeight();
                            layoutParams.y = hidth/5;
                            try {
                                windowManager.removeView(touchLayout);
                                windowManager.addView(button,layoutParams);
                            }catch (Exception e){

                            }
                        }
                    }
                });
                objectAnimators.start();
            }
        });

        imageview8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new painting().TurnToActivity(stats.get(0).getPackageName(),getApplicationContext());

                final ObjectAnimator objectAnimators = ObjectAnimator.ofFloat(imageView,"rotation",-50f,-180f);
                objectAnimators.setDuration(600);
                RotaImage(imageview5,-20f,-180f,600);
                RotaImage(imageview6,10f,-180f,600);
                RotaImage(imageview7,40f,-180f,600);
                RotaImage(imageview8,70f,-180f,600);
                objectAnimators.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) objectAnimators.getAnimatedValue("rotation");
                        if (value == -180f){
                            layoutParams.width = 120;
                            layoutParams.height = 160;
                            int width = windowManager.getDefaultDisplay().getWidth();
                            layoutParams.x =width+-60;
                            int hidth = windowManager.getDefaultDisplay().getHeight();
                            layoutParams.y = hidth/5;

                            try {
                                windowManager.removeView(touchLayout);
                                windowManager.addView(button,layoutParams);
                            }catch (Exception e){

                            }
                        }
                    }
                });
                objectAnimators.start();
            }
        });
    }

    public void RotaImage(View imageView,float num1,float num2,int duration){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",num1,num2);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
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
    public void PackageInit(List<UsageStats> stats){
        ImageView imageView = touchLayout.findViewById(R.id.imageView);
        ImageView imageView1 = touchLayout.findViewById(R.id.imageView7);
        ImageView imageView2 = touchLayout.findViewById(R.id.imageView8);
        ImageView imageView3 = touchLayout.findViewById(R.id.imageView9);
        ImageView imageView4 = touchLayout.findViewById(R.id.imageView10);
        painting painting = new painting();
        if (stats.size()<4
        )return;
        String test = stats.get(4).getPackageName();
        int i =stats.size();
            Drawable drawable;
            Bitmap bitmap;
            drawable = painting.GetIcon(stats.get(4).getPackageName(),getApplicationContext());
            bitmap = painting.drawableToBitamp(drawable);
            Bitmap createbitmap = CreateMDicon(bitmap);
            imageView.setImageBitmap(createbitmap);
        SetDraw(3,imageView1,stats);
        SetDraw(2,imageView2,stats);
        SetDraw(1,imageView3,stats);
        SetDraw(0,imageView4,stats);
        //painting.TurnToActivity(test,getApplicationContext());
        //Drawable  drawable;
        //drawable = painting.GetIcon(test,getApplicationContext());
        //Bitmap bitmap;
        //bitmap = painting.drawableToBitamp(drawable);
        //view.setImageBitmap(bitmap);
        //imageView.setImageBitmap(bitmap);
    }

    public void SetDraw(int num,ImageView imageView,List<UsageStats> stats){

        painting painting = new painting();
        if (stats == null)return;
        String test = stats.get(num).getPackageName();
        Drawable drawable;
        Bitmap bitmap;
        drawable = painting.GetIcon(stats.get(num).getPackageName(),getApplicationContext());
        bitmap = painting.drawableToBitamp(drawable);
        Bitmap createbitmap = CreateMDicon(bitmap);
        imageView.setImageBitmap(createbitmap);

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
        Bitmap bitmap1 = Bitmap.createBitmap(width2*3,hidth2*2,Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmap1);
        paint.setColor(ColorIcon);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0,width1,hidth1,paint);
        canvas.drawBitmap(bitmap,width2/2,hidth2/2,null);
        canvas.save();
        return bitmap1;
    }

}

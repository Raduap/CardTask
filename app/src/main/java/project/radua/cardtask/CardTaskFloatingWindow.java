package project.radua.cardtask;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.app.Service;
import android.graphics.Paint;
import android.graphics.PixelFormat;


import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.print.PrintDocumentAdapter;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class CardTaskFloatingWindow extends Service{
    public  static boolean isStarted = false;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button button;
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,"rotation",0f,360f);
                objectAnimator.setDuration(600);
                objectAnimator.start();
                painting painting = new painting();
                painting.TurnToActivity("com.baidu.tieba",getApplicationContext());
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




}

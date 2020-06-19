package cn.xue.circleprogressbar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.widget.FrameLayout;

import cn.xue.circleprogress.SplashView;

public class MainActivity extends AppCompatActivity {

    private SplashView splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//        Button btn = this.findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        FrameLayout mMainView = new FrameLayout(this);
//        ContentView contentView = new ContentView(this);
//        mMainView.addView(contentView);
//        splashView = new SplashView(this);
//        mMainView.addView(splashView);
//        setContentView(mMainView);
//        startLoadData();
        setContentView(R.layout.activity_main2);
    }
    Handler handler = new Handler();
    private void startLoadData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //数据加载完毕，进入主界面--->开启后面的两个动画
                splashView.splashDisappear();
            }
        },5000);//延迟时间
    }
}
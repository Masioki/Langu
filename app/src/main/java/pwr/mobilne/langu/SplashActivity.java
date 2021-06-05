package pwr.mobilne.langu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.imageViewLogo);
        TranslateAnimation moveUp = new TranslateAnimation(0F, 0F, 0F, 300F);
        moveUp.setDuration(2000);
        moveUp.setFillAfter(true);
        logo.startAnimation(moveUp);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },4000);

        ProgressBar mProgressBar;
        CountDownTimer mCountDownTimer;

        mProgressBar=findViewById(R.id.progressbar);
        mProgressBar.setProgress(0);
        mCountDownTimer=new CountDownTimer(4000,1000) {
            int i=0;
            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                mProgressBar.setProgress(i*100/3);
            }

            @Override
            public void onFinish() {
                i++;
                mProgressBar.setProgress(100);
            }
        };
        mCountDownTimer.start();

    }
}
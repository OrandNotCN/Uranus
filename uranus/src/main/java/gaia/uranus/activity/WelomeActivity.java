package gaia.uranus.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import gaia.uranus.R;
import gaia.uranus.module.welcome.FloatLeafLayout;
import gaia.uranus.module.welcome.SecretTextView;

public class WelomeActivity extends Activity {
    private FloatLeafLayout mLeafLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);

        mLeafLayout = (FloatLeafLayout) findViewById(R.id.fl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLeafLayout.playLeaf();
                SecretTextView hTextView = (SecretTextView)findViewById(R.id.htextview);
                hTextView.animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                        startActivity(new Intent(WelomeActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                hTextView.show();
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLeafLayout.onDestroy();
    }

}

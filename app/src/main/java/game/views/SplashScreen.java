package game.views;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alessandro.computergraphicsexample.R;

import java.util.concurrent.CountDownLatch;

import game.Waiter;

/**
 * Created by Andrea on 22/04/2015.
 */
public class SplashScreen implements Waiter {

    public static final String LOG_TAG = "SplashScreen";

    private Activity activity;

    private FrameLayout frameLayout;
    private ImageView imageView;
    private TextView textView;

    public SplashScreen(Activity activity, int frameLayoutId, int imageViewId, int textViewId) {
        frameLayout = (FrameLayout) activity.findViewById(frameLayoutId);
        imageView = (ImageView) activity.findViewById(imageViewId);
        textView = (TextView) activity.findViewById(textViewId);

        this.activity = activity;
    }

    public void animate() {
        animateImage();
        animateText();
    }

    private void animateImage() {
        final Animation imageRotation = AnimationUtils.loadAnimation(activity, R.anim.rotator);
        final android.view.animation.Interpolator li = new LinearInterpolator();
        imageRotation.setInterpolator(li);

        imageView.startAnimation(imageRotation);

        imageRotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(imageRotation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void animateText() {
        final Animation fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fadein);
        final Animation fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fadeout);

        textView.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void unleash() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                frameLayout.setVisibility(View.GONE);
            }
        });
    }

}

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
        imageRotation.setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(imageRotation);
            }
        });
    }

    private void animateText() {
        final Animation fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fadein);
        final Animation fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fadeout);

        textView.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                textView.startAnimation(fadeOut);
            }
        });

        fadeOut.setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                textView.startAnimation(fadeIn);
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

    public abstract class AnimationAdapter implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {}
        @Override
        public void onAnimationEnd(Animation animation) {}
        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

}

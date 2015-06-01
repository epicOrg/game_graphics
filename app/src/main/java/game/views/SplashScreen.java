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

import game.miscellaneous.Waiter;

/**
 * Represents the loading screen fo the game. Has an animated image and text.
 * The <code>unleash</code> operation corrisponds to the disappearing of the screen.
 *
 * @author Torlaschi
 * @date 22/04/2015
 */
public class SplashScreen implements Waiter {

    public static final String LOG_TAG = "SplashScreen";

    private Activity activity;

    private FrameLayout frameLayout;
    private ImageView imageView;
    private TextView textView;

    /**
     * Creates a new <code>SplashScreen</code> with a given <code>ImageView</code> and <code>TextView</code>.
     *
     * @param activity      <code>Activity</code> on which thread the <code>SplashScreen</code> works.
     * @param frameLayoutId id of the <code>FrameLayout</code> used to control the disappearing of the <code>SplashScreen</code>.
     * @param imageViewId   id of the <code>ImageView</code> to draw the image on the <code>SplashScreen</code>.
     * @param textViewId    id of the <code>TextView</code> to draw text on the <code>SplashScreen</code>.
     */
    public SplashScreen(Activity activity, int frameLayoutId, int imageViewId, int textViewId) {
        frameLayout = (FrameLayout) activity.findViewById(frameLayoutId);
        imageView = (ImageView) activity.findViewById(imageViewId);
        textView = (TextView) activity.findViewById(textViewId);

        this.activity = activity;
    }

    /**
     * Starts the <code>SplashScreen</code> animation.
     */
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

    public abstract class AnimationAdapter implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

}

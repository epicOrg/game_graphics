package game.net.interaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

/**
 * Shows an animation during some operation performing.
 *
 * @author Micieli
 * @date 31/03/2015
 */
public class ProgressShower {

    private View mLoginFormView;
    private View mProgressView;
    private int shortAnimTime;

    /**
     * Creates a new <code>ProgressShower</code>, that shows animation during progresses.
     *
     * @param mProgressView  <code>View</code> that represents operation progressing.
     * @param mLoginFormView <code>View</code> associated with the current operation.
     * @param shortAnimTime  time of the animation.
     */
    public ProgressShower(View mProgressView, View mLoginFormView, int shortAnimTime) {
        this.mProgressView = mProgressView;
        this.mLoginFormView = mLoginFormView;
        this.shortAnimTime = shortAnimTime;
    }

    /**
     * Sets visibility and animations of mLoginFormView and mProgressView.
     *
     * @param show whether to show the mLoginFormView and mProgressView.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

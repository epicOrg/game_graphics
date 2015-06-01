package game.views;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alessandro.computergraphicsexample.R;

import game.miscellaneous.Waiter;

/**
 * Represents a message over the game screen.
 *
 * @author Torlaschi
 * @date 12/05/2015
 */
public class MessageScreen implements Waiter {

    public static final String LOG_TAG = "MessageScreen";

    private Activity activity;
    private int textColor;
    private int backgroundColor;
    private LinearLayout container;

    private TextView textView;

    /**
     * Creates a new <code>MessageScreen</code> with given color and layout.
     *
     * @param activity        <code>Activity</code> on which thread the <code>MessageScreen</code> should work.
     * @param backgroundColor color of the background of the <code>MessageScreen</code>.
     * @param container       Layout to which add the <code>MessageScreen</code>.
     */
    public MessageScreen(Activity activity, int backgroundColor, LinearLayout container) {
        this.activity = activity;
        this.backgroundColor = backgroundColor;
        this.container = container;

        setup();
    }

    private void setup() {
        textView = new TextView(activity);
        textView.setTextColor(textColor);
        textView.setTextSize(activity.getResources().getDimension(R.dimen.message_screen_text));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        textView.setLayoutParams(layoutParams);
    }

    /**
     * Sets the text message of the <code>MessageScreen</code>.
     *
     * @param text  Text of the message.
     * @param color color of the message text.
     */
    public void setText(String text, int color) {
        textColor = color;

        textView.setText(text);
        textView.setTextColor(textColor);
    }

    public void show() {
        Log.d(LOG_TAG, "show");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.addView(textView);
                container.setBackgroundColor(backgroundColor);
                container.bringToFront();
            }
        });
    }

    public void hide() {
        Log.d(LOG_TAG, "hide");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.removeAllViews();
                container.setBackgroundColor(Color.TRANSPARENT);
            }
        });
    }

    @Override
    public void unleash() {
        show();
    }
}

package game.views;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import game.Waiter;

/**
 * @author Torlaschi
 * @date 12/05/2015
 */
public class MessageScreen implements Waiter {

    public static final String LOG_TAG = "MessageScreen";

    private Activity activity;
    private String message;
    private int textColor;
    private int backgroundColor;
    private LinearLayout container;

    private TextView textView;

    public MessageScreen(Activity activity, int backgroundColor, LinearLayout container) {
        this.activity = activity;
        this.backgroundColor = backgroundColor;
        this.container = container;

        setup();
    }

    private void setup() {
        textView = new TextView(activity);
        textView.setText(message);
        textView.setTextColor(textColor);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        textView.setLayoutParams(layoutParams);
    }

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

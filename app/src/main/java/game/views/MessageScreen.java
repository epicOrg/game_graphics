package game.views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Andrea on 12/05/2015.
 */
public class MessageScreen {

    private Context context;
    private String message;
    private int textColor;
    private int backgroundColor;
    private LinearLayout container;

    private TextView textView;

    public MessageScreen(Context context, int backgroundColor, LinearLayout container) {
        this.context = context;
        this.backgroundColor = backgroundColor;
        this.container = container;

        setup();
    }

    private void setup() {
        textView = new TextView(context);
        textView.setText(message);
        textView.setTextColor(textColor);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        textView.setLayoutParams(layoutParams);

        container.addView(textView);
        container.setBackgroundColor(backgroundColor);
        container.setVisibility(View.GONE);
    }

    public void setText(String text, int color) {
        textColor = color;

        textView.setText(text);
        textView.setTextColor(textColor);
    }

    public void show() {
        container.setVisibility(View.VISIBLE);
        container.bringToFront();
    }

    public void hide() {
        container.setVisibility(View.GONE);
    }

}

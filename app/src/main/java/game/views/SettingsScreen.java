package game.views;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.alessandro.computergraphicsexample.R;

/**
 * Created by Andrea on 12/05/2015.
 */
public class SettingsScreen {

    public static final String LOG_TAG = "SettingsScreen";

    private Activity activity;
    private LinearLayout container;

    private SettingsScreen settingsScreen;

    public SettingsScreen(Activity activity, LinearLayout container) {
        this.activity = activity;
        this.container = container;

        settingsScreen = this;

        setup();
    }

    private void setup() {
        Button muteButton = (Button) activity.findViewById(R.id.game_menu_mute_unmute);
        Button quitButton = (Button) activity.findViewById(R.id.game_menu_quit);
        Button cancelButton = (Button) activity.findViewById(R.id.game_menu_cancel);

        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Muting/UnMuting..", Toast.LENGTH_SHORT).show();
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Quitting..", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsScreen.hide();
            }
        });
    }

    public void show() {
        Log.d(LOG_TAG, "show");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.setVisibility(View.VISIBLE);
                container.bringToFront();
            }
        });
    }

    public void hide() {
        Log.d(LOG_TAG, "hide");

        container.setVisibility(View.GONE);
    }

}

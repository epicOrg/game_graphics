package game.views;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alessandro.computergraphicsexample.R;

import game.GameManager;
import game.Team;
import game.player.Player;

/**
 * Created by Andrea on 12/05/2015.
 */
public class SettingsScreen {

    public static final String LOG_TAG = "SettingsScreen";

    private Activity activity;
    private LinearLayout container;

    private SettingsScreen settingsScreen;
    private GameManager gameManager;

    public SettingsScreen(Activity activity, LinearLayout container) {
        this.activity = activity;
        this.container = container;

        settingsScreen = this;

        setup();
    }

    private void setup() {
        gameManager = GameManager.getInstance();

        SettingsContainer settingsContainer = new SettingsContainer(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        settingsContainer.setLayoutParams(layoutParams);

        int padding = activity.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        settingsContainer.setPadding(padding, padding, padding, padding);
        settingsContainer.setOrientation(LinearLayout.VERTICAL);

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

        container.removeAllViews();
        settingsContainer.addView(muteButton);
        settingsContainer.addView(quitButton);
        settingsContainer.addView(cancelButton);

        for (Team t : gameManager.getRoom().getTeams()) {
            ListView listView = new ListView(activity);
            settingsContainer.addView(listView);
            ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(activity, android.R.layout.simple_list_item_1, t.getPlayers());

            TextView textView = new TextView(activity);
            textView.setText(t.getName());
            listView.addHeaderView(textView);
            listView.setAdapter(adapter);
        }

        container.addView(settingsContainer);
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

    private class SettingsContainer extends LinearLayout {

        public SettingsContainer(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }
    }
}

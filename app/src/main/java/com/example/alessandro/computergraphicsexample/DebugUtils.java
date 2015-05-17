package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import game.Team;
import game.graphics.Map;
import game.graphics.Obstacle;
import game.graphics.Wall;
import game.physics.Circle;
import game.physics.Square;
import game.player.Player;
import game.player.PlayerStatus;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 22/04/2015.
 */
public class DebugUtils {

    public static final String LOG_TAG = "DebugUtils";

    // Metodi fatti male di proposito. Tanto verrano cancellati.

    public static void fillMap(Map map, int groundDim) {
        double h = 2.5;
        int texture_id = R.drawable.wall_texture_03;
        map.addObjects(new Wall(new Square(new SFVertex3f(-9.25, -1, -2.75), 1.5, h, 14.5), texture_id),
                new Wall(new Square(new SFVertex3f(-6.75, -1, -4.25), 3.5, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(-1, -1, -9.25), 15, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(7.25, -1, -2.75), 1.5, h, 14.5), texture_id),
                new Wall(new Square(new SFVertex3f(4, -1, -4.25), 5, h, 1.5), texture_id),
                new Obstacle(new Circle(new SFVertex3f(-0.7, -1, -4.25), 0.3), h, R.drawable.obstacle_texture_01),
                new Obstacle(new Circle(new SFVertex3f(-2.9, -1, -4.25), 0.3), h, R.drawable.obstacle_texture_01),
                new Wall(new Square(new SFVertex3f(3.75, -1, 3.75), 5.5, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(-4.75, -1, 3.75), 7.5, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(-1, -1, -0.25), 10, h, 1.5), texture_id)
        );

        final double h2 = 2;
        map.addObjects(new Wall(new Square(new SFVertex3f(0, -1, groundDim), 2 * groundDim, h2, 2), R.drawable.hedge_texture_02_1));
        map.addObjects(new Wall(new Square(new SFVertex3f(0, -1, -groundDim), 2 * groundDim, h2, 2), R.drawable.hedge_texture_02_1));
        map.addObjects(new Wall(new Square(new SFVertex3f(groundDim, -1, 0), 2, h2, 2 * groundDim - 2), R.drawable.hedge_texture_02_1));
        map.addObjects(new Wall(new Square(new SFVertex3f(-groundDim, -1, 0), 2, h2, 2 * groundDim - 2), R.drawable.hedge_texture_02_1));
    }

    public static void startGame(Activity activity, ArrayList<Player> otherPlayers, Map map, int groundWidth, int groundHeight, CountDownLatch startSignal) {
        SFVertex3f position = new SFVertex3f(5, 0.5f, -7);
        SFVertex3f direction = new SFVertex3f(-1, -0.25f, 0);

        Player me = new Player(new PlayerStatus(direction, new Circle(position, 0.75)), "Me");

        Log.d(LOG_TAG, "Starting GraphicsView..");
        LinearLayout graphicsContainerLayout = (LinearLayout) activity.findViewById(R.id.graphics_view_container);
        GraphicsView graphicsView = new GraphicsView(activity, me, new ArrayList<Team>(), map, startSignal, groundWidth, groundHeight, null); //TODO Probabilmente non va..
        graphicsContainerLayout.addView(graphicsView);
    }

}

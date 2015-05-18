package com.example.alessandro.computergraphicsexample;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import game.Team;
import game.controls.ButtonMaster;
import game.controls.ButtonsControl;
import game.generators.FundamentalGenerator;
import game.generators.GroundGenerator;
import game.generators.MoveButtonsGenerator;
import game.generators.SettingsButtonsGenerator;
import game.graphics.Camera;
import game.graphics.Map;
import game.graphics.MaterialKeeper;
import game.graphics.PlayerView;
import game.graphics.ShadersKeeper;
import game.graphics.Sky;
import game.graphics.TextLabel;
import game.graphics.TextureKeeper;
import game.listeners.DirectionDirectionMoveListener;
import game.listeners.DirectionMoveListenerInterface;
import game.listeners.PositionMoveListenerInterface;
import game.listeners.PositionMoveListenerXZWithCollisions;
import game.listeners.TouchListener;
import game.listeners.TouchListenerInterface;
import game.physics.CollisionMediator;
import game.player.Player;
import game.views.SettingsScreen;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLState;
import sfogl2.SFOGLStateEngine;
import sfogl2.SFOGLSystemState;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Alessandro on 13/03/15.
 */
public class GraphicsView extends GLSurfaceView {

    public static final String LOG_TAG = "GraphicsView";

    private CountDownLatch startSignal;
    private Camera camera;
    private Context context;

    private Player me;
    private ArrayList<Team> teams;

    private TouchListenerInterface touchListener;
    private PositionMoveListenerInterface positionMoveListener;
    private DirectionMoveListenerInterface directionMoveListener;

    private SettingsScreen settingsScreen;

    private CollisionMediator cm;
    private Map map;

    private SFOGLState sfs;

    private int groundWidth, groundHeight;
    private ArrayList<PlayerView> playerViews = new ArrayList<>();

    public GraphicsView(Context context, Player me, ArrayList<Team> teams, Map map, CountDownLatch startSignal, int groundWidth, int groundHeight, SettingsScreen settingsScreen) {
        super(context);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        this.context = context;
        this.me = me;
        this.teams = teams;
        this.map = map;
        this.startSignal = startSignal;
        this.groundWidth = groundWidth;
        this.groundHeight = groundHeight;

        camera = new Camera(me, 0.125f, 200, 80);
        cm = new CollisionMediator();

        this.settingsScreen = settingsScreen;

        sfs = SFOGLStateEngine.glEnable(GL_CULL_FACE);

        positionMoveListener = new PositionMoveListenerXZWithCollisions(me.getStatus(), cm);
        directionMoveListener = new DirectionDirectionMoveListener(me.getStatus().getDirection(), getWidth(), getHeight());

        setRenderer(new GraphicsRenderer());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void drawPlayers() {
        for (PlayerView view : playerViews)
            view.draw();
    }

    @Override
    public void onPause() {
        super.onPause();

        ShadersKeeper.clear();
        MaterialKeeper.getInstance().clear();
        TextureKeeper.getInstance().clear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchListener != null)
            touchListener.onTouchEvent(event);

        return true;
    }

    public void onGameGo() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                touchListener.setReadyToPlay(true);
            }
        });
    }

    public class GraphicsRenderer implements Renderer {

        private ShadingProgram program;
        private Sky sky;
        private Node node, groundNode;
        private ButtonMaster buttonMaster;
        private ArrayList<TextLabel> labels = new ArrayList<>();

        private float t = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.d(LOG_TAG, "onSurfaceCreated");

            ShadersKeeper.loadPipelineShaders(context);
            program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
            TextureKeeper.getInstance().reload(context);
            glEnable(GLES20.GL_BLEND);
            glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            for (Team team : teams) {
                for (Player player : team.getPlayers()) {
                    if (!player.getName().equals(me.getName())) {
                        playerViews.add(new PlayerView(player, context, R.drawable.rabbit_texture));
                        labels.add(new TextLabel(0.6f, 0.6f, 0.5f, me.getStatus().getDirection(), player.getStatus().getPosition(), player.getName(), team.getColor()));
                    }
                }
            }

            groundNode = new GroundGenerator(FundamentalGenerator.getModel(context, program, R.drawable.ground_texture_04, "Ground.obj"))
                    .getGroundNode(0, 0, groundWidth, groundHeight, -1);
            map.loadMap(cm, context);
            sky = new Sky(context, program, me.getStatus().getPosition());

            createMonkeys();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, final int width, final int height) {
            Log.d(LOG_TAG, "onSurfaceChanged");

            glViewport(0, 0, width, height);
            camera.updateMatrices((float) width / height);
            directionMoveListener.update(width, height);

            buttonMaster = new ButtonMaster();
            MoveButtonsGenerator moveButtonsGenerator = new MoveButtonsGenerator(context, program, buttonMaster, positionMoveListener);
            moveButtonsGenerator.generate(new SFVertex3f(-1f, -0.50f, 1), 0.15f, 2);
            SettingsButtonsGenerator settingsButtonsGenerator = new SettingsButtonsGenerator(context, program, buttonMaster, settingsScreen);
            settingsButtonsGenerator.generate(new SFVertex3f(+1.1f, +0.6f, 0), 0.15f);

            final ButtonsControl buttonsControl = new ButtonsControl(context, program, camera.getOrthoMatrix(), buttonMaster);

            queueEvent(new Runnable() {
                @Override
                public void run() {
                    buttonsControl.update(width, height);
                    touchListener = new TouchListener(buttonsControl, directionMoveListener);
                }
            });

            startSignal.countDown();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            sky.draw();
            program.setupProjection(camera.getResultMatrix());
            SFOGLSystemState.cleanupColorAndDepth(0, 0, 1, 1);
            sfs.applyState();
            drawMonkeys();

            groundNode.draw();
            drawPlayers();
            map.draw();
            sky.draw();

            for (TextLabel label : labels) {
                label.draw();
            }

            program.setupProjection(camera.getOrthoMatrix());

            buttonMaster.draw();
        }


        private void createMonkeys() {
            Model monkeyModel = FundamentalGenerator.getModel(context, program, R.drawable.animal_texture_01, "Monkey.obj");

            node = new Node();
            node.setModel(monkeyModel);
            node.getRelativeTransform().setPosition(0.5f, 0.5f, -6.5f);

            Node anotherNode = new Node();

            anotherNode.setModel(monkeyModel);
            anotherNode.getRelativeTransform().setPosition(1, 1, 0);
            anotherNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.3f, 0.2f, 0.1f));
            node.getSonNodes().add(anotherNode);
        }

        private void drawMonkeys() {
            t += 0.01f;
            float rotation = 0.2f + t;
            float scaling = 0.3f;
            SFMatrix3f matrix3f = SFMatrix3f.getScale(scaling, scaling, scaling);
            matrix3f = matrix3f.MultMatrix(SFMatrix3f.getRotationY(rotation));
            node.getRelativeTransform().setMatrix(matrix3f);
            node.updateTree(new SFTransform3f());
            node.draw();
        }

    }

}


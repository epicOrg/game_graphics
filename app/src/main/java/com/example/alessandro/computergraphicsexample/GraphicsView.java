package com.example.alessandro.computergraphicsexample;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import game.controls.ButtonMaster;
import game.controls.ButtonsControl;
import game.data.Team;
import game.generators.FundamentalGenerator;
import game.generators.GroundGenerator;
import game.generators.MoveButtonsGenerator;
import game.generators.SettingsButtonsGenerator;
import game.graphics.Camera;
import game.graphics.Map;
import game.graphics.MaterialKeeper;
import game.graphics.MeshKeeper;
import game.graphics.ModelKeeper;
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
import game.miscellaneous.FloatLoader;
import game.physics.CollisionMediator;
import game.player.Player;
import game.views.SettingsScreen;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLState;
import sfogl2.SFOGLStateEngine;
import sfogl2.SFOGLSystemState;
import shadow.math.SFVertex3f;

import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

/**
 *
 */
public class GraphicsView extends GLSurfaceView {

    public static final String LOG_TAG = "GraphicsView";

    private CountDownLatch startSignal;
    private Context context;

    private Player me;
    private ArrayList<Team> teams;

    private TouchListenerInterface touchListener;
    private PositionMoveListenerInterface positionMoveListener;
    private DirectionMoveListenerInterface directionMoveListener;

    private SettingsScreen settingsScreen;

    private CollisionMediator cm;
    private Map map;

    private int groundWidth, groundHeight;
    private ArrayList<PlayerView> playerViews = new ArrayList<>();
    private FloatLoader fl;
    private Resources res;

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
        cm = new CollisionMediator();

        this.settingsScreen = settingsScreen;

        fl = new FloatLoader(context);
        res = context.getResources();

        positionMoveListener = new PositionMoveListenerXZWithCollisions(me.getStatus(), cm, fl.getFloat(R.dimen.moveSpeed));
        directionMoveListener = new DirectionDirectionMoveListener(me.getStatus().getDirection(), getWidth(), getHeight());

        setRenderer(new GraphicsRenderer());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        ShadersKeeper.clear();
        MeshKeeper.MESH_KEEPER.clear();
        MaterialKeeper.MATERIAL_KEEPER.clear();
        TextureKeeper.TEXTURE_KEEPER.clear();
        ModelKeeper.MODEL_KEEPER.clear();
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
        private Node groundNode;
        private ButtonMaster buttonMaster;
        private ArrayList<TextLabel> labels = new ArrayList<>();
        private Camera camera;

        private SFOGLState sfsWithCulling = SFOGLStateEngine.glEnable(GL_CULL_FACE);
        private SFOGLState sfsWithoutCulling = SFOGLStateEngine.glDisable(GL_CULL_FACE);

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.d(LOG_TAG, "onSurfaceCreated");

            ShadersKeeper.loadPipelineShaders(context);
            program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
            TextureKeeper.TEXTURE_KEEPER.reload(context);
            glEnable(GLES20.GL_BLEND);
            glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            camera = new Camera(me, fl.getFloat(R.dimen.camZnear), fl.getFloat(R.dimen.camZfar), fl.getFloat(R.dimen.camAngle));

            for (Team team : teams) {
                for (Player player : team.getPlayers()) {
                    if (!player.getName().equals(me.getName())) {
                        playerViews.add(new PlayerView(player, context, R.drawable.rabbit_texture));
                        labels.add(new TextLabel(res.getInteger(R.integer.labelTextQuality), fl.getFloat(R.dimen.labelTextHeight),
                                fl.getFloat(R.dimen.labelHeight), me.getStatus().getDirection(),
                                player.getStatus().getPosition(), player.getName(), team.getColor()));
                    }
                }
            }

            groundNode = new GroundGenerator(FundamentalGenerator.getModel(context, program, R.drawable.ground_texture_02, "Ground.obj")).getGroundNode(0, 0, groundWidth, groundHeight, -1);
            map.loadMap(cm, context);
            sky = new Sky(context, program, me.getStatus().getPosition());
        }

        @Override
        public void onSurfaceChanged(GL10 gl, final int width, final int height) {
            Log.d(LOG_TAG, "onSurfaceChanged");

            glViewport(0, 0, width, height);
            camera.updateMatrices((float) width / height);
            directionMoveListener.update(width, height);

            buttonMaster = new ButtonMaster();
            MoveButtonsGenerator moveButtonsGenerator = new MoveButtonsGenerator(context, program, buttonMaster, positionMoveListener);
            moveButtonsGenerator.generate(new SFVertex3f(fl.getFloat(R.dimen.xMove), fl.getFloat(R.dimen.yMove), fl.getFloat(R.dimen.zMove)), fl.getFloat(R.dimen.scaleMove), fl.getFloat(R.dimen.distanceMove));
            SettingsButtonsGenerator settingsButtonsGenerator = new SettingsButtonsGenerator(context, program, buttonMaster, settingsScreen);
            settingsButtonsGenerator.generate(new SFVertex3f(fl.getFloat(R.dimen.xSet), fl.getFloat(R.dimen.ySet), fl.getFloat(R.dimen.zSet)), fl.getFloat(R.dimen.scaleSet));

            final ButtonsControl buttonsControl = new ButtonsControl(program, camera.getOrthoMatrix(), buttonMaster);

            queueEvent(new Runnable() {
                @Override
                public void run() {
                    buttonsControl.update(width, height);
                    touchListener = new TouchListener(buttonsControl, directionMoveListener, res.getInteger(R.integer.timeSleep));
                }
            });

            startSignal.countDown();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            program.setupProjection(camera.getResultMatrix());
            SFOGLSystemState.cleanupColorAndDepth(0, 0, 1, 1);

            map.draw();

            sfsWithCulling.applyState();
            drawPlayers();

            sfsWithoutCulling.applyState();
            groundNode.draw();
            sky.draw();
            for (TextLabel label : labels) {
                label.draw();
            }

            program.setupProjection(camera.getOrthoMatrix());
            buttonMaster.draw();
        }

        private void drawPlayers() {
            for (PlayerView view : playerViews)
                view.draw();
        }

    }

}


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

import game.controls.ButtonMaster;
import game.controls.ButtonsControl;
import game.generators.FundamentalGenerator;
import game.generators.GroundGenerator;
import game.generators.MoveButtonsGenerator;
import game.graphics.Camera;
import game.graphics.Map;
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
    private ArrayList<Player> otherPlayers;
    private TouchListenerInterface touchListener;
    private PositionMoveListenerInterface positionMoveListener;
    private DirectionMoveListenerInterface directionMoveListener;
    private CollisionMediator cm;
    private Map map;
    private boolean isReadyForTouch = false;
    private SFOGLState sfs;

    private int groundWidth, groundHeight;
    private ArrayList<PlayerView> playerViews = new ArrayList();

    public GraphicsView(Context context, Player me, ArrayList<Player> otherPlayers, Map map, CountDownLatch startSignal, int groundWidth, int groundHeight) {
        super(context);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        this.context = context;
        this.me = me;
        this.otherPlayers = otherPlayers;
        this.map = map;
        this.startSignal = startSignal;
        this.groundWidth = groundWidth;
        this.groundHeight = groundHeight;

        camera = new Camera(me, 0.125f, 128, 80);
        cm = new CollisionMediator();
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isReadyForTouch)
            touchListener.onTouchEvent(event);

        return true;
    }

    public class GraphicsRenderer implements Renderer {

        private ShadingProgram program;
        private Sky sky;
        private Node node, groundNode;
        private ButtonMaster buttonMaster;

        private float t = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.d(LOG_TAG, "onSurfaceCreated");

            ShadersKeeper.loadPipelineShaders(context);
            program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
            TextureKeeper.getInstance().reload(context);
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            label=new TextLabel(0.5f,0.5f,new SFVertex3f(2, 0.5f, -7),new SFVertex3f(0,0,-1),"epicOrg");

            for (Player player : otherPlayers) {
                playerViews.add(new PlayerView(player, context, R.drawable.rabbit_texture));
            }

            groundNode = new GroundGenerator(FundamentalGenerator.getModel(context, program, R.drawable.ground_texture_04, "Ground.obj")).getGroundNode(0, 0, groundWidth, groundHeight, -1);
            map.loadMap(cm, context);
            sky = new Sky(context, program, me.getStatus().getPosition());

            createMonkeys();
        }

        private TextLabel label;

        @Override
        public void onSurfaceChanged(GL10 gl, final int width, final int height) {
            Log.d(LOG_TAG, "onSurfaceChanged");

            glViewport(0, 0, width, height);
            camera.updateMatrices((float) width / height);
            directionMoveListener.update(width, height);

            buttonMaster = new ButtonMaster(null, 0.15f, new SFVertex3f(-1f, -0.50f, 1));
            Model arrowModel = FundamentalGenerator.getModel(context, program, R.drawable.arrow_texture_02, "Arrow.obj");
            MoveButtonsGenerator.generate(buttonMaster, arrowModel, positionMoveListener);
            final ButtonsControl buttonsControl = new ButtonsControl(context, program, camera.getOrthoMatrix(), buttonMaster);

            touchListener = new TouchListener(buttonsControl, directionMoveListener);

            queueEvent(new Runnable() {
                @Override
                public void run() {
                    buttonsControl.update(width, height);
                    isReadyForTouch = true;
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
            label.draw();

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


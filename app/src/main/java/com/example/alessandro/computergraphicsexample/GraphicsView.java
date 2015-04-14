package com.example.alessandro.computergraphicsexample;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import game.controls.ButtonsControl;
import game.generators.ButtonsGenerator;
import game.generators.FundamentalGenerator;
import game.generators.GroundGenerator;
import game.graphics.Camera;
import game.graphics.Map;
import game.graphics.Sky;
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
import sfogl2.SFOGLSystemState;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;

import static android.opengl.GLES20.glViewport;

/**
 * Created by Alessandro on 13/03/15.
 */
public class GraphicsView extends GLSurfaceView {

    public static final String LOG_TAG = "GraphicsView";

    private CountDownLatch startSignal;
    private Camera camera;
    private GLSurfaceView surfaceView;
    private Context context;
    private Player me;
    private ArrayList<Player> otherPlayers;
    private TouchListenerInterface touchListener;
    private PositionMoveListenerInterface positionMoveListener;
    private DirectionMoveListenerInterface directionMoveListener;
    private CollisionMediator cm;
    private Map map;
    private boolean isReadyForTouch = false;

    public GraphicsView(Context context, Player me, ArrayList<Player> otherPlayers, Map map, CountDownLatch startSignal) {
        super(context);
        setEGLContextClientVersion(2);

        this.startSignal = startSignal;
        this.surfaceView = this;
        this.context = context;
        this.me = me;
        this.otherPlayers = otherPlayers;
        this.camera = new Camera(me, 0.125f, 128, 80);

        cm = new CollisionMediator();
        this.map=map;

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(new GraphicsRenderer());

        positionMoveListener = new PositionMoveListenerXZWithCollisions(me.getStatus(), cm);
        directionMoveListener = new DirectionDirectionMoveListener(me.getStatus().getDirection(), getWidth(), getHeight());
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
        private ArrayList<Node> buttonsNodes;

        private float t = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            ShadersKeeper.loadPipelineShaders(context);
            program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
            TextureKeeper.reload(context);

            groundNode = new GroundGenerator(FundamentalGenerator.getModel(context, program, R.drawable.ground_texture_02, "Ground.obj")).getGroundNode(0, 0, 20, 20, -1);
            map.loadMap(cm,context);
            sky = new Sky(context, program, me.getStatus().getPosition());

            createMonkeys();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, final int width, final int height) {
            glViewport(0, 0, width, height);
            camera.updateMatrices((float) width / height);
            directionMoveListener.update(width, height);

            Model arrowModel = FundamentalGenerator.getModel(context, program, R.drawable.arrow_texture_02, "Arrow.obj");
            final ButtonsGenerator buttonsGenerator = new ButtonsGenerator(arrowModel);
            final ButtonsControl buttonsControl = new ButtonsControl(context, program, camera.getOrthoMatrix(), buttonsGenerator.getLeftNode(), buttonsGenerator.getRightNode(), buttonsGenerator.getUpNode(), buttonsGenerator.getDownNode());

            touchListener = new TouchListener(surfaceView, buttonsControl, positionMoveListener, directionMoveListener);
            buttonsNodes = buttonsGenerator.getButtons();

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
            program.setupProjection(camera.getResultMatrix());
            SFOGLSystemState.cleanupColorAndDepth(0, 0, 1, 1);

            drawMonkeys();

            groundNode.draw();
            map.draw();
            sky.draw();

            program.setupProjection(camera.getOrthoMatrix());

            for (Node buttonNode : buttonsNodes) {
                buttonNode.draw();
            }
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


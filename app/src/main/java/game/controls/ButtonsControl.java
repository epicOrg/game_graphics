package game.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import game.graphics.MaterialKeeper;
import sfogl.integration.Material;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLSystemState;

/**
 * It allows to control if the {@link Button}, which are associated with a {@link ButtonMaster},
 * are pressed through colorpicking.
 *
 * @see Button
 */
public class ButtonsControl {

    public static final String LOG_TAG = "ButtonsControl";

    private HashMap<Integer, ButtonControlObject> buttonsMap = new HashMap<>();

    private ShadingProgram program;
    private float[] orthoMatrix;

    private Bitmap buttonsBitmap;
    private ButtonMaster buttonMaster;

    /**
     * Creates a nwe <code>ButtonControl</code>.
     *
     * @param program      <code>ShadingProgram</code> to be used.
     * @param orthoMatrix  2D projection matrix.
     * @param buttonMaster <code>ButtonMaster</code> which contains the <code>Button</code> to be controlled.
     */
    public ButtonsControl(ShadingProgram program, float[] orthoMatrix, ButtonMaster buttonMaster) {
        this.program = program;
        this.orthoMatrix = orthoMatrix;
        this.buttonMaster = buttonMaster;
        int n = 0;
        for (Button b : buttonMaster.getButtons()) {
            n++;
            int color = generateNewColor(n);
            buttonsMap.put(color, new ButtonControlObject(b, MaterialKeeper.MATERIAL_KEEPER.getColorMaterial(program, color)));
        }
    }

    /**
     * Sets (and updates) the <code>ButtonControl</code> if screen dimension has changed.
     * Needs to be called at the beginning.
     *
     * @param width  Screen width.
     * @param height Screen height.
     */
    public void update(int width, int height) {
        program.setupProjection(orthoMatrix);
        SFOGLSystemState.cleanupColorAndDepth(0, 0, 0, 1);
        drawForColorPicking();

        int size = width * height;
        ByteBuffer buffer = ByteBuffer.allocateDirect(size * 4);
        buffer.order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
        int pixelsBuffer[] = new int[size];
        buffer.asIntBuffer().get(pixelsBuffer);
        buffer = null;

        for (int i = 0; i < size; ++i) {
            pixelsBuffer[i] = ((pixelsBuffer[i] & 0xff00ff00)) | ((pixelsBuffer[i] & 0x000000ff) << 16) | ((pixelsBuffer[i] & 0x00ff0000) >> 16);
        }

        buttonsBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        buttonsBitmap.setPixels(pixelsBuffer, size - width, -width, 0, 0, width, height);
    }

    /**
     * @return 'true' if the point (touchX,touchY) is within a <code>Button</code>.
     */
    public boolean isInsideAButton(float touchX, float touchY) {
        return getColorAt(touchX, touchY) != Color.rgb(0, 0, 0);
    }

    /**
     * @return The <code>Button</code> in the specified position (touchX,touchY).
     */
    public Button getPressedButton(float touchX, float touchY) {
        return buttonsMap.get(getColorAt(touchX, touchY)).button;
    }

    private int generateNewColor(int n) {
        return Color.rgb(n, 0, 0);
    }

    private void drawForColorPicking() {
        for (ButtonControlObject o : buttonsMap.values()) {
            savePreviousMaterialDrawRestore(buttonMaster.getButtonNode(o.button), o.material);
        }
    }

    private void savePreviousMaterialDrawRestore(Node node, Material material) {
        Material tmpMaterial = node.getModel().getMaterialComponent();
        node.getModel().setMaterialComponent(material);
        node.draw();
        node.getModel().setMaterialComponent(tmpMaterial);
    }

    private int getColorAt(float touchX, float touchY) {
        return buttonsBitmap.getPixel((int) touchX, (int) touchY) | 0xff000000;
    }

    private class ButtonControlObject {

        public Button button;
        public Material material;

        public ButtonControlObject(Button button, Material material) {
            this.button = button;
            this.material = material;
        }

    }

}

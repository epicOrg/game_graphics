package game.graphics;

import game.player.Player;

import static android.opengl.Matrix.frustumM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.setLookAtM;

/**
 * Video camera: it manages the matrices for 3D and 2D projections following the position of a Player.
 *
 * @author De Pace
 */
public class Camera {

    private final float[] orthoMatrix=new float[16],
            resultMatrix=new float[16];
    private float[] projectionMatrix=new float[16];
    private Player me;
    private float znear,zfar,k;

    /**
     * Creates a new camera object from a Player position, on the strength of its direction.
     *
     * @param player The Player to be followed.
     * @param znear Minimum distance between an object and the camera
     *              to have the object displayed in the 3D projection.
     * @param zfar Maximum distance between an object and the camera
     *              to have the object displayed in the 3D projection.
     * @param angle Vision angle of the 3D projection between up-down and right-left, expressed in degrees.
     */
    public Camera(Player player, float znear, float zfar, float angle){
        this.me=player;
        this.znear=znear;
        this.zfar=zfar;
        this.k=znear*(float)Math.tan(angle*Math.PI/360);
    }

    /**
     * Updated the matrices of the 3D and 2D projection.
     * Has to be invoked if screen dimension changes.
     *
     * @param ratio Screen width/height ratio.
     */
    public void updateMatrices(float ratio){
        setProjection(ratio);
        setOrthoMatrix(ratio);
    }

    /**
     * @return 2D projection matrix.
     */
    public float[] getOrthoMatrix(){
        return orthoMatrix;
    }

    /**
     * @return Current 3D projection matrix.
     */
    public float[] getResultMatrix(){
        setResultMatrix();
        return resultMatrix;
    }

    private void setOrthoMatrix(float ratio) {
        if (ratio>1)
            orthoM(orthoMatrix, 0, -ratio, ratio, -1, 1, -1, 1);
        else
            orthoM(orthoMatrix, 0, -1, 1, -(1 / ratio), (1 / ratio), -1, 1);
    }

    private void setProjection(float ratio) {
        if (ratio>1)
            frustumM(projectionMatrix, 0, -k, k, -(k/ratio), (k/ratio), znear, zfar);
        else
            frustumM(projectionMatrix, 0, -k*ratio, k*ratio, -k, k, znear, zfar);
    }

    private void setResultMatrix() {
        final float[] viewMatrix = new float[16];
        setViewMatrix(viewMatrix);
        multiplyMM(resultMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    private void setViewMatrix(float[] viewMatrix) {
        float eyeX, eyeY, eyeZ, centerX, centerY, centerZ;
        eyeX = me.getStatus().getPosition().getX();
        eyeY = me.getStatus().getPosition().getY();
        eyeZ = me.getStatus().getPosition().getZ();
        centerX = me.getStatus().getPosition().getX() + me.getStatus().getDirection().getX();
        centerY = me.getStatus().getPosition().getY() + me.getStatus().getDirection().getY();
        centerZ = me.getStatus().getPosition().getZ() + me.getStatus().getDirection().getZ();
        setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
    }

}

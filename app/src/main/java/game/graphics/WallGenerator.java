package game.graphics;

import android.content.Context;
import android.util.Log;
import game.generators.FundamentalGenerator;
import sfogl.integration.ArrayObject;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.ShadingProgram;

/**
 * Classe utility per generare Model per parallelepipedi texturizzati proporzionalmente alle dimensioni.
 * @author Stefano De Pace
 */
public class WallGenerator {

    private float w,h,l;
    private int texture;
    private Context context;
    private ShadingProgram program;

    /**
     * Crea un nuovo  WallGenerator con i parametri per creare un parallelepipedo.
     * @param context Context per il recupero delle risorse.
     * @param program ShadingProgram per ottenere lo shader per la texture.
     * @param texture indice della texture da usare nelle risorse.
     * @param w Dimensione lungo x del parallelepipedo.
     * @param h Dimensione lungo y del parallelepipedo.
     * @param l Dimensione lungo z del parallelepipedo.
     */

    public WallGenerator(Context context, ShadingProgram program, int texture, float w, float h, float l) {
        this.program = program;
        this.context = context;
        this.w=w/2;
        this.h=h/2;
        this.l=l/2;
        this.texture=texture;
    }

    /**
     * Restituisce il Model appositamente dimensionato per un parallelepipedo.
     * @return Model del parallelepipedo, già compreso di Mesh e texture.
     */
    public Model getModel(){
        Mesh meshPos = new Mesh(generatemodel());
        Log.d("New WallModel; dim: ",w+","+h+","+l);
        meshPos.init();
        Model modelPos = new Model();
        modelPos.setRootGeometry(meshPos);
        modelPos.setMaterialComponent(MaterialKeeper.MATERIAL_KEEPER.getMaterial(context, program, texture));
        return modelPos;
    }

    private ArrayObject generatemodel(){
        float hh=2*h;
        return new ArrayObject(
                new float[]{
                        +w, 0, -l,
                        +w, 0, +l,
                        +w, +hh, -l,
                        +w, +hh, +l,

                        +w, +hh, +l,
                        -w, +hh, +l,
                        +w, +hh, -l,
                        -w, +hh, -l,

                        -w, 0, +l,
                        -w, +hh, +l,
                        +w, 0, +l,
                        +w, +hh, +l,

                        -w, +hh, +l,
                        -w, 0, +l,
                        -w, +hh, -l,
                        -w, 0, -l,

                        +w, 0, -l,
                        +w, +hh, -l,
                        -w, 0, -l,
                        -w, +hh, -l
                },
                new float[]{
                        -1, 0, 0
                },
                new float[]{
                        0, 0, 0,
                        l, 0, 0,
                        0, h, 0,
                        l, h, 0,

                        w, l, 0,
                        0, l, 0,
                        w, 0, 0,
                        0, 0, 0,

                        w, 0, 0,
                        w, h, 0,
                        0, 0, 0,
                        0, h, 0,

                        0, h, 0,
                        0, 0, 0,
                        l, h, 0,
                        l, 0, 0,

                        w, 0, 0,
                        w, h, 0,
                        0, 0, 0,
                        0, h, 0
                },
                new short[]{
                        3,0,2,0,3,1,
                        7,4,6,4,7,5,
                        11,8,10,8,11,9,
                        15,12,14,12,15,13,
                        19,16,18,16,19,17
                });
    }

}

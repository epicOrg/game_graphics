package game.graphics;

import android.content.Context;

import java.util.HashMap;

import sfogl.integration.Shaders;
import sfogl.integration.ShadingParameter;
import sfogl.integration.ShadingProgram;


/**
 * Created by Alessandro on 16/03/15.
 */
public class ShadersKeeper {

    public static final String STANDARD_TEXTURE_SHADER = "reflect";

    private static HashMap<String, ShadingProgram> shaders = new HashMap<String, ShadingProgram>();

    private static ShadingParameter pTextureMaterial = new ShadingParameter("textureMaterial", ShadingParameter.ParameterType.GLOBAL_TEXTURE);
    private static ShadingParameter pColor = new ShadingParameter("color", ShadingParameter.ParameterType.GLOBAL_FLOAT4);
    private static ShadingParameter baseColor = new ShadingParameter("baseColor", ShadingParameter.ParameterType.GLOBAL_FLOAT4);
    private static ShadingParameter specColor = new ShadingParameter("specColor", ShadingParameter.ParameterType.GLOBAL_FLOAT4);
    private static ShadingParameter lPos1 = new ShadingParameter("lighPos1", ShadingParameter.ParameterType.GLOBAL_FLOAT3);
    private static ShadingParameter lPos2 = new ShadingParameter("lighPos2", ShadingParameter.ParameterType.GLOBAL_FLOAT3);
    private static ShadingParameter lPos3 = new ShadingParameter("lighPos3", ShadingParameter.ParameterType.GLOBAL_FLOAT3);
    private static ShadingParameter sh = new ShadingParameter("sh", ShadingParameter.ParameterType.GLOBAL_FLOAT);
    private static ShadingParameter reflect = new ShadingParameter("reflect", ShadingParameter.ParameterType.GLOBAL_FLOAT);
    private static ShadingParameter pTextureLow = new ShadingParameter("textureLow", ShadingParameter.ParameterType.GLOBAL_TEXTURE);
    private static ShadingParameter pTextureNormal = new ShadingParameter("textureNormal", ShadingParameter.ParameterType.GLOBAL_TEXTURE);
    private static ShadingParameter pTextureHigh = new ShadingParameter("textureHigh", ShadingParameter.ParameterType.GLOBAL_TEXTURE);

    public static void loadPipelineShaders(Context context) {
        String vertexShader = Shaders.loadText(context, "shaders/stdVertexShader.vsh");
        String fragmentShader = Shaders.loadText(context, "shaders/stdFragmentShader.fsh");

        ShadingProgram program = Shaders.loadShaderModel(vertexShader, fragmentShader, pTextureMaterial);
        program.init();
        shaders.put(STANDARD_TEXTURE_SHADER, program);
    }

    public static ShadingProgram getProgram(String name) {
        return shaders.get(name);
    }

    public static void clear() {
        shaders.clear();
    }

}

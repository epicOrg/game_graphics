package game.miscellaneous;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Class utility for quickly load a float from dimen resources.
 *
 * @author De Pace
 */
public class FloatLoader {

    private Resources resources;

    /**
     * Creates a anew FloatLoader with the given Context.
     *
     * @param context Context from which to retrieve resouces.
     */
    public FloatLoader(Context context) {
        this.resources = context.getResources();
    }

    /**
     * Returns a flot value from resources.
     *
     * @param id id of the float value in resources, from R.dimen.
     * @return the requested float value.
     */
    public float getFloat(int id) {
        TypedValue typedValue = new TypedValue();
        resources.getValue(id, typedValue, true);
        return typedValue.getFloat();
    }
}

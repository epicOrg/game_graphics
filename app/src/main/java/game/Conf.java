package game;

import android.content.Context;
import android.util.TypedValue;

/**
 * Singleton for the operations of configuration.
 *
 * @author De Pace
 */
public enum Conf {
    CONF;

    public static final String errorException = "Must first call the init method with valid parameters!!";
    private Context context;

    /**
     * Initilizes the Context used to acces configuration resources; should be called before trying to retrieve any value.
     *
     * @param context Context used to acces configuration resources.
     */
    public void init(Context context) {
        this.context = context;
    }

    /**
     * Returns a resource int from a given id.
     *
     * @param id id of the float resource parameter: found in R.integer.
     * @return int resource parameter mapped by id.
     */
    public int getInt(int id) {
        checkInitialized();
        return context.getResources().getInteger(id);
    }

    /**
     * Returns a resource int from a given id.
     *
     * @param id id of the float resource parameter: usually found in R.dimen.
     * @return float resource parameter mapped by id.
     */
    public float getFloat(int id) {
        checkInitialized();
        return getValue(id).getFloat();
    }

    private void checkInitialized() {
        if (this.context == null)
            throw new RuntimeException(errorException);
    }

    private TypedValue getValue(int id) {
        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(id, typedValue, true);
        return typedValue;
    }

}

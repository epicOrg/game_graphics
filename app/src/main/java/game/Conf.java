package game;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by depa on 31/05/15.
 */
public enum Conf {
    CONF;

    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public int getInt(int id) {
        return context.getResources().getInteger(id);
    }

    public float getFloat(int id) {
        return getValue(id).getFloat();
    }

    private TypedValue getValue(int id) {
        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(id, typedValue, true);
        return typedValue;
    }

}

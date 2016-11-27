package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;

/**
 * Created and given Context at the start of the app so that Context isn't being dragged around constantly.
 *
 * Purpose of this class:
 * Gson Requires a Context Object to work.
 * If one were to pass a Singleton  (Central Controller) a context object at the start of the
 * code and tell it to hold onto that context for Gson use, Android Studio would complain that
 * this is a memory leak, potentially.
 * Instead, we have a class object that will hold onto our context, but is not a singleton itself.
 *
 * Inspired by Professor Hindle making mention of a "Context Factory" (Pattern) in the lectures,
 * though we're not sure if this necessarily qualifies as an implementation of said pattern.
 * @author kgmills
 */
public class ContextFactory {

    /**
     * Typically passed at the beginning of the app.
     */
    private Context gsonContext;

    public ContextFactory(Context context) {
        this.setGsonContext(context);
    }

    /**
     * Explanatory getter
     * @return Context object
     */
    public Context getGsonContext() {
        return gsonContext;
    }

    /**
     * Explanatory setter
     * @param gsonContext Context to be held.
     */
    private void setGsonContext(Context gsonContext) {
        this.gsonContext = gsonContext;
    }
}

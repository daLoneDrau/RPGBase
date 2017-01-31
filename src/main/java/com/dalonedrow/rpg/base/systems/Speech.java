package com.dalonedrow.rpg.base.systems;

import com.dalonedrow.rpg.base.flyweights.BaseInteractiveObject;

public abstract class Speech<IO extends BaseInteractiveObject> {
    /** the one and only instance of the <tt>Speech</tt> class. */
    private static Speech instance;
    /**
     * Gives access to the singleton instance of {@link Speech}.
     * @return {@link Script}
     */
    public static Speech getInstance() {
        return Speech.instance;
    }
    /**
     * Sets the singleton instance.
     * @param i the instance to set
     */
    protected static void setInstance(final Speech i) {
        Speech.instance = i;
    }
    public abstract int ARX_SPEECH_AddSpeech(final IO io, final int mood,
            final String speech, long voixoff);
}

package hr.fer.oprpp1.hw08.jnotepad.local;

import java.util.ArrayList;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
    private ArrayList<ILocalizationListener> listeners = new ArrayList<>();

    @Override
    public void addLocalizationListener(ILocalizationListener l) {
        listeners.add(l);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener l) {
        listeners.remove(l);
    }

    public void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }
}

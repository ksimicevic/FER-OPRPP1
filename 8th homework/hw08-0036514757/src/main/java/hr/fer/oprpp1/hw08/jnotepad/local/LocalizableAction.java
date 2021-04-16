package hr.fer.oprpp1.hw08.jnotepad.local;

import javax.swing.*;

public abstract class LocalizableAction extends AbstractAction {
    private String key;
    private ILocalizationListener listener;
    private ILocalizationProvider provider;

    public LocalizableAction(String key, ILocalizationProvider provider) {
        this.key = key;
        this.provider = provider;
        this.listener = () -> {
            putValue(NAME, provider.getString(key));
            putValue(Action.SHORT_DESCRIPTION, provider.getString(key + "_description"));

        };
        provider.addLocalizationListener(listener);
        listener.localizationChanged();
    }
}

package hr.fer.oprpp1.hw08.jnotepad.local;

import javax.swing.*;

public class LJMenu extends JMenu {
    private String key;
    private ILocalizationProvider provider;
    private ILocalizationListener listener;

    public LJMenu(String key, ILocalizationProvider provider) {
        this.key = key;
        this.provider = provider;
        this.listener = () -> setText(provider.getString(key));
        provider.addLocalizationListener(listener);
        listener.localizationChanged();
    }
}

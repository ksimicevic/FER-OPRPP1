package hr.fer.oprpp1.hw08.jnotepad.local;

import javax.swing.*;

public class LJMenuItem extends JMenuItem {
    private String key;
    private ILocalizationProvider provider;
    private ILocalizationListener listener;

    public LJMenuItem(String key, ILocalizationProvider provider, LocalizableAction action)  {
        this.key = key;
        this.provider = provider;
        addActionListener(action);
        this.listener = () -> setText(provider.getString(key));
        provider.addLocalizationListener(listener);
        listener.localizationChanged();
    }
}

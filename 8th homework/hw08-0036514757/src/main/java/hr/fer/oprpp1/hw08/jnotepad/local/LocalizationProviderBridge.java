package hr.fer.oprpp1.hw08.jnotepad.local;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {
    private boolean connected;
    private ILocalizationProvider parent;
    private ILocalizationListener listener;

    public LocalizationProviderBridge(ILocalizationProvider parent) {
        this.parent = parent;
        this.listener = this::fire;
    }

    public void connect() {
        if (!connected) {
            connected = true;
            parent.addLocalizationListener(listener);
        }
    }

    public void disconnect() {
        if (connected) {
            connected = false;
            parent.removeLocalizationListener(listener);
        }
    }

    public String getString(String key) {
        return parent.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return parent.getCurrentLanguage();
    }
}

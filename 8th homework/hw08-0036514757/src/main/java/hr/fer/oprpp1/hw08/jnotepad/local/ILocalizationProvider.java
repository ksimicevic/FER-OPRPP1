package hr.fer.oprpp1.hw08.jnotepad.local;

public interface ILocalizationProvider {
    void addLocalizationListener(ILocalizationListener l);

    void removeLocalizationListener(ILocalizationListener l);

    String getString(String key);

    String getCurrentLanguage();
}

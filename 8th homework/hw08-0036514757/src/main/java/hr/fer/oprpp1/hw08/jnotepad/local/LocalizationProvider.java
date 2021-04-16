package hr.fer.oprpp1.hw08.jnotepad.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {
    private static LocalizationProvider provider = new LocalizationProvider();

    private String language;
    private ResourceBundle bundle;

    private LocalizationProvider() {
        language = "en";
        getResourceBundle();
    }

    public static LocalizationProvider getInstance() {
        return provider;
    }

    public void setLanguage(String language) {
        this.language = language;
        getResourceBundle();
        fire();
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return language;
    }

    private void getResourceBundle() {
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepad.local.prijevodi", locale);
    }
}

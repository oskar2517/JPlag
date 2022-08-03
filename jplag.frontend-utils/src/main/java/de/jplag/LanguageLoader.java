package de.jplag;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains methods to load {@link Language Languages}.
 * @author Dominik Fuchss
 */
public final class LanguageLoader {
    private static final Logger logger = LoggerFactory.getLogger(LanguageLoader.class);

    private static Map<String, Language> loaded = null;

    private LanguageLoader() {
        throw new IllegalAccessError();
    }

    /**
     * Load all languages that are currently in the classpath.
     * @return the languages
     */
    public static synchronized List<Language> getAllAvailableLanguages() {
        if (loaded != null)
            return loaded.values().stream().toList();

        Map<String, Language> languages = new TreeMap<>();

        for (Language language : ServiceLoader.load(Language.class)) {
            String shortName = language.getShortName();
            if (languages.containsKey(shortName)) {
                logger.error("Multiple implementations for a language '{}' are present in the classpath! Skipping ..", shortName);
                languages.remove(shortName);
                continue;
            }
            logger.info("Loading Language Frontend '{}'", language.getName());
            languages.put(shortName, language);
        }

        loaded = Collections.unmodifiableMap(languages);
        return loaded.values().stream().toList();
    }

    /**
     * Load a language that is currently in the classpath by its short name.
     * @param shortName the short name of the language
     * @return the language or an empty optional if no language has been found.
     */
    public static Optional<Language> getLanguage(String shortName) {
        var result = getAllAvailableLanguages().stream().filter(it -> Objects.equals(it.getShortName(), shortName)).findFirst();
        if (result.isEmpty())
            logger.warn("Attempt to load Language {} was not successful", shortName);
        return result;
    }

    /**
     * Get a list of all available languages with their short name.
     * @return the list of all languages
     */
    public static List<String> getAllAvailableLanguageShortNames() {
        return getAllAvailableLanguages().stream().map(Language::getShortName).toList();
    }

    /**
     * Resets the internal cache of all loaded languages
     */
    public static synchronized void clearCache() {
        loaded = null;
    }
}

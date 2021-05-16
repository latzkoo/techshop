package hu.db.techshop.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

public class Slug {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String from(String input) {
        String noWhiteSpace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace, Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH);
    }

}
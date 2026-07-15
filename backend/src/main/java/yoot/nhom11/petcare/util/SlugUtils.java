package yoot.nhom11.petcare.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtils {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
    private static final Pattern MULTIDASH = Pattern.compile("-+");

    public static String slugify(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // Convert to lower case
        String temp = input.trim().toLowerCase(Locale.ENGLISH);

        // Replace special Vietnamese character đ/Đ
        temp = temp.replace('đ', 'd');

        // Normalize
        temp = Normalizer.normalize(temp, Normalizer.Form.NFD);
        
        // Remove diacritics
        temp = temp.replaceAll("\\p{M}", "");

        // Replace whitespaces with dash
        temp = WHITESPACE.matcher(temp).replaceAll("-");

        // Remove non-latin, non-numeric characters except dash
        temp = NONLATIN.matcher(temp).replaceAll("");

        // Replace multiple consecutive dashes with a single dash
        temp = MULTIDASH.matcher(temp).replaceAll("-");

        // Remove leading and trailing dashes
        if (temp.startsWith("-")) {
            temp = temp.substring(1);
        }
        if (temp.endsWith("-")) {
            temp = temp.substring(0, temp.length() - 1);
        }

        return temp;
    }
}

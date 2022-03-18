package serverbot.util;

public class QuizActions {
    public static String simplifyString(String string) {
        return string
                .toLowerCase()
                .replace("á", "a")
                .replace("à", "a")
                .replace("â", "a")
                .replace("é", "e")
                .replace("è", "e")
                .replace("ê", "e")
                .replace("ë", "e")
                .replace("í", "i")
                .replace("ì", "i")
                .replace("î", "i")
                .replace("ï", "i")
                .replace("ó", "o")
                .replace("ò", "o")
                .replace("ô", "o")
                .replace("ú", "u")
                .replace("ù", "u")
                .replace("û", "u");
    }
}

package entities.minifilemanager;

import java.io.File;

public final class MiniFileManagerUtils {

    private MiniFileManagerUtils() {
    }

    static String[] debugInput(String input) {
        return input.split(" ", 2);
    }

    static String debugText(String text) {
        return text.substring(1, text.length() - 1);
    }

    static void showPath(File path) {
        System.out.print(path.getAbsolutePath() + " > ");
    }
}

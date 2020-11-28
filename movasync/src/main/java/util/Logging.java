package util;

import java.text.MessageFormat;

public final class Logging {
    private Logging() {
    }

    public static void log(String str, Object... args) {
        System.out.print(MessageFormat.format("[{0}] -> ", Thread.currentThread().getId()));
        System.out.println(MessageFormat.format(str, args));
    }
}


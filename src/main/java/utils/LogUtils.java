package utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class LogUtils {
    private static final List<String> ls = new ArrayList<>();
    public static void add(String s) { ls.add(s); }
    // public static List<String> get() { return ls; }
    public static void clr() { ls.clear(); }
    // The function here is designed to write error messages into log.txt
    public static void w(String p) {
        try {
            Files.write(Paths.get(p), ls);
        } catch (IOException e) {
            throw new RuntimeException("Log fail: " + p, e);
        }
    }
}
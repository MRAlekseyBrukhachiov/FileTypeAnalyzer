package analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processor implements Runnable {

    private File file;
    private List<FilePattern> patterns;

    public Processor(File file, List<FilePattern> patterns) {
        this.file = file;
        this.patterns = patterns;
    }

    public static int[] prefixFunction(String s) {
        int n = s.length();
        int[] pi = new int[n];

        for (int i = 1; i < pi.length; i++) {
            int j = pi[i - 1];

            while (j > 0 && s.charAt(j) != s.charAt(i)) {
                j = pi[j - 1];
            }

            if (s.charAt(i) == s.charAt(j)) {
                j += 1;
            }

            pi[i] = j;
        }
        return pi;
    }

    //Algorithms
    public boolean kmp(String s, String pattern, String type) {
        boolean find = false;
        int[] pi = prefixFunction(pattern);
        int j = 0;

        for (int i = 0; i < s.length(); i++) {

            while (j > 0 && s.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }

            if (s.charAt(i) == pattern.charAt(j)) {
                j += 1;
            }

            if (j == pattern.length()) {
                find = true;
                break;
            }
        }
        System.out.printf("%s: %s\n", file.getName(), find ? type : "Unknown file type");
        return find;
    }

    public static void naive(String text, String pattern, String type) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        System.out.println(matcher.find() ? type : "Unknown file type");
    }

    public boolean rabinKarp(String s, String pattern) {
        int a = 117;
        long m = 173_961_102_589_771L;

        if (pattern.length() > s.length()) {
            return false;
        }

        long patternHash = 0;
        long currSubstrHash = 0;
        long pow = 1;

        for (int i = 0; i < pattern.length(); i++) {
            patternHash += (long) pattern.charAt(i) * pow;
            patternHash %= m;

            currSubstrHash += (long) s.charAt(s.length() - pattern.length() + i) * pow;
            currSubstrHash %= m;

            if (i != pattern.length() - 1) {
                pow = pow * a % m;
            }
        }

        for (int i = s.length(); i >= pattern.length(); i--) {
            if (patternHash == currSubstrHash) {
                for (int j = 0; j < pattern.length(); j++) {
                    if (s.charAt(i - pattern.length() + j) != pattern.charAt(j)) {
                        break;
                    }
                }

                return true;
            }

            if (i > pattern.length()) {
                currSubstrHash = (currSubstrHash - s.charAt(i - 1) * pow % m + m) * a % m;
                currSubstrHash = (currSubstrHash + s.charAt(i - pattern.length() - 1)) % m;
            }
        }

        return false;
    }

    @Override
    public void run() {
        try {
            byte[] data = Files.readAllBytes(file.toPath());
            String text = new String(data, StandardCharsets.UTF_8);
            for (FilePattern pt: patterns) {
                boolean find = rabinKarp(text, pt.getPattern());
                System.out.printf("%s: %s\n", file.getName(), find ? pt.getType() : "Unknown file type");
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }
}

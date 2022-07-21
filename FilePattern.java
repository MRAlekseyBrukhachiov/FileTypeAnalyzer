package analyzer;

public class FilePattern {

    private final int priority;
    private final String pattern;
    private final String type;

    public FilePattern(int priority, String pattern, String type) {
        this.priority = priority;
        this.pattern = pattern;
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getType() {
        return type;
    }
}

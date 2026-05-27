package use.tool.dev.analyser;

public class LineMatch {
    private final int lineNumber;
    private final String lineContent;

    public LineMatch(int lineNumber, String lineContent) {
        this.lineNumber = lineNumber;
        this.lineContent = lineContent;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLineContent() {
        return lineContent;
    }

    @Override
    public String toString() {
        return "Zeile " + lineNumber + ": " + lineContent;
    }
}

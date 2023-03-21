package io.jenkins.plugins.VirtualPipeline;

//possibly make advanced class from this
public class VirtualPipelineLineOutput {
    private String regex;

    private String line;

    private int index;

    private Boolean deleteMark;

    //private OFFSET TODO

    public VirtualPipelineLineOutput(String regex, String line, int index, Boolean deleteMark) {
        this.regex = regex;
        this.line = line;
        this.index = index;
        this.deleteMark = deleteMark;
    }

    // just for the purpose of good deserialization
    public VirtualPipelineLineOutput() {
        super();
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Boolean getDeleteMark() {
        return deleteMark;
    }

    public void setDeleteMark(Boolean deleteMark) {
        this.deleteMark = deleteMark;
    }

}

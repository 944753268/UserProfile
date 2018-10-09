package com.canbot.userprofile.model;

public class Voice {

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String time;

    private String content;

    @Override
    public String toString() {
        return "Voice{" +
                "time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

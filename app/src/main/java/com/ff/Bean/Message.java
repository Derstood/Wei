package com.ff.Bean;

/**
 * Created by Kr on 2018/4/30.
 */

public class Message {
    private int type; //1表示群聊消息，2表示私聊消息
    private String name;
    private String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

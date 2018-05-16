package com.ff.Bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Kr on 2018/4/30.
 */

public class Message implements Serializable{

    private Timestamp time;
    private String group;      //私聊消息的group为空，群聊消息的to为空
    private String from;        //group,from,to都是ID
    private String to;
    private String content;
    private String avatarURL;



    public Message() {
    }


    public Message(Timestamp time, String group , String from, String to, String content,String avatarURL) {
        this.time = time;
        this.group = group;
        this.from = from;
        this.to = to;
        this.content = content;
        this.avatarURL = avatarURL;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatarURL() { return avatarURL; }

    public void setAvatarURL(String avatarURL) { this.avatarURL = avatarURL; }

    @Override
    public String toString() {
        return "Message{" +
                "time=" + time +
                ", group='" + group + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                '}';
    }
}

package com.example.myapp;

import java.io.Serializable;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

/**
 * Class that represents all kind of messages
 *  in networking
 */
public class Message implements Serializable {
    public static final int NEW_BROKER = 0;
    public static final int UPDATE_CHANNEL = 1;
    public static final int LIST_BROKERS = 2;
    public static final int LIST_CHANNELS = 3;
    public static final int CHANNEL_HISTORY = 4;
    public static final int PULL_MEDIA = 5;
    public static final int TEXT = 6;
    public static final int IMAGE = 7;
    public static final int VIDEO = 8;
    public static final int FINISH = 9;

    //Class serialization ID
    private static final long serialVersionUID = -2723363051271966964L;
    //Fields
    private String channel = null;//Topic's identifier
    private String username = null;//Sender's identifier
    private int type;
    private Object content;//Actual data

    // Constructor(s)
    Message(JSONObject obj) { load(obj); }
    Message(String user, int type) {
        username = user;
        this.type = type;
    }

    Message(int type, Object data) {
        this.type = type;
        content = data;
    }

    Message(String user, String channel, int type, Object data) {
        username = user;
        this.channel = channel;
        this.type = type;
        content = data;
    }


    // Getters
    public String getChannel() { return channel; }
    public Object getContent() { return content; }
    public int getType() { return type; }
    public String getUsername() { return username; }

    // Setters
    public void setChannel(String channel) { this.channel = channel; }
    public void setContent(Object content) { this.content = content; }
    public void setType(int type) { this.type = type; }
    public void setUsername(String username) { this.username = username; }

    // IO
    public Map<String, Object> export() {
        Map<String, Object> obj = new LinkedHashMap<>();
        obj.put("type", type);
        obj.put("user", username);
        switch(type) {
            case Message.TEXT:
                obj.put("content", (String)content);
                break;
            case Message.IMAGE:
            case Message.VIDEO:
                obj.put("content", ((MediaFile)content).export());
                break;
            default://Maybe through exception instead
                return null;
        }
        return obj;
    }

    public void load(JSONObject obj) {
        username = (String)obj.get("user");
        type = ((Long)obj.get("type")).intValue();
        switch(type) {
            case Message.TEXT:
                content = (String)obj.get("content");
                break;
            case Message.IMAGE:
            case Message.VIDEO:
                content = new MediaFile((JSONObject)obj.get("content"));
                break;
            default:
                return;
        }
    }

}
package com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean;

public class MessageEvent {
    private String id;
    private String name;
    private String create_time;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }
    private int position;
    public MessageEvent(String id, String name, String create_time, String update_time, String type, String sequence, String record_state,int position) {
        this.id = id;
        this.name = name;
        this.create_time = create_time;
        this.update_time = update_time;
        this.type = type;
        this.sequence = sequence;
        this.record_state = record_state;
        this.position=position;
    }

    public void setId(String id) {
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getRecord_state() {
        return record_state;
    }

    public void setRecord_state(String record_state) {
        this.record_state = record_state;
    }
    private String update_time;
    private String type;
    private String sequence;
    private String record_state;
}

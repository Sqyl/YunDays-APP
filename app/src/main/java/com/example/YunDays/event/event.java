package com.example.YunDays.event;

public abstract class event implements eventGet{

    protected String name;
    protected int belong_userID;
    protected int _id;

    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public void setBelong_userID(int belong_userID) {
        this.belong_userID = belong_userID;
    }

    @Override
    public void set_id(int _id) { this._id = _id; }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getBelong_userID() { return belong_userID; }

    @Override
    public int get_id() { return _id; }

    @Override
    public String toString() {
        return name;
    }
}

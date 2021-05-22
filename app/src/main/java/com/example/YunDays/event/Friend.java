package com.example.YunDays.event;

public class Friend {
    private int id;
    private int belong_UserId;
    private int friendId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBelong_UserId() {
        return belong_UserId;
    }

    public void setBelong_UserId(int belong_UserId) {
        this.belong_UserId = belong_UserId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
}

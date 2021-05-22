package com.example.YunDays.userclass;

/**
 * Designed by Sqyl NZ171王畅
 * 毕设作品，2021年7月1日前仅供个人使用
 */

/**
 * UserClass表
 * _id           integer  primary key    not null
 * userAccount   text       not null
 * userPassword  text       not null
 * userName      text       not null
 *
 */

public class UserClass {

    private int userID;
    private String userAccount;
    private String userPassword;
    private String userName;

    public UserClass(int userID, String userAccount, String userPassword,String userName) {
        this.setUserAccount(userAccount);
        this.setUserPassword(userPassword);
        this.setUserName(userName);
        this.setUserID(userID);

    }

    public void setUserID(int userID) { this.userID = userID; }
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserID() { return userID;}
    public String getUserAccount() { return userAccount; }
    public String getUserName() { return userName; }
    public String getUserPassword() { return userPassword; }
}

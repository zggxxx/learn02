package com.learn.vo;

public class UserVO {
    private String userName;
    private Long userId;
    private String nickname;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public UserVO(String userName, Long userId,String nickname) {
        this.userName = userName;
        this.userId = userId;
        this.nickname = nickname;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getNickname(){
        return nickname;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
}

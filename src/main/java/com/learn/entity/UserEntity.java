package com.learn.entity;

public class UserEntity {
    private String userName;
<<<<<<< HEAD
    private String password;
=======
>>>>>>> origin/main

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

<<<<<<< HEAD
    public UserEntity(String userName, String password, Long userId) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
    }

=======
>>>>>>> origin/main
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

<<<<<<< HEAD
=======
    private String password;
>>>>>>> origin/main
    private Long userId;
}

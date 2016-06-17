package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.util.Date;

public class UserAccountData {
    private String username;
    private String email;
    private String password;
    private Date createTime;
    private int authorityId;

    public UserAccountData() {
    }

    public UserAccountData(String username, String email, String password, Date createTime,
            int authorityId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createTime = createTime;
        this.authorityId = authorityId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(int authorityId) {
        this.authorityId = authorityId;
    }

    @Override
    public String toString() {
        return "UserAccountData [username=" + username + ", email=" + email + ", password=" + password
                + ", createTime=" + createTime + ", authorityId=" + authorityId + "]";
    }

}

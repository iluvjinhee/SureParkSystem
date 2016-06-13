package com.lge.sureparksystem.parkserver.databasemanager;

public class Account {
    private String login_id;
    private String login_pw;
    private String name;
    private String authority;
    private String credit_number;
    private String credit_name;
    private String credit_expired;

    public Account() {
    }

    public Account(String id, String pw, String name, String authority) {
        this.login_id = id;
        this.login_pw = pw;
        this.name = name;
        this.authority = authority;
        this.credit_number = null;
        this.credit_name = null;
        this.credit_expired = null;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getLogin_pw() {
        return login_pw;
    }

    public void setLogin_pw(String login_pw) {
        this.login_pw = login_pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getCredit_number() {
        return credit_number;
    }

    public void setCredit_number(String credit_number) {
        this.credit_number = credit_number;
    }

    public String getCredit_name() {
        return credit_name;
    }

    public void setCredit_name(String credit_name) {
        this.credit_name = credit_name;
    }

    public String getCredit_expired() {
        return credit_expired;
    }

    public void setCredit_expired(String credit_expired) {
        this.credit_expired = credit_expired;
    }

    @Override
    public String toString() {
        return "Account [login_id=" + login_id + ", login_pw=" + login_pw + ", name=" + name
                + ", authority=" + authority + ", credit_number=" + credit_number + ", credit_name="
                + credit_name + ", credit_expired=" + credit_expired + "]";
    }

}

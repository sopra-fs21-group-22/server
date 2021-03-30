package ch.uzh.ifi.hase.soprafs21.rest.dto.users;

import java.sql.Date;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

public class UserPostDTO {

    private String password;

    private String username;

    private Date birthday;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}

package ch.uzh.ifi.hase.soprafs21.rest.dto.users;

import java.sql.Date;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

public class UserPutDTO {
    private String password;

    private String username;

    private Date birthday;

    private UserStatus status;

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
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}

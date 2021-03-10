package ch.uzh.ifi.hase.soprafs21.entity;

import java.sql.Date;

import org.springframework.security.core.userdetails.UserDetails;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

public class AuthenticationResponse {

    private final String jwt;
    private String username;
    private Long id;
    private UserStatus status;
    private Date creationDate;
    private Date birthday;

    public AuthenticationResponse(String jwt, User user) {
        this.jwt = jwt;
        this.username = user.getUsername();
        this.id = user.getId();
        this.status = user.getStatus();
        this.creationDate = user.getCreationDate();
        this.birthday = user.getBirthday();

    }

    public String getJwt() {
        return jwt;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}

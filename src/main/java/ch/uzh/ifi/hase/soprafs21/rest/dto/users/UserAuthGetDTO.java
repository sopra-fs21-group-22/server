package ch.uzh.ifi.hase.soprafs21.rest.dto.users;

import java.sql.Date;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

public class UserAuthGetDTO {
    private String jwt;
    private String username;
    private Long id;
    private UserStatus status;
    private Date creationDate;
    private Date birthday;
    private Player player;

    public Long getPlayer() {
        if (player == null) {
            return null;
        }
        return player.getId();
    }

    public Long getTableId() {
        if (player == null) {
            return null;
        }
        return player.getTable().getId();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}

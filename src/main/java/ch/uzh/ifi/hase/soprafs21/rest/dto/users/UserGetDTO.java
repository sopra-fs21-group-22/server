package ch.uzh.ifi.hase.soprafs21.rest.dto.users;

import java.sql.Date;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class UserGetDTO {

    private Long id;
    private String username;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}

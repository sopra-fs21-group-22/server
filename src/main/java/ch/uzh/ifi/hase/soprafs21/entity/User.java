package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

/**
 * Internal User Representation This class composes the internal representation
 * of the user and defines how the user is stored in the database. Every
 * variable will be mapped into a database field with the @Column annotation -
 * nullable = false -> this cannot be left empty - unique = true -> this value
 * must be unqiue across the database -> composes the primary key
 */
@Entity
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "user")
    private Player player;

    @Column(nullable = false)
    private Date creationDate;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private UserStatus status;

    @Column()
    private Date birthday;

    private String jwt;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    // TODO remove?
    public void setId(Long id) {
        this.id = id;
    }

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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
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

    public String getJwt() {
        return this.jwt;
    }

    public void setJwt(String token) {
        this.jwt = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

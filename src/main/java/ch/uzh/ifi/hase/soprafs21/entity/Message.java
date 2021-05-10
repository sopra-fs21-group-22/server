package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.*;

import javax.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    private String content;
    
    private String name;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
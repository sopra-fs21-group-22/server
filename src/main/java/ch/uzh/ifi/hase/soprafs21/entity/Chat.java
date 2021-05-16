package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.*;

import ch.uzh.ifi.hase.soprafs21.entity.Message;

import javax.persistence.*;

@Entity
public class Chat {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id")
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
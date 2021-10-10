package com.javaspringpractice.step01.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javaspringpractice.step01.user.User;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.Size;

@ApiModel(description = "All details about post controller.")
@Entity
public class Post {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Size(min = 2, message = "name should be more than 2 characters")
    private String name;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

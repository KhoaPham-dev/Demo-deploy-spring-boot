package com.javaspringpractice.step01.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJPAResourse {

    @Autowired
    private UserDaoService service;

    @Autowired
    private UserRepository repository;

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return repository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = repository.findById(id);
        if(!user.isPresent()) {
            throw new UserNotFoundException("id" + id);
        }

        EntityModel<User> entityModel = EntityModel.of(user.get());

        Link link = WebMvcLinkBuilder.linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("user-list");

        return entityModel.add(link);
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<Object> addUser(@Valid @RequestBody User newUser) {
        User addedUser = repository.save(newUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(addedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        repository.deleteById(id);
    }
}

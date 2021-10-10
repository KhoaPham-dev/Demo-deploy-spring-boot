package com.javaspringpractice.step01.post;

import com.javaspringpractice.step01.user.User;
import com.javaspringpractice.step01.user.UserNotFoundException;
import com.javaspringpractice.step01.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PostJPAResource {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("jpa/users/{userId}/posts")
    public List<Post> retrieveAllPosts(@PathVariable int userId) {
        Optional<User> userOptions = userRepository.findById(userId);
        if(!userOptions.isPresent()) {
            throw new UserNotFoundException("id-" + userId);
        }
        return userOptions.get().getPosts();
    }

    @GetMapping("jpa/users/{userId}/posts/{postId}")
    public EntityModel<Post> retrievePost(@PathVariable int userId, @PathVariable int postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(!optionalPost.isPresent()) {
            throw new PostNotFoundException("id" + postId);
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()) {
            throw new UserNotFoundException("user-id-" + userId);
        }
        if(optionalPost.get().getUser().getId() != optionalUser.get().getId()) {
            throw new PostNotFoundException("id" + postId);
        }

        EntityModel<Post> entityModel = EntityModel.of(optionalPost.get());
        Link link = WebMvcLinkBuilder.linkTo(methodOn(this.getClass()).retrieveAllPosts(optionalUser.get().getId())).withRel("get post");
        return entityModel.add(link);
    }

    @PostMapping("jpa/users/{userId}/posts")
    public ResponseEntity<Object> createPost(@PathVariable int userId, @Valid @RequestBody Post post) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()) {
            throw new UserNotFoundException("userId-" + userId);
        }
        post.setUser(optionalUser.get());
        postRepository.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}

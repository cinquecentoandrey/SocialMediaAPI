package com.cinquecento.smapi.rest;

import com.cinquecento.smapi.dto.PostDTO;
import com.cinquecento.smapi.model.Post;
import com.cinquecento.smapi.model.User;
import com.cinquecento.smapi.service.PostService;
import com.cinquecento.smapi.util.CurrentUserInfo;
import com.cinquecento.smapi.util.ErrorMessageBuilder;
import com.cinquecento.smapi.util.PostConverter;
import com.cinquecento.smapi.util.exception.PostNotCreatedException;
import com.cinquecento.smapi.util.exception.PostNotUpdatedException;
import com.cinquecento.smapi.util.response.PostErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostConverter postConverter;
    private final ErrorMessageBuilder errorMessageBuilder;
    private final CurrentUserInfo currentUserInfo;


    @Autowired
    public PostController(PostService postService, PostConverter postConverter, ErrorMessageBuilder errorMessageBuilder, CurrentUserInfo currentUserInfo) {
        this.postService = postService;
        this.postConverter = postConverter;
        this.errorMessageBuilder = errorMessageBuilder;
        this.currentUserInfo = currentUserInfo;
    }

    @GetMapping("/{id}")
    public PostDTO get(@PathVariable(name = "id") Long id) {
        return postConverter.convertToPostDTO(postService.findById(id));
    }

    @PostMapping
    public PostDTO create(@RequestBody @Valid PostDTO postDTO,
                          BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new PostNotCreatedException(errorMessageBuilder.message(bindingResult));
        }

        Post post = postConverter.convertToPost(postDTO);
        post.setCreator(currentUserInfo.getCurrentUser());

        postService.save(post);

        return postDTO;
    }

    @PatchMapping("/{id}")
    public PostDTO update(@PathVariable(name = "id") Long id,
                          @RequestBody @Valid PostDTO postDTO,
                          BindingResult bindingResult) {

        User user = currentUserInfo.getCurrentUser();

        if (postService.findByUser(user)) {
            if (bindingResult.hasErrors()) {
                throw new PostNotUpdatedException(errorMessageBuilder.message(bindingResult));
            }
            postService.update(id, postConverter.convertToPost(postDTO));

            return postDTO;
        } else {
            throw new PostNotUpdatedException("You can't update this post, cuz it isn't your post!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/friends")
    public List<PostDTO> friendsPosts() {
        return postService.findAllFriendsPosts(currentUserInfo.getCurrentUser().getId())
                .stream()
                .map(postConverter::convertToPostDTO)
                .toList();
    }

    @ExceptionHandler
    public ResponseEntity<PostErrorResponse> handledException(PostNotUpdatedException exception) {
        PostErrorResponse response = new PostErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler
    public ResponseEntity<PostErrorResponse> handledException(PostNotCreatedException exception) {
        PostErrorResponse response = new PostErrorResponse(
                exception.getMessage(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_MODIFIED);
    }

}

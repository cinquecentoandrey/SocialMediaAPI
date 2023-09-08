package com.cinquecento.smapi.service;

import com.cinquecento.smapi.model.Post;
import com.cinquecento.smapi.model.User;

import java.util.List;

public interface PostService {

    void save(Post post);

    List<Post> findAll();

    List<Post> findAllByUserId(Long id);

    Post findById(Long id);

    boolean findByUserAndId(User user, Long id);

    List<Post> findAllFriendsPosts(Long userId);

    void update(Long id, Post updatedPost);

    void delete(Long id);
}

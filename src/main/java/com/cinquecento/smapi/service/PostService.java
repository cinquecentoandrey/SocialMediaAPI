package com.cinquecento.smapi.service;

import com.cinquecento.smapi.model.Post;

import java.util.List;

public interface PostService {

    void save(Post post);

    List<Post> findAll();

    List<Post> findAllByUserId(Long id);

    Post findById(Long id);

    void update(Long id, Post updatedPost);

    void delete(Long id);
}

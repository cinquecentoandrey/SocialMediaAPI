package com.cinquecento.smapi.service.impl;

import com.cinquecento.smapi.model.Post;
import com.cinquecento.smapi.model.User;
import com.cinquecento.smapi.repository.PostRepository;
import com.cinquecento.smapi.service.PostService;
import com.cinquecento.smapi.util.exception.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public void save(Post post) {
        post.setCreatedAt(new Date());
        post.setLastUpdate(new Date());
        postRepository.save(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAllByUserId(Long id) {
        return postRepository.findAllByCreator_Id(id);
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id = " + id + " not found"));
    }

    @Override
    public boolean findByUserAndId(User user, Long postId) {
        return postRepository.findByCreatorAndId(user, postId).isPresent();
    }

    @Override
    public List<Post> findAllFriendsPosts(Long userId) {
        return postRepository.findAllFriendsPosts(userId)
                .stream()
                .sorted(Comparator.comparing(Post::getLastUpdate).reversed())
                .toList();
    }

    @Override
    @Transactional
    public void update(Long id, Post postToUpdate) {
        postToUpdate.setLastUpdate(new Date());
        postRepository.partUpdate(id,
                postToUpdate.getArticle(),
                postToUpdate.getContent(),
                postToUpdate.getLastUpdate());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}

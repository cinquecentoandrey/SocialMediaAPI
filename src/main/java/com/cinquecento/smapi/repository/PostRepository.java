package com.cinquecento.smapi.repository;

import com.cinquecento.smapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

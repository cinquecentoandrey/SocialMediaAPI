package com.cinquecento.smapi.repository;

import com.cinquecento.smapi.model.Comment;
import com.cinquecento.smapi.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost_Id(Long id, Sort sort);

}

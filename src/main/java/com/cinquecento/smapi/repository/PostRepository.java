package com.cinquecento.smapi.repository;

import com.cinquecento.smapi.model.Post;
import com.cinquecento.smapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByCreator_Id(Long id);

    List<Post> findAllByCreator_IdOrderByLastUpdateDesc(Long id);

    List<Post> findAllByCreator_IdOrderByLastUpdateAsc(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.article=:article, p.content=:content, p.lastUpdate=:lastUpdate WHERE p.id=:id")
    void partUpdate(@Param("id") Long id,
                    @Param("article") String article,
                    @Param("content") String content,
                    @Param("lastUpdate") Date lastUpdate);

    Optional<Post> findByCreatorAndId(User creator, Long postId);

    @Query(value = "SELECT DISTINCT P.*\n" +
            "FROM Friends AS F\n" +
            "JOIN Users AS U ON (F.first_user_id = U.id OR F.second_user_id = U.id) and (U.id <> :userId) \n" +
            "Join Posts AS P ON p.user_id = u.id\n" +
            "WHERE F.first_user_id=:userId OR F.second_user_id=:userId", nativeQuery = true)
    List<Post> findAllFriendsPosts(@Param("userId") Long userId);

}

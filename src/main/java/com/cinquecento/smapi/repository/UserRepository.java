package com.cinquecento.smapi.repository;

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
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE User u SET " +
            "u.username=:username, u.firstName=:first_name, u.lastName=:last_name, " +
            "u.age=:age, u.telephone=:telephone, u.email=:email, u.lastUpdate=:last_update WHERE u.id=:id")
    void partUpdate(@Param(value = "id") Long id,
                    @Param(value = "username") String username,
                    @Param(value = "first_name") String firstName,
                    @Param(value = "last_name") String lastName,
                    @Param(value = "age") Integer age,
                    @Param(value = "telephone") String telephone,
                    @Param(value = "email") String email,
                    @Param(value = "last_update") Date last_update);

    @Query(value = "SELECT DISTINCT " +
            "U.*\n" +
            "FROM Friends AS F\n" +
            "JOIN Users AS U ON (F.fist_user_id = U.id OR F.second_user_id = U.id) and (U.id <> :id)\n" +
            "WHERE F.fist_user_id= :id OR F.second_user_id= :id", nativeQuery = true)
    List<User> findFriendsByUserId(@Param("id") Long id);

    @Query(value = "SELECT DISTINCT U.*\n" +
            "FROM Followers AS F\n" +
            "JOIN Users AS U ON (F.user_id = U.id OR F.follower_id = U.id) and (U.id <> :id) \n" +
            "WHERE F.user_id= :id OR F.follower_id= :id", nativeQuery = true)
    List<User> findFollowersByUserId(@Param("id") Long id);

    @Modifying
    @Query(value = "INSERT INTO Followers values(:userId, :subId)", nativeQuery = true)
    void subscribe(@Param("userId") Long userId, @Param("subId") Long subId);

    @Modifying
    @Query(value = "DELETE FROM Followers WHERE user_id=:userId AND follower_id=:unsubId", nativeQuery = true)
    void unsubscribe(@Param("userId") Long userId, @Param("unsubId") Long unsubId);

    @Modifying
    @Query(value = "INSERT INTO Friends values(:userId, :subId)", nativeQuery = true)
    void addFriend(@Param("userId") Long id, @Param("subId") Long friendId);

    @Modifying
    @Query(value = "DELETE FROM Friends WHERE first_user_id=:firstId AND second_user_id=:secondId", nativeQuery = true)
    void removeFriend(@Param("firstId") Long firstId, @Param("secondId") Long secondId);
}

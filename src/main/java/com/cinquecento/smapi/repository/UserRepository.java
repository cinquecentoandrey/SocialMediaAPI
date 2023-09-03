package com.cinquecento.smapi.repository;

import com.cinquecento.smapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

}

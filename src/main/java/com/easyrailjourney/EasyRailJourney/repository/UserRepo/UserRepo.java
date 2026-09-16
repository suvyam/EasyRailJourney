package com.easyrailjourney.EasyRailJourney.repository.UserRepo;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.users.Users;

public interface UserRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByEmailAndIsDeleted(
            String email,
            boolean isDeleted
    );

    Optional<Users> findByEmail(String email);

    Optional<Users> findByProfileNameAndIsDeleted(String profile,boolean isDeleted);

    Optional<Users> findByEmailAndIsDeletedFalse(String email);

    @Query("""
        SELECT u FROM users u
        WHERE
            (
                (:id IS NOT NULL AND u.id = :id)
                OR (:phoneNumber IS NOT NULL AND u.phoneNumber = :phoneNumber)
                OR (:fullName IS NOT NULL AND u.fullName = :fullName)
                OR (:email IS NOT NULL AND u.email = :email)
            )
            AND u.isDeleted = :isDeleted
    """)
    List<Users> searchUser(
            @Param("id") Long id,
            @Param("phoneNumber") String phoneNumber,
            @Param("fullName") String fullName,
            @Param("email") String email,
            @Param("isDeleted") boolean isDeleted
    );
}
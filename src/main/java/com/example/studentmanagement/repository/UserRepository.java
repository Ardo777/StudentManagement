package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findUserByUserType(UserType userType);

    List<User> findByUserType(UserType userType);

    List<User> findAllByUserTypeAndLesson(UserType userType, Lesson lesson);

    List<User> findByLesson(Lesson lesson);
}

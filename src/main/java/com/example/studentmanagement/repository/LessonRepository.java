package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {
}

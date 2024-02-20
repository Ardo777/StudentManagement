package com.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "lesson")
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @DateTimeFormat(pattern = "HH:mm")
    private Date duration;

    private Double price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private User user;
}
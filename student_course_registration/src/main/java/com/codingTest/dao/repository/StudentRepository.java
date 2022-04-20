package com.codingTest.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingTest.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	public Optional<Student> findStudentByStudentName(String studentName);
}

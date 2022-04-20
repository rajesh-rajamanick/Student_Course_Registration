package com.codingTest.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codingTest.domain.Student;
import com.codingTest.service.StudentService;

@RestController
public class StudentController {
	private final static Logger LOG = LoggerFactory.getLogger(StudentController.class);
	
	private StudentService studentService;

	@Autowired
	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@PostMapping("/student")
	public String addStudent(@RequestBody Student student) {
		LOG.info("Student :: Student Name {}", student.getStudentName());
		studentService.addStudent(student);
		return "Student with Name:" + student.getStudentName() + " has been Added.";
	}

	@PutMapping("/student")
	public String updateStudent(@RequestBody Student student) {
		LOG.info("Student :: Student Name {}", student.getStudentName());
		studentService.updateStudent(student);
		return "Student with Name:" + student.getStudentName() + " has been Added.";
	}

	
	
	@DeleteMapping("/student/{studentId}")
	public String removeStudent(Long studentId) {
		studentService.removeStudent(studentId);
		return "Student with Id:" + studentId + " has been removed.";
	}
	
	@GetMapping("/studentsByCourseName/{courseName}")
	public Set<Student> getStudentsByCourseName(@PathVariable String courseName) {
		return studentService.getStudentsByCourseName(courseName);
	}

	@GetMapping("/allStudents")
	public Set<Student> getAllStudents() {
		return studentService.getAllStudents();
	}

}

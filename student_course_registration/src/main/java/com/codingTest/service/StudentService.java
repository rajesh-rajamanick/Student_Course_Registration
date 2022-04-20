package com.codingTest.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingTest.dao.repository.CourseRepository;
import com.codingTest.dao.repository.StudentRepository;
import com.codingTest.domain.Course;
import com.codingTest.domain.Student;
import com.codingTest.exception.StudentCourseIllegalStateException;

@Service
public class StudentService {
	private final static Logger LOG = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
		this.studentRepository = studentRepository;
	}

	public Long addStudent(Student student) {
		student = studentRepository.save(student);
		LOG.info("Student {} Successfully added", student.getStudentID());
		return student.getStudentID();
	}

	public Long updateStudent(Student student) {
		Optional<Student> studentOptional = studentRepository.findById(student.getStudentID());
		if (!studentOptional.isPresent()) {
			throw new StudentCourseIllegalStateException(
					"Failed to update Student. Invalid StudentId :: " + student.getStudentID());
		}
		student = studentRepository.save(student);
		LOG.info("Student {} Successfully updated", student.getStudentID());
		return student.getStudentID();
	}

	public void removeStudent(Long studentId) {
		Optional<Student> student = studentRepository.findById(studentId);
		if (!student.isPresent()) {
			throw new StudentCourseIllegalStateException("Failed to remove Student. Invalid StudentId :: " + studentId);
		}
		studentRepository.delete(student.get());
	}

	public void registerCourse(Long studentId, Set<Course> courses) {
		Optional<Student> studentOptional = studentRepository.findById(studentId);
		if (!studentOptional.isPresent()) {
			throw new StudentCourseIllegalStateException("Failed to register course. Invalid CourseId :: " + studentId);
		}
		Student student = studentOptional.get();
		courses.addAll(student.getCourses());
		student.setCourses(courses);
		studentRepository.save(student);
	}

	public Set<Student> getStudentsByCourseName(String courseName) {
		Optional<Course> course = courseRepository.findCourseByCourseName(courseName);
		if (!course.isPresent()) {
			throw new StudentCourseIllegalStateException("Failed to get Students. Invalid courseName :: " + courseName);
		}
		Comparator<Student> studentByName = (Student student1, Student student2) -> student1.getStudentName()
				.compareTo(student2.getStudentName());
		TreeSet<Student> sortedStudents = new TreeSet<>(studentByName);

		Set<Student> students = course.get().getStudents();
		students.forEach(student -> student.setCourses(null));
		sortedStudents.addAll(students);
		LOG.debug("Actual Students :: {} and Sorted Students by Name:: {}", students, sortedStudents);
		return sortedStudents;
	}

	public Set<Student> getAllStudents() {
		Comparator<Student> studentByName = (Student student1, Student student2) -> student1.getStudentName()
				.compareTo(student2.getStudentName());
		TreeSet<Student> sortedStudents = new TreeSet<>(studentByName);
		List<Student> students = studentRepository.findAll();
		sortedStudents.addAll(students);
		LOG.debug("Actual Students :: {} and Sorted Students by Name:: {}", students, sortedStudents);
		return sortedStudents;
	}

	public Student getStudentByName(String studentName) {
		Optional<Student> student = studentRepository.findStudentByStudentName(studentName);
		if (!student.isPresent()) {
			throw new StudentCourseIllegalStateException(
					"Failed to get Student. Invalid StudentName :: " + studentName);
		}
		return student.get();
	}

}

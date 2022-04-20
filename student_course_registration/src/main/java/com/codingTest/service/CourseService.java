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
public class CourseService {
	private final static Logger LOG = LoggerFactory.getLogger(CourseService.class);
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
		this.courseRepository = courseRepository;
		this.studentRepository = studentRepository;
	}

	public Long addCourse(Course course) {
		course = courseRepository.save(course);
		LOG.info("Course: {} has been successfully added. ", course.getCourseID());
		return course.getCourseID();
	}
	
	public Long updateCourse(Course course) {
		Optional<Course> courseOptional = courseRepository.findById(course.getCourseID());
		if (!courseOptional.isPresent()) {
			throw new StudentCourseIllegalStateException("Failed to update Course. Invalid CourseId :: " + course.getCourseID());
		}
		course = courseRepository.save(course);
		LOG.info("Course: {} has been successfully added. ", course.getCourseID());
		return course.getCourseID();
	}

	public void removeCourse(Long courseId) {
		Optional<Course> course = courseRepository.findById(courseId);
		if (!course.isPresent()) {
			throw new StudentCourseIllegalStateException("Failed to remove Course. Invalid CourseId :: " + courseId);
		}
		courseRepository.delete(course.get());
	}

	public void registerStudentToCourse(Long courseId, Set<Student> students) {
		LOG.info("CourseId :: {} , Student :: {}", courseId, students);
		Optional<Course> courseOptional = courseRepository.findById(courseId);
		if (!courseOptional.isPresent()) {
			throw new StudentCourseIllegalStateException("Failed to register Student. Invalid CourseId :: " + courseId);
		}
		Course course = courseOptional.get();
		students.addAll(course.getStudents());
		course.setStudents(students);
		courseRepository.save(course);
	}

	public Optional<Course> getCourseByCourseName(String courseName) {
		return courseRepository.findCourseByCourseName(courseName);
	}

	public Set<Course> getCoursesByStudentName(String studentName) {
		Optional<Student> studentOptional = studentRepository.findStudentByStudentName(studentName);
		if (!studentOptional.isPresent()) {
			throw new StudentCourseIllegalStateException(
					"Failed to retrieve Course. Invalid StudentName :: " + studentName);
		}
		Student student = studentOptional.get();
		Comparator<Course> courseByName = (Course course1, Course course2) -> course1.getCourseName()
				.compareTo(course2.getCourseName());
		TreeSet<Course> sortedCourses = new TreeSet<>(courseByName);

		Set<Course> courses = student.getCourses();
		sortedCourses.addAll(courses);
		LOG.debug("Actual Courses :: {} and Sorted Courses by Name:: {}", courses, sortedCourses);
		return sortedCourses;
	}

	public Set<Course> getAllCourses() {
		List<Course> courses = courseRepository.findAll();
		Comparator<Course> courseByName = (Course course1, Course course2) -> course1.getCourseName()
				.compareTo(course2.getCourseName());
		TreeSet<Course> sortedCourses = new TreeSet<>(courseByName);
		sortedCourses.addAll(courses);
		LOG.debug("Actual Courses :: {} and Sorted Courses by Name:: {}", courses, sortedCourses);
		return sortedCourses;
	}

}

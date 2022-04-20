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

import com.codingTest.domain.Course;
import com.codingTest.domain.Student;
import com.codingTest.service.CourseService;

@RestController
public class CourseController {
	private final static Logger LOG = LoggerFactory.getLogger(StudentController.class);

	private CourseService courseService;

	@Autowired
	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@PostMapping("/course")
	public String addCourse(@RequestBody Course course) {
		LOG.info("Course  ::Course Name {}", course.getCourseName());
		courseService.addCourse(course);
		return "Course with Name:" + course.getCourseName() + " has been Added.";
	}

	@DeleteMapping("/course/{courseId}")
	public String removeCourse(Long courseId) {
		courseService.removeCourse(courseId);
		return "Course with Id:" + courseId + " has been removed.";
	}

	@PutMapping("/course")
	public String removeCourse(@RequestBody Course course) {
		Long courseId = courseService.updateCourse(course);
		return "Course with Id:" + courseId + " has been updated.";
	}

	@PutMapping("/registerStudentsToCourse/{courseId}")
	public String enrollStudentToCourse(@PathVariable Long courseId, @RequestBody Set<Student> students) {
		courseService.registerStudentToCourse(courseId, students);
		return "Students has been successfully Enrolled to Course :: " + courseId;
	}

	@GetMapping("/coursesByStudentName/{studentName}")
	public Set<Course> getCoursesByStudentName(@PathVariable String studentName) {
		return courseService.getCoursesByStudentName(studentName);
	}

	@GetMapping("/allCourses")
	public Set<Course> getAllCourses() {
		return courseService.getAllCourses();
	}

}

package com.openshift.coursecatalogue.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openshift.coursecatalogue.model.Courses;
import com.openshift.coursecatalogue.model.Enrollment;
import com.openshift.coursecatalogue.model.Filter;

import com.openshift.coursecatalogue.model.Users;

import com.openshift.coursecatalogue.model.UserwithCourse;

import com.openshift.coursecatalogue.service.CourseService;

import com.openshift.coursecatalogue.service.UserService;


/**
 * 
 * @author kaleembasha.akbar
 *
 * 
 * 
 *         Controller class for Course related stuffs.
 * 
 */

@RestController

public class CourseController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired

	private CourseService courseService;

	@Autowired

	private UserService userService;

	@GetMapping(path = "/get")

	public List<Courses> getCourseLlist() {

		return courseService.findAll();

	}

	@GetMapping(value = "course/{courseId}")

	public UserwithCourse getUser(@PathVariable String courseId) {

		Courses course = courseService.findOne(courseId);

		Users user = userService.findOne(course.getOwner());

		return new UserwithCourse(course.getId(), user.getName(), course.getDescription(), course.getName(),
				course.getFilters());

	}

	@GetMapping(path = "/getAllCourses")

	public List<UserwithCourse> getCourses() {

		List<UserwithCourse> usercourse = new ArrayList<>();

		List<Courses> course = courseService.findAll();

		course.stream().forEach(action -> {

			String id = action.getId();

			String owner = action.getOwner();

			String name = action.getName();

			String desc = action.getDescription();

			List<Filter> filt = action.getFilters();

			Users users = userService.findOne(owner);

			LOG.info("Getting users of value " + users.getName());

			UserwithCourse user = new UserwithCourse(id, users.getName(), desc, name, filt);

			usercourse.add(user);

		});

		return usercourse;

	}

	@RequestMapping(value = "getCourseStatus/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	// @RequestMapping(value = "getCourseStatus", method = RequestMethod.POST,
	// produces=MediaType.APPLICATION_JSON_VALUE)
	public String getCourseStatus(@PathVariable String userId) throws JsonProcessingException {
		List<String> usercourse = new ArrayList<>();
		Map map1=new HashMap();  
		String s = "111111111";

		LOG.info("Getting users of valu of id" + new ObjectId(userId));
		Enrollment enrollCourse = courseService.findEnrollCourse(new ObjectId(userId));
		Courses course = courseService.findOne(enrollCourse.getCourseId());
		// usercourse.add(enrollCourse.getId());
		// usercourse.add(enrollCourse.getCourseId().getName());
		map1.put("id",enrollCourse.getCourseId());
		map1.put("courseName",course.getName());
		map1.put("startDate",enrollCourse.getStartDate());
		map1.put("endDate",enrollCourse.getEndDate());
		map1.put("state",enrollCourse.getState());
		
		ObjectMapper objectMapper = new ObjectMapper();
	//	usercourse.add(enrollCourse.getCourseId());
	/*	usercourse.add(course.getName());
		usercourse.add(enrollCourse.getStartDate());
		usercourse.add(enrollCourse.getEndDate());
		usercourse.add(enrollCourse.getState());
	*/	
	//	Map<String, String> result = books.stream() .collect(Collectors.toMap(book -> book.getISBN, book -> book));

		
		// enrollCourse.getCourse_id()
		// usercourse.add(e)
		/*
		 * Courses course=courseService.findOne(c); Users users=
		 * userService.findOne(course.getOwner().toString());
		 * LOG.info("Getting users of valu" +users.getEmail()+users.getName());
		 * usercourse.add(course.getName()); usercourse.add(users.getName());
		 * usercourse.add(users.getEmail()); usercourse.add(course.getDescription());
		 */
	//	return usercourse;
		 String json = objectMapper.writeValueAsString(map1);
		return json;
	}
}

package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private AuthUserClient authUserClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseUserService courseUserService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDto>> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable(value = "courseId") UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid SubscriptionDto subscriptionDto) {
        ResponseEntity<UserDto> responseUser;
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        if (courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDto.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        try {
            responseUser = authUserClient.getOneUserById(subscriptionDto.getUserId());
            if (responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found.");
            }
        }

        CourseUserModel courseUserModel = courseUserService.saveAndSendSubscriptionUserInCourse(courseModelOptional.get().convertToCourseUserModel(subscriptionDto.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);

    }

    @DeleteMapping("/courses/users/{userId}")
    public ResponseEntity<Object> deleteCourseUserByUser(@PathVariable(value = "userId") UUID userId) {
        if (!courseUserService.existsByUserId(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseUser not found.");
        }
        courseUserService.deleteCourseUserByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("CourseUser deleted successfully");
    }
}
package com.ead.course.services.Impl;

import com.ead.course.repositories.CourseUserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourUserServiceImpl {

    @Autowired
    private CourseUserRespository courseUserRespository;

}

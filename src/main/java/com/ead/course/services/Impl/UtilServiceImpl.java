package com.ead.course.services.Impl;

import com.ead.course.services.UtilService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilServiceImpl implements UtilService {

    @Value("${ead.api.url.authuser}")
    private String REQUEST_URL_AUTHUSER;

    public String createUrlGetAllUsersByCourse(UUID courseId, Pageable pageable) {
        return REQUEST_URL_AUTHUSER + "/users?courseId=" + courseId
                + "&page=" + pageable.getPageNumber()
                + "&size=" + pageable.getPageSize()
                + "&sort=" + pageable.getSort().toString().replace(": ", ",");
    }

}

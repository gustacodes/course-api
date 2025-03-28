package com.ead.course.services;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {
    void delete(ModuleModel moduleModel);
    Object save(ModuleModel moduleModel);
    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);
    List<ModuleModel> findAllByCourse(UUID courseId);
    Optional<ModuleModel> findById(UUID moduleId);
    Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable);
}

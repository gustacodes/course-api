package com.ead.course.services;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {
    Object save(LessonModel lessonModel);
    Optional<LessonModel> findLessonIntoModule(UUID lessonId, UUID moduleId);
    void delete(LessonModel lessonModel);
    List<LessonModel> findAllByModule(UUID moduleId);
    Page<LessonModel> findAllByModule(Specification<LessonModel> spec, Pageable pageable);
}

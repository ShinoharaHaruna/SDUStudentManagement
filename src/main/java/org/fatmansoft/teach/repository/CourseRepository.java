package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {
    // Update @ 2022/4/29 17:26
    // 课程中心
    @Query(value = "select max(id) from Course  ")
    Integer getMaxId();

    @Query(value = "from Course where ?1='' or courseNum like %?1% or courseName like %?1% ")
    List<Course> findCourseListByNumName(String numName);

    @Query(value = "select * from course  where ?1='' or courseNum like %?1% or courseName like %?1% ", nativeQuery = true)
    List<Course> findCourseListByNumNameNative(String numName);
}

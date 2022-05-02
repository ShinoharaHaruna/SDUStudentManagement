package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Innovation;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface InnovationRepository extends JpaRepository<Innovation,Integer> {
    // 创新中心
    @Query(value = "select max(id) from Innovation  ")
    Integer getMaxId();

    @Query(value = "from Innovation where ?1='' or studentNum like %?1%")
    List<Innovation> findInnovationListByStudentNum(String studentNum);
}

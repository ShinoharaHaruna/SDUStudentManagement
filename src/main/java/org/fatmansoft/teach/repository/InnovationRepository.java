package org.fatmansoft.teach.repository;
import org.fatmansoft.teach.models.Achievement;
import org.fatmansoft.teach.models.Innovation;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InnovationRepository extends JpaRepository<Innovation, Integer>{    //2022/4/30 18:56 by qyf

    @Query(value = "select max(id) from Innovation  ")
    Integer getMaxId();

    @Query(value = "from Innovation where ?1='' or innoName like %?1% or innoType like %?1% ")
    List<Innovation> findInnovationListByNumName(String numName);

//    @Query(value = "select * from student  where ?1='' or studentNum like %?1% or studentName like %?1% ", nativeQuery = true)
//    List<Student> findStudentListByNumNameNative(String numName);

}

package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Innovation;
import org.fatmansoft.teach.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository extends JpaRepository<Log,Integer> {
    // 日志中心
    @Query(value = "select max(id) from Log  ")
    Integer getMaxId();

    @Query(value = "from Log where ?1='' or studentNum like %?1%")
    List<Log> findLogListByStudentNum(String studentNum);
}
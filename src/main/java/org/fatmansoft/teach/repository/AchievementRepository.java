package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
    // Update @ 2022/3/8 13:50
    // 设计一些 achievement 数据库的JpaRepository

    @Query(value = "select max(id) from Achievement  ")
    Integer getMaxId();

    @Query(value = "from Achievement where ?1=''")
    List<Achievement> findTitlesByStudentNum(String StudentNum);
}

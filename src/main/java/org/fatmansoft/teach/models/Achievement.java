package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

// Update @ 2022/3/8 13:52
// 考虑重新设计 achievement 数据表
// 设想中，应该包含字段：id（主键，不变），title（字符串，表示荣誉，新增），student_num（改个名以统一形式）
// 删去字段：score（因为成就或者说荣誉很难用分数 score 记录），course_id（并不是所有荣誉都能和校内的课程绑定）


@Entity
@Table(	name = "achievement",
        uniqueConstraints = {
        })
public class Achievement {
    @Id
    private Integer id;

//    @ManyToOne
//    @JoinColumn(name="student_num")
//    private Student student;
    @NotBlank
    private String studentNum;

    // 如第 7 行所声明
    @NotBlank
    private String title;

//    @ManyToOne
//    @JoinColumn(name="courseId")
//    private Student course;

//    private Double score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudentNum() {
        return studentNum;
//        return student.getStudentNum();
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
//        this.student.setStudentNum(studentNum);
    }

    //    public Student getCourse() {
//        return course;
//    }

//    public void setCourse(Student course) {
//        this.course = course;
//    }

//    public Double getScore() {
//        return score;
//    }

//    public void setScore(Double score) {
//        this.score = score;
//    }
}

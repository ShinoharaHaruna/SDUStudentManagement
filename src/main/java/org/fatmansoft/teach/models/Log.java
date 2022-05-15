package org.fatmansoft.teach.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "log",
        uniqueConstraints = {
        })
public class Log {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name="student_num")
    private Student student;
//    @NotBlank
//    @Size(max = 20)
//    private String studentNum;

    @Range(min = 1, max = 4)
    private Integer logType;
    @NotBlank
    @Size(max = 256)
    private String logDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentNum() {
//        return studentNum;
        return student.getStudentNum();
    }

    public void setStudentNum(String studentNum) {
//        this.studentNum = studentNum;
        this.student.setStudentNum(studentNum);
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }
}

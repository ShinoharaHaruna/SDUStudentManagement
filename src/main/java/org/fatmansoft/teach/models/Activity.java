package org.fatmansoft.teach.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(	name = "activity",
        uniqueConstraints = {
        })
public class Activity {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name="student_num")
    private Student student;
//    @NotBlank
//    @Size(max = 20)
//    private String studentNum;

    @Range(min = 1, max = 4)
    private Integer acType;
    private Date acDate;

    @NotBlank
    private String acName;

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

    public Integer getAcType() {
        return acType;
    }

    public void setAcType(Integer acType) {
        this.acType = acType;
    }

    public Date getAcDate() {
        return acDate;
    }

    public void setAcDate(Date acDate) {
        this.acDate = acDate;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }
}

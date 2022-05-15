package org.fatmansoft.teach.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(	name = "innovation",
        uniqueConstraints = {
        })
public class Innovation {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name="student_num")
    private Student student;
//    @NotBlank
//    @Size(max = 20)
//    private String studentNum;

    @Range(min= 1, max = 6)
    private Integer innoType;
    private Date innoDate;

    @NotBlank
    @Size(max = 256)
    private String innoName;

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
        student.setStudentNum(studentNum);
    }

    public Integer getInnoType() {
        return innoType;
    }

    public void setInnoType(Integer innoType) {
        this.innoType = innoType;
    }

    public Date getInnoDate() {
        return innoDate;
    }

    public void setInnoDate(Date innoDate) {
        this.innoDate = innoDate;
    }

    public String getInnoName() {
        return innoName;
    }

    public void setInnoName(String innoName) {
        this.innoName = innoName;
    }
}

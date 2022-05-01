package org.fatmansoft.teach.models;


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
    @JoinColumn(name="studentId")
    private Student student;
    private String studentNum ;
    private String studentName ;

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @NotBlank
    @Size(max = 20)
    private String innoType ;
    private String innoName ;

    @Size(max = 2)
    private Date innoDate ;

    public Integer getId(){ return id; }

    public void setId(Integer id){ this.id = id; }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getInnoType() {
        return innoType;
    }

    public void setInnoType(String innoType) {
        this.innoType = innoType;
    }

    public String getInnoName() {
        return innoName;
    }

    public void setInnoName(String innoName) {
        this.innoName = innoName;
    }

    public Date getInnoDate() {
        return innoDate;
    }

    public void setInnoDate(Date innoDate) {
        this.innoDate = innoDate;
    }
}

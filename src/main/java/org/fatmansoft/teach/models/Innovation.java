package org.fatmansoft.teach.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @NotBlank
    @Size(max = 20)
    private String studentNum;

    private Integer innoType;
    private Date innoDate;

    private String innoName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
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

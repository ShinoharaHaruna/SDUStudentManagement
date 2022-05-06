package org.fatmansoft.teach.models;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "course",
        uniqueConstraints = {
        })
public class Course {
    @Id
    private Integer id;
    @NotBlank
    @Size(max = 20)
    private String courseNum;

    @NotBlank
    @Size(max = 50)
    private String courseName;
    @Min(0)
    private Integer courseCapacity;
    @Min(0)
    private Integer courseReged;

    // 显然，下列数据都可留空
    private String courseIntro;
    private String courseBook;
    @URL
    private String courseware;
    private String courseRef;
    private String courseHomework;

    public Integer getCourseCapacity() {
        return courseCapacity;
    }

    public void setCourseCapacity(Integer courseCapacity) {
        this.courseCapacity = courseCapacity;
    }

    public Integer getCourseReged() {
        return courseReged;
    }

    public void setCourseReged(Integer courseReged) {
        this.courseReged = courseReged;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseIntro() {
        return courseIntro;
    }

    public void setCourseIntro(String courseIntro) {
        this.courseIntro = courseIntro;
    }

    public String getCourseBook() {
        return courseBook;
    }

    public void setCourseBook(String courseBook) {
        this.courseBook = courseBook;
    }

    public String getCourseware() {
        return courseware;
    }

    public void setCourseware(String courseware) {
        this.courseware = courseware;
    }

    public String getCourseRef() {
        return courseRef;
    }

    public void setCourseRef(String courseRef) {
        this.courseRef = courseRef;
    }

    public String getCourseHomework() {
        return courseHomework;
    }

    public void setCourseHomework(String courseHomework) {
        this.courseHomework = courseHomework;
    }
}

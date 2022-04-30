package org.fatmansoft.teach.models;

// Grade 类可以用 json 格式化得到每门课程的：
// 1. 课程名
// 2. 学分
// 3. 绩点
// 4. 缺勤数

public class Grade {
    public String courseName;
    public Double credit;
    private Double grade;
    private Integer absence;

    public Grade(String courseName, Double credit, Double grade, Integer absence) {
        this.courseName = courseName;
        this.credit = credit;
        this.grade = grade;
        this.absence = absence;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getAbsence() {
        return absence;
    }

    public void setAbsence(Integer absence) {
        this.absence = absence;
    }

    @Override
    public String toString() {
        return "{\"courseName\":\"" + courseName + "\"" +
                ",\"credit\":" + credit +
                ",\"grade\":" + grade +
                ",\"absence\":" + absence +
                '}';
    }
}

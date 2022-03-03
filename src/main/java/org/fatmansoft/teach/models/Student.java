package org.fatmansoft.teach.models;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(	name = "student",
        uniqueConstraints = {
        })
public class Student {
    @Id
    private Integer id;

    @NotBlank
    @Size(max = 20)
    private String studentNum;

    @Size(max = 50)
    private String studentName;
    @Size(max = 2)
    private String sex;
    private Integer age;
    private Date birthday;

    private String phone;

    // json 字符串，存的是该学生的成绩信息，见 org.fatmansoft.teach.models.GradeList
    private String grade;

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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGradeByCourse(String targetCourse) {
        // 初始化json
        String str = grade;
        JSONObject jsonObject = JSONObject.parseObject(str);
        // 没有成绩视为 0 分
        if(jsonObject == null){
            return "0.00";
        }
        JSONArray jsonArray = jsonObject.getJSONArray("courses");

        // targetCourse 是目标课程名字
        for(int i = 0; i < jsonArray.size(); ++i){
            // 拿出每门课的成绩
            JSONObject j = JSONObject.parseObject(jsonArray.get(i).toString());
            if(j.get("courseName").equals(targetCourse)){
                // 找到就跳出
                return j.get("grade").toString();
            }
        }
        // 返回 null, 说明没有找到这门课的成绩
        return null;
    }

    public Double getGPA(){
        // 初始化 json
        String str = grade;
        JSONObject jsonObject = JSONObject.parseObject(str);
        if (jsonObject == null){
            // 没有成绩视为 0 分
            return new Double("0.00");
        }
        JSONArray jsonArray = jsonObject.getJSONArray("courses");

        Double a = new Double(0.0), b = new Double(0.0);
        for(int i = 0; i < jsonArray.size(); ++i){
            // 拿出每门课的成绩
            JSONObject j = JSONObject.parseObject(jsonArray.get(i).toString());
            a += Double.parseDouble(j.get("grade").toString()) * Double.parseDouble(j.get("credit").toString());
            b += Double.parseDouble(j.get("credit").toString());
        }
        // 这是没选课的情况，GPA 为0
        if(b.equals(new Double(0.0)))return new Double("0.00");
        // 否则就正常返回 GPA
        return a / b;
    }
}

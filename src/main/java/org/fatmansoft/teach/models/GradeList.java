package org.fatmansoft.teach.models;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;


// GradeList 相当于某同学的成绩单，格式化为 json 后存储至数据表 student 中
public class GradeList {
    private ArrayList<Grade> courses;

    public GradeList() {this.courses = new ArrayList<Grade>();}

    public GradeList(Grade g){
        this.courses = new ArrayList<Grade>();
        this.courses.add(g);
    }
    public GradeList(String str) {
        courses = new ArrayList<Grade>();
        JSONObject jsonObject = JSONObject.parseObject(str);
        if (jsonObject == null)this.courses = null;
        else{
            JSONArray jsonArray = jsonObject.getJSONArray("courses");
            for(int i = 0; i < jsonArray.size(); ++i){
                // 拿出每门课的成绩
                JSONObject j = JSONObject.parseObject(jsonArray.get(i).toString());
                this.courses.add(new Grade(j.get("courseName").toString(), Double.parseDouble(j.get("credit").toString()), Double.parseDouble(j.get("grade").toString()), Integer.parseInt(j.get("absence").toString())));
            }
        }
    }
    public void add(Grade e){
        if(courses == null){
            courses = new ArrayList<Grade>();
            courses.add(e);
        }else{
            courses.add(e);
        }
    }
    public int size(){
        if (this.courses == null)return 0;
        return courses.size();
    }
    public Grade get(int index){
        return courses.get(index);
    }
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        if(courses == null)return "";
        for(int i = 0; i < courses.size() - 1; ++i){
            str.append(courses.get(i)).append(",");
        }
        return "{\"courses\":[" + str + courses.get(courses.size() - 1) + "]}";
    }
}

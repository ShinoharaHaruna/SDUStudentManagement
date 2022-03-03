package org.fatmansoft.teach.models;

import java.util.ArrayList;


// GradeList 相当于某同学的成绩单，格式化为 json 后存储至数据表 student 中
public class GradeList {
    private ArrayList<Grade> courses = new ArrayList<>();

    public void add(Grade e){
        courses.add(e);
    }
    public int size(){
        return courses.size();
    }

    public Grade get(int index){
        return courses.get(index);
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < courses.size() - 1; ++i){
            str.append(courses.get(i)).append(",");
        }
        return "{\"courses\":[" + str + courses.get(courses.size() - 1) + "]}";
    }
}

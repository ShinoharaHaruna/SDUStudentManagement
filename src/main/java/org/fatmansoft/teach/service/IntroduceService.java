package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IntroduceService {
    @Autowired
    private StudentRepository studentRepository;
    //个人简历信息数据准备方法  请同学修改这个方法，请根据自己的数据的希望展示的内容拼接成字符串，放在Map对象里， attachList 可以方多段内容，具体内容有个人决定
    public Map getIntroduceDataMap(){
        Map data = new HashMap();
        data.put("myName", "张三");   // 学生信息
        data.put("overview","本人.");  //学生基本信息综述
        List attachList = new ArrayList();
        Map m;
        m = new HashMap();
        m.put("title","学习成绩");   //
        m.put("content","成绩...");  // 学生成绩综述
        attachList.add(m);
        m = new HashMap();
        m.put("title","社会实践");
        m.put("content","社会实践...");  // 社会实践综述
        attachList.add(m);
        data.put("attachList",attachList);
        return data;
    }

    public Map getIntroduceDataMap(String studentNum){
        if(studentNum == null){
            Map data = new HashMap();
            List attachList = new ArrayList();
            Map m;
            m = new HashMap();
//            data.put("myName", "❤");   // 学生信息
//            data.put("overview","本人.");  //学生基本信息综述
            m.put("title", "简介");
            m.put("content", "这里是个人简介喵~");
            attachList.add(m);
            data.put("attachList",attachList);
            return data;
        }
        List<Student> sList = studentRepository.findStudentListByNumName(studentNum);
        Map data = new HashMap();
        if(sList == null)return data;
        Student s = sList.get(0);
        data.put("myName", s.getStudentName());   // 学生信息
        data.put("overview","本人.");  //学生基本信息综述
        List attachList = new ArrayList();
        Map m;
        m = new HashMap();
        m.put("title","学习成绩");   //
        m.put("content",s.getGPA());  // 学生成绩综述
        attachList.add(m);
        m = new HashMap();
        m.put("title","社会实践");
        m.put("content","社会实践...");  // 社会实践综述
        attachList.add(m);
        m = new HashMap();
        m.put("title","社会实践");
        m.put("content","社会实践???????????...");  // 社会实践综述
        attachList.add(m);
        data.put("attachList",attachList);
        return data;
    }
}

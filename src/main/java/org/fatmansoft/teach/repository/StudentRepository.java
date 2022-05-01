package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

public interface StudentRepository extends JpaRepository<Student,Integer> {
    Optional<Student> findByStudentNum(String studentNum);
    List<Student> findByStudentName(String studentName);

    @Query(value = "select max(id) from Student  ")
    Integer getMaxId();

    @Query(value = "from Student where ?1='' or studentNum like %?1% or studentName like %?1% ")
    List<Student> findStudentListByNumName(String numName);

    @Query(value = "select * from student  where ?1='' or studentNum like %?1% or sudentName like %?1% ", nativeQuery = true)
    List<Student> findStudentListByNumNameNative(String numName);

}
//    public List getStudentMapList(String numName) {
//        List dataList = new ArrayList();
//        List<Student> sList = studentRepository.findStudentListByNumName(numName);  //数据库查询操作
//        if(sList == null || sList.size() == 0)
//            return dataList;
//        Student s;
//        Map m;
//        for(int i = 0; i < sList.size();i++) {
//            s = sList.get(i);
//            m = new HashMap();
//            m.put("id", s.getId());
//            m.put("studentNum",s.getStudentNum());
//            m.put("studentName",s.getStudentName());
//            if("1".equals(s.getSex())) {    //数据库存的是编码，显示是名称
//                m.put("sex","男");
//            }else {
//                m.put("sex","女");
//            }
//            m.put("age",s.getAge());
//            m.put("birthday", DateTimeTool.parseDateTime(s.getBirthday(),"yyyy-MM-dd"));  //时间格式转换字符串
//            dataList.add(m);
//        }
//        return dataList;
//    }
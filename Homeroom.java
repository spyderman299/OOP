import java.util.*;

public class Homeroom {
    public static final int MAX_STUDENTS = 50;
    private final String classId, subjectId, teacherId, startDate, endDate;
    private final Set<String> students = new TreeSet<>();

    public Homeroom(String subjectId, String teacherId, String start, String end, int seq) {
        this.subjectId = subjectId.toUpperCase();
        this.teacherId = teacherId.toUpperCase();
        this.startDate = start; this.endDate = end;
        this.classId = this.subjectId + "-" + seq;
    }

    public String getClassId(){ return classId; }
    public boolean addStudent(String studentId){
        return students.size() < MAX_STUDENTS && students.add(studentId.toUpperCase());
    }
    public int size(){ return students.size(); }

    public String save(){
        return String.join(",", classId, subjectId, teacherId, startDate, endDate, String.valueOf(size()));
    }
}

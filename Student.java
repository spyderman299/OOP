import java.util.*;

public class Student extends Person {
    private static int cnt = 0;
    private final Set<String> attendedClasses = new TreeSet<>(); 

    public Student(String name, String dob, String phone) {
        super("SV" + String.format("%03d", ++cnt), name, dob, phone);
    }
    public void addClass(String classId) {
        if (classId != null && !classId.isEmpty())
            attendedClasses.add(classId.toUpperCase());
    }
    public Set<String> getAttendedClasses() { return Collections.unmodifiableSet(attendedClasses); }

    @Override
    public String save() { 
        return String.join(",", getId(), getName(), getDob(), getPhoneNum());
    }
}

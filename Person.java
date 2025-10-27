import java.text.SimpleDateFormat;
import java.text.ParseException;

public abstract class Person {
    private String id, name, dob, phoneNum;

    public Person() {}
    public Person(String id, String name, String dob, String phone) {
        setId(id); setName(name); setDob(dob); setPhoneNum(phone);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDob() { return dob; }
    public String getPhoneNum() { return phoneNum; }

    public void setId(String id) { this.id = (id==null?"":id.trim().toUpperCase()); }
    public void setName(String name) {
        if (name==null) { this.name=""; return; }
        name = name.trim().replaceAll("\\s+"," ");
        String[] w = name.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s : w) if (!s.isEmpty())
            sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
        this.name = sb.toString().trim();
    }
    public void setDob(String dob) {
        if (dob==null) { this.dob=""; return; }
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            this.dob = df.format(df.parse(dob));
        } catch (ParseException e) { this.dob = dob.trim(); }
    }
    public void setPhoneNum(String phone) {
        phone = phone==null? "" : phone.trim();
        if (!phone.startsWith("0")) phone = "0"+phone;
        this.phoneNum = phone;
    }

    public abstract String save(); 
}

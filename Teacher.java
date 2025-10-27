public class Teacher extends Person {
    private static int cnt = 0;
    private String email;

    public Teacher(String name, String dob, String phone) {
        super("GV" + String.format("%03d", ++cnt), name, dob, phone);
        this.email = genEmail();
    }
    private String genEmail() {
        String clean = getName().toLowerCase().replaceAll("\\s+",".");
        return clean + getId().toLowerCase() + "@school.edu.vn";
    }
    public String getEmail() { return email; }

    @Override
    public String save() {
        return String.join(",", getId(), getName(), getDob(), getPhoneNum(), email);
    }
}

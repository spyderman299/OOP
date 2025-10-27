public class Subject {
    private static int cnt = 0;
    private final String id;
    private String name;
    private int totalSessions;

    public Subject(String name, int total) {
        this.id = "MH" + String.format("%03d", ++cnt);
        setName(name); this.totalSessions = Math.max(0, total);
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public int getTotalSessions() { return totalSessions; }

    public void setName(String n) {
        n = n==null? "" : n.trim().replaceAll("\\s+"," ");
        String[] w = n.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s: w) if(!s.isEmpty())
            sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
        this.name = sb.toString().trim();
    }
    public String save() { return String.join(",", id, name, String.valueOf(totalSessions)); }
}

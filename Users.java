import java.util.*;

public class Users {
    private static final Map<String,String> accounts = new HashMap<>();
    static { 
        accounts.put("admin","admin123"); 
    }
    public static boolean login(String u,String p){ 
        return accounts.containsKey(u) && accounts.get(u).equals(p); 
    }
}

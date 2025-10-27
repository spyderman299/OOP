public class Score {
    private double attendance, homework, midTerm, endTerm;
    public Score() {}
    public Score(double a,double h,double m,double e){ attendance=a; homework=h; midTerm=m; endTerm=e; }
    public double finalScore(){ return attendance*0.1 + homework*0.2 + midTerm*0.3 + endTerm*0.4; }
    public String result(){ return finalScore() >= 5.0 ? "Passed" : "Failed"; }
    @Override public String toString(){
        return String.format("A=%.1f,H=%.1f,M=%.1f,E=%.1f -> %s", attendance,homework,midTerm,endTerm,result());
    }
}

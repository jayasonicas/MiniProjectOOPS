public class Student {
    public String name;
    public String regNo;
    public String department;
    public int maths, digitalPrinciples, computerOrganization, ai, oops, dbms;
    public int total;
    public double average;
    public String grade;
    public String passfail;

    public Student() {}

    public Student(String name, String regNo, String department,
                   int maths, int digitalPrinciples, int computerOrganization,
                   int ai, int oops, int dbms) {
        this.name = name;
        this.regNo = regNo;
        this.department = department;
        this.maths = maths;
        this.digitalPrinciples = digitalPrinciples;
        this.computerOrganization = computerOrganization;
        this.ai = ai;
        this.oops = oops;
        this.dbms = dbms;
        computeDerived();
    }

    public void computeDerived() {
        total = maths + digitalPrinciples + computerOrganization + ai + oops + dbms;
        average = (double) total / 6.0;
        grade = computeGrade(average);
        passfail = (maths >= 50 && digitalPrinciples >= 50 && computerOrganization >= 50
                && ai >= 50 && oops >= 50 && dbms >= 50) ? "PASS" : "FAIL";
    }

    private String computeGrade(double avg) {
        if (avg >= 85) return "A";
        if (avg >= 70) return "B";
        if (avg >= 60) return "C";
        if (avg >= 50) return "D";
        return "F";
    }
}

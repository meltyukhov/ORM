@DBTable(name = "student")
public class Student {
    @SQLInteger(constraints = @Constraints(primaryKey = true)) Integer student_id;
    @SQLString(30) String name;
    @SQLString(30) String surname;
    @SQLInteger Integer mark;
}

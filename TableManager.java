import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class TableManager {
    public void create(String className) {
        try {
            Class<?> cls = Class.forName(className);
            DBTable dbTable = cls.getAnnotation(DBTable.class);
            if (dbTable == null) {
                System.out.println("DBTable annotation not found!");
                return;
            }

            String tableName = dbTable.name();
            if(tableName.length() < 1)
                tableName = cls.getName();

            String query = "";
            for(Field field : cls.getDeclaredFields()) {

                Annotation[] anns = field.getDeclaredAnnotations();

                if(anns[0] instanceof SQLInteger) {
                    SQLInteger sqlInt = (SQLInteger) anns[0];
                    query += (sqlInt.name().equals("")) ? field.getName() : sqlInt.name();
                    query += " INT" + getConctraints(sqlInt.constraints()) + ", ";
                }
                else if(anns[0] instanceof SQLString) {
                    SQLString sqlStr = (SQLString) anns[0];
                    query += (sqlStr.name().equals("")) ? field.getName() : sqlStr.name();
                    query += " VARCHAR(" + sqlStr.value() + ")" + getConctraints(sqlStr.constraints()) + ", ";
                }
            }
            query = "CREATE TABLE " + tableName + "(" + query.substring(0, query.length() - 3) + ");";
            System.out.println(query);

        }
        catch(ClassNotFoundException exc) {
            System.out.println("Class not found");
        }
    }

    private String getConctraints(Constraints con) {
        String constraints = "";
        if (con.notNull()) constraints += " NOT NULL";
        if (con.unique()) constraints += " UNIQUE";
        if (con.primaryKey()) constraints += " PRIMARY KEY";
        return constraints;
    }
}

package pl.drowidia.database;

/**
 * Start form start() :)
 * 
 * @author Dawid Drozd
 * 
 */
// TODO Refactor this for something better
public class SQLiteCreator {

    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_TEXT = "TEXT";
    public static final String FLOAT = "REAL";
    private static StringBuilder stringBuilder = new StringBuilder();
    private static SQLiteCreator INSTANCE;

    private SQLiteCreator() {
    }

    public static SQLiteCreator start() {
	if (INSTANCE == null) {
	    INSTANCE = new SQLiteCreator();
	}
	stringBuilder.delete(0, stringBuilder.length());
	return INSTANCE;
    }

    public SQLiteCreator unique(String... columns) {
	stringBuilder.append(", UNIQUE (");

	stringBuilder.append(columns[0]);

	for (int i = 1; i < columns.length; ++i) {
	    stringBuilder.append(",").append(columns[i]);
	}
	stringBuilder.append(") ON CONFLICT REPLACE ");
	return INSTANCE;
    }

    public SQLiteCreator createTable(String name) {
	stringBuilder.append("CREATE TABLE ").append(name).append(" (");
	return INSTANCE;
    }

    public SQLiteCreator addColumn(String name) {
	if (stringBuilder.charAt(stringBuilder.length() - 1) != '(')
	    stringBuilder.append(",");
	stringBuilder.append(name);
	return INSTANCE;
    }

    public SQLiteCreator withType(String type) {
	if (type != null)
	    stringBuilder.append(" ").append(type);
	return INSTANCE;
    }

    public SQLiteCreator primaryKey() {
	stringBuilder.append(" PRIMARY KEY");
	return INSTANCE;
    }

    public SQLiteCreator primaryKeyAutoIncrement() {
	stringBuilder.append(" PRIMARY KEY AUTOINCREMENT");
	return INSTANCE;
    }

    public SQLiteCreator methodName(String name, String type) {
	stringBuilder.append(name).append(" ").append(type);
	return INSTANCE;
    }

    public SQLiteCreator nullable() {
	stringBuilder.append(" NOT NULL");
	return INSTANCE;
    }

    public String toSql() {
	return stringBuilder.append(" );").toString();
    }

    public SQLiteCreator deleteTable(String tableName) {
	stringBuilder.append("(DROP TABLE IF EXIST ").append(tableName);
	return INSTANCE;
    }

}

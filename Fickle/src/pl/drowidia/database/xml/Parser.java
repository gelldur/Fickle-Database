package pl.drowidia.database.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import pl.drowidia.database.SQLiteCreator;

public class Parser implements DatabaseXmlParser {

    private static final String PRIMARY_KEY = "primaryKey";
    private static final String NULLABLE2 = "nullable";
    private static final String TYPE = "type";
    private final static String CHANGE_LOG = "changeLog";
    private final static String CHANGE_SET = "changeSet";
    private final static int CHANGE_SET_DEPTH = 2;
    private final static String TAG_DATABASE_NAME = "databaseName";
    private final static String TAG_CHANGE_SET_ID = "id";
    private final static String TAG_CHANGE_SET_AUTHOR = "author";
    private final static String CHANGE_CREATE_TABLE = "createTable";
    private final static String CHANGE_DELETE_TABLE = "deleteTable";
    private final static String CHANGE_SQL = "sql";
    private final static String CHANGE_SQL_COMMAND = "command";
    private final static String NAME = "name";

    private XmlPullParser xmlPullParser;
    private String databaseName;

    public static Parser create(XmlPullParser xmlPullParser)
	    throws XmlPullParserException, IOException {

	xmlPullParser.setFeature(
		XmlPullParser.FEATURE_REPORT_NAMESPACE_ATTRIBUTES, true);
	xmlPullParser.next();
	xmlPullParser.nextTag();
	xmlPullParser.require(XmlPullParser.START_TAG, null, CHANGE_LOG);
	return new Parser(xmlPullParser);
    }

    private Parser(XmlPullParser xmlPullParser) {
	this.xmlPullParser = xmlPullParser;

    }

    @Override
    public String getDatabaseName() {
	if (databaseName == null) {
	    databaseName = xmlPullParser.getAttributeValue(null,
		    TAG_DATABASE_NAME);
	    if (databaseName == null)
		throw new ChangeLogException("You don't set database name");
	}
	return databaseName;
    }

    /**
     * 
     * @return null if no other change set available or SQL command from change
     *         set
     * @throws IOException
     * @throws XmlPullParserException
     */
    @Override
    public String nextChangeSet() throws XmlPullParserException, IOException {
	boolean next = true;
	while (next && xmlPullParser != null) {
	    xmlPullParser.nextTag();
	    System.out.println("Depth: " + xmlPullParser.getDepth());
	    if (xmlPullParser.getDepth() != CHANGE_SET_DEPTH)
		continue;
	    try {
		xmlPullParser
			.require(XmlPullParser.START_TAG, null, CHANGE_SET);
		next = false;
	    } catch (XmlPullParserException ex) {
	    }
	}
	return null;
    }

    @Override
    public String nextChange() throws XmlPullParserException, IOException {

	xmlPullParser.nextTag();
	System.out.println(xmlPullParser.getName());
	String changeName = xmlPullParser.getName();

	if (changeName.equals(CHANGE_CREATE_TABLE)) {
	    return parseChangeCreateTable();
	} else if (changeName.equals(CHANGE_DELETE_TABLE)) {
	    return parseChangeDeleteTable();
	} else if (changeName.equals(CHANGE_SQL)) {
	    return parseChangeSqlCommand();
	}

	return null;
    }

    private String parseChangeCreateTable() throws XmlPullParserException,
	    IOException {

	String tableName = xmlPullParser.getAttributeValue(null, NAME);
	if (tableName == null)
	    throw new ChangeLogException("Table must have a name!");

	SQLiteCreator createTableSql = SQLiteCreator.start();
	createTableSql.createTable(tableName);

	xmlPullParser.nextTag();

	System.out.println("Here2");
	// We move to first column
	// This should be a collumn tag
	String createTableChange = xmlPullParser.getName();
	while (!createTableChange.equals(CHANGE_CREATE_TABLE)) {

	    System.out.println("Here");
	    // Reading first column
	    System.out.println(xmlPullParser.getName());
	    String columnName = xmlPullParser.getAttributeValue(null, NAME);
	    if (columnName == null)
		throw new ChangeLogException("Column must have a name!");

	    String columnType = xmlPullParser.getAttributeValue(null, TYPE);
	    String primaryKey = xmlPullParser.getAttributeValue(null,
		    PRIMARY_KEY);
	    String nullable = xmlPullParser.getAttributeValue(null, NULLABLE2);

	    createTableSql.addColumn(columnName).withType(columnType);
	    if (primaryKey != null && Boolean.parseBoolean(primaryKey))
		createTableSql.primaryKey();

	    if (nullable != null && Boolean.parseBoolean(nullable))
		createTableSql.nullable();

	    if (xmlPullParser.nextTag() == XmlPullParser.END_TAG) {
		xmlPullParser.nextTag();
	    }
	    createTableChange = xmlPullParser.getName();
	}

	return createTableSql.toSql();
    }

    private String parseChangeDeleteTable() {
	return SQLiteCreator.start()
		.deleteTable(xmlPullParser.getAttributeValue(null, NAME))
		.toSql();
    }

    private String parseChangeSqlCommand() {
	return xmlPullParser.getAttributeValue(null, CHANGE_SQL_COMMAND);
    }

    @Override
    public String getChangeSetId() {
	String changeSetId = xmlPullParser.getAttributeValue(null,
		TAG_CHANGE_SET_ID);
	if (changeSetId == null)
	    throw new ChangeLogException("No id for this change set!");

	return changeSetId;
    }

    @Override
    public String getChangeSetAuthor() {
	String changeSetAuthor = xmlPullParser.getAttributeValue(null,
		TAG_CHANGE_SET_AUTHOR);
	if (changeSetAuthor == null)
	    throw new ChangeLogException("No author for this change set!");

	return changeSetAuthor;
    }

    @Override
    public int countChangeSets() {
	try {
	    int countChangeSets = 0;
	    while (!(xmlPullParser.nextTag() == XmlPullParser.END_TAG
		    && xmlPullParser.getName().equals(CHANGE_LOG))) {
		System.out.println("Depth: " + xmlPullParser.getDepth());
		if (xmlPullParser.getDepth() != CHANGE_SET_DEPTH)
		    continue;
		try {
		    xmlPullParser.require(XmlPullParser.START_TAG, null,
			    CHANGE_SET);
		    ++countChangeSets;
		} catch (XmlPullParserException ex) {
		}
	    }
	    return countChangeSets;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return 0;
	}
    }
}

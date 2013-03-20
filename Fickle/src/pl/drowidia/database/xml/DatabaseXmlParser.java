package pl.drowidia.database.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

public interface DatabaseXmlParser {

    public String getDatabaseName();

    /**
     * @return true if there is next changeSet
     * @throws XmlPullParserException
     * @throws IOException
     */
    public boolean nextChangeSet() throws XmlPullParserException, IOException;

    /**
     * @return SQL command representing 1 change in this changeSet for example
     *         createTable
     * @throws XmlPullParserException
     * @throws IOException
     */
    public String nextChange() throws XmlPullParserException, IOException;

    /**
     * @return change set id attribute
     */
    public String getChangeSetId();

    /**
     * @return change set author
     */
    public String getChangeSetAuthor();

    /**
     * @return count how much change sets you have in file
     */
    public int countChangeSets();
}

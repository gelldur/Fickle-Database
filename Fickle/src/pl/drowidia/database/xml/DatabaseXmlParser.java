package pl.drowidia.database.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

public interface DatabaseXmlParser {

    public String getDatabaseName();

    public String nextChangeSet() throws XmlPullParserException, IOException;

    public String nextChange() throws XmlPullParserException, IOException;

    public String getChangeSetId();

    public String getChangeSetAuthor();
}

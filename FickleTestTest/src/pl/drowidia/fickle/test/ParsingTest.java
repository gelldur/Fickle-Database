package pl.drowidia.fickle.test;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import pl.drowidia.database.xml.ChangeLogException;
import pl.drowidia.database.xml.DatabaseXmlParser;
import pl.drowidia.database.xml.Parser;
import android.content.res.Resources.NotFoundException;
import android.test.ActivityInstrumentationTestCase2;

public class ParsingTest extends ActivityInstrumentationTestCase2<TestActivity> {

    public ParsingTest() {
	super(TestActivity.class.getPackage().toString(), TestActivity.class);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }

    public void testFirst() throws NotFoundException, XmlPullParserException,
	    IOException {
	TestActivity testActivity = getActivity();
	DatabaseXmlParser parser = Parser.create(testActivity.getResources().getXml(
		R.xml.fickle_test));

	assertEquals("fickle_test_database", parser.getDatabaseName());

	try {
	    parser.getChangeSetAuthor();
	    assertTrue(false);
	} catch (ChangeLogException ex) {
	    assertTrue(true);
	}

	assertEquals(true, parser.nextChangeSet());

	checkChangeSetAtributes("John", "1", parser);

	String string = parser.nextChange();
	System.out.println("Change: " + string);

	string = parser.nextChange();
	System.out.println("Change: " + string);

	string = parser.nextChange();
	System.out.println("Change: " + string);

	assertEquals(true, parser.nextChangeSet());

	checkChangeSetAtributes("john2", "2", parser);
    }

    public void testCheckChangeSetCount() throws NotFoundException,
	    XmlPullParserException, IOException {

	Parser parser = Parser.create(getActivity().getResources().getXml(
		R.xml.fickle_test));
	assertEquals(2, parser.countChangeSets());
    }

    private void checkChangeSetAtributes(String expectedAuthor,
	    String expectedId, DatabaseXmlParser parser) {
	assertEquals(expectedAuthor, parser.getChangeSetAuthor());
	assertEquals(expectedId, parser.getChangeSetId());

	assertNotSame(expectedAuthor, parser.getChangeSetId());
	assertNotSame(expectedId, parser.getChangeSetAuthor());
    }
}

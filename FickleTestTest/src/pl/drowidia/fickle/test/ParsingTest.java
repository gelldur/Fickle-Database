package pl.drowidia.fickle.test;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import pl.drowidia.database.xml.ChangeLogException;
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
	Parser parser = Parser.create(testActivity.getResources().getXml(
		R.xml.fickle_test));

	assertEquals("fickle_test_database", parser.getDatabaseName());

	try {
	    parser.getChangeSetAuthor();
	    assertTrue(false);
	} catch (ChangeLogException ex) {
	    assertTrue(true);
	}

	parser.nextChangeSet();

	checkChangeSetAtributes("John", "1", parser);

	String string = parser.nextChange();

	parser.nextChangeSet();

	checkChangeSetAtributes("john2", "2", parser);
    }

    private void checkChangeSetAtributes(String expectedAuthor,
	    String expectedId, Parser parser) {
	assertEquals(expectedAuthor, parser.getChangeSetAuthor());
	assertEquals(expectedId, parser.getChangeSetId());

	assertNotSame(expectedAuthor, parser.getChangeSetId());
	assertNotSame(expectedId, parser.getChangeSetAuthor());
    }
}
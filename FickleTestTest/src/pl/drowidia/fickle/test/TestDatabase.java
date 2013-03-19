package pl.drowidia.fickle.test;

import pl.drowidia.database.DatabaseHelper;
import android.test.ActivityInstrumentationTestCase2;

public class TestDatabase extends
	ActivityInstrumentationTestCase2<TestActivity> {

    public TestDatabase() {
	super(TestActivity.class.getPackage().toString(), TestActivity.class);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }

    public void testCreate() {

	DatabaseHelper databaseHelper = new DatabaseHelper(getActivity(),
		R.xml.test_database);

	databaseHelper.getWritableDatabase().close();

    }
}

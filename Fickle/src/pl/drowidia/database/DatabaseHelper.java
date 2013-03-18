package pl.drowidia.database;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import pl.drowidia.database.xml.ChangeLogException;
import pl.drowidia.database.xml.Parser;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.util.Xml;

public class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static String getDatabaseName(Context context, int databaseFile) {

	System.out.println("================================");
	try {
	    Parser parser = Parser.create(context.getResources().getXml(
		    databaseFile));
	    String databaseName = parser.getDatabaseName();
	    System.out.println(databaseName);
	    parser.nextChangeSet();

	    System.out.println(parser.getChangeSetAuthor());

	} catch (NotFoundException e) {
	    e.printStackTrace();
	} catch (XmlPullParserException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

    private static int getDatabaseVersion() {
	// TODO get count of change sets
	return 1;
    }

    public DatabaseHelper(Context context, int databaseFile) {
	super(context, getDatabaseName(context, databaseFile), null,
		getDatabaseVersion());

	if (checkChangeLog()) {
	    throw new ChangeLogException("Someone modified old change set!");
	}
    }

    private boolean checkChangeLog() {
	return false;
	// Check MD5? or smth of every old change set is is modified
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// TODO execute every change set
    }
}

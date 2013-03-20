package pl.drowidia.database;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import pl.drowidia.database.xml.ChangeLogException;
import pl.drowidia.database.xml.Parser;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static Parser parser;

    private static String getDatabaseName(Context context, int databaseFile) {

	try {
	    parser = Parser.create(context.getResources().getXml(databaseFile));
	    return parser.getDatabaseName();
	} catch (NotFoundException e) {
	    e.printStackTrace();
	} catch (XmlPullParserException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private static int getDatabaseVersion(Context context, int databaseFile) {
	try {
	    Parser parserCount = Parser.create(context.getResources().getXml(
		    databaseFile));
	    return parserCount.countChangeSets() + 1;
	} catch (NotFoundException e) {
	    e.printStackTrace();
	} catch (XmlPullParserException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return -1;
    }

    public DatabaseHelper(Context context, int databaseFile) {
	super(context, getDatabaseName(context, databaseFile), null,
		getDatabaseVersion(context, databaseFile));

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
	try {
	    applyPatches(db, 1, Integer.MAX_VALUE);
	} catch (XmlPullParserException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void applyPatches(SQLiteDatabase database, int oldVersion,
	    int newVersion) throws XmlPullParserException, IOException {
	int patch;
	for (patch = 1; patch < oldVersion; ++patch) {
	    if (parser.nextChangeSet() == false) {
		return;
	    }
	    // This patch was applied
	}

	for (; patch <= newVersion; ++patch) {
	    Log.d(this.toString(), "Next changeset");
	    if (parser.nextChangeSet() == false) {
		Log.d(this.toString(), "Last change set");
		return;
	    }
	    String sqlPatch;
	    while ((sqlPatch = parser.nextChange()) != null) {
		Log.d(this.toString(), "Applying change:" + sqlPatch);
		database.execSQL(sqlPatch);
	    }
	}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	try {
	    applyPatches(db, oldVersion, newVersion);
	} catch (XmlPullParserException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}

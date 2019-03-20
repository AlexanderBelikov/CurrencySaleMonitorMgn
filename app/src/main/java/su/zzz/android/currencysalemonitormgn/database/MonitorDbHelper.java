package su.zzz.android.currencysalemonitormgn.database;

import su.zzz.android.currencysalemonitormgn.database.MonitorDbSchema.CourseTable;
import su.zzz.android.currencysalemonitormgn.Course;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MonitorDbHelper extends SQLiteOpenHelper {
    private static final String TAG = MonitorDbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "course_db";

    private static MonitorDbHelper sInstance;

    public static synchronized MonitorDbHelper getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new MonitorDbHelper(context);
        }
        return sInstance;
    }

    public MonitorDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ CourseTable.NAME + "("+
                " _id integer primary key autoincrement, " +
                CourseTable.Cols.UUID + ", " +
                CourseTable.Cols.BANK + ", " +
                CourseTable.Cols.DATE + ", " +
                CourseTable.Cols.USD + ", " +
                CourseTable.Cols.EUR +
                ")");
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void cleanCourses() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ CourseTable.NAME);
    }
    public void insertCourse(Course course) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues courseValues = new ContentValues();
        courseValues.put(CourseTable.Cols.UUID, course.getUUID().toString());
        courseValues.put(CourseTable.Cols.BANK, course.getBank());
        courseValues.put(CourseTable.Cols.DATE, course.getDate().getTime());
        courseValues.put(CourseTable.Cols.USD, course.getUSD());
        courseValues.put(CourseTable.Cols.EUR, course.getEUR());
        db.insert(CourseTable.NAME, null, courseValues);
        db.close();
    }

    public List getMinCourseList(String currency) {
        List<Course> courseList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String MIN_CURRENCY_QUERY = String.format("SELECT * FROM %s WHERE %s = (SELECT MIN(%s) FROM %s)", CourseTable.NAME, currency, currency, CourseTable.NAME);
        Cursor cursor = db.rawQuery(MIN_CURRENCY_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    courseList.add(new Course(
                                UUID.fromString(cursor.getString(cursor.getColumnIndex(CourseTable.Cols.UUID))),
                                cursor.getString(cursor.getColumnIndex(CourseTable.Cols.BANK)),
                                new Date(cursor.getLong(cursor.getColumnIndex(CourseTable.Cols.DATE))),
                                cursor.getFloat(cursor.getColumnIndex(CourseTable.Cols.USD)),
                                cursor.getFloat(cursor.getColumnIndex(CourseTable.Cols.EUR))
                                ));
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return courseList;
    }
    public float getMinCourse(String currency){
        float minCourse = 0.0f;
        List<Course> courseList = getMinCourseList(currency);
        if(courseList.size() != 0){
            if(currency == MonitorDbSchema.CourseTable.Cols.USD) {
                minCourse = courseList.get(0).getUSD();
            } else if (currency == MonitorDbSchema.CourseTable.Cols.EUR) {
                minCourse = courseList.get(0).getEUR();
            }
        }
        return minCourse;
    }
}

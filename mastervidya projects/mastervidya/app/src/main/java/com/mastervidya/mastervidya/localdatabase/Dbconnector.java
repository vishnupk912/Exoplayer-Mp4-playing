package com.mastervidya.mastervidya.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mastervidya.mastervidya.video.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class Dbconnector extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mastervidya";

    private static final String TABLE_VIDEO = "tbl_video";
    private static final String KEY_ID= "id";
    private static final String KEY_CLASS = "class";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_CHAPTER = "chapter";


    private static final String KEY_VIDEONAME = "videoname";
    private static final String KEY_VIDEOURL = "videourl";


    public Dbconnector(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CATEGORY_TABLE = " CREATE TABLE IF NOT EXISTS "
                + TABLE_VIDEO
                + "("
                + KEY_ID + " TEXT,"
                + KEY_CLASS + " TEXT,"
                + KEY_SUBJECT + " TEXT,"
                + KEY_CHAPTER + " TEXT,"
                + KEY_VIDEONAME + " TEXT,"
                + KEY_VIDEOURL + " TEXT"
                + ")";

        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);

    }

    public void addvideo(VideoModel videoModel) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, videoModel.getVideoId());
            values.put(KEY_CLASS, videoModel.getClassname());
            values.put(KEY_VIDEONAME, videoModel.getVideoName());
            values.put(KEY_CHAPTER, videoModel.getChaptername());
            values.put(KEY_VIDEOURL, videoModel.getVideoUrl());
            values.put(KEY_SUBJECT, videoModel.getSubjectname());
            db.insert(TABLE_VIDEO, null, values);
            db.close();
        }
        catch (Exception ignored)
        {

        }

    }

    public List<VideoModel> getallvideo() {

        List<VideoModel> videoModelArrayList = new ArrayList<>();

        try
        {
            String selectQuery = "SELECT  * FROM " + TABLE_VIDEO;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    VideoModel videoModel = new VideoModel();
                    videoModel.setVideoId(cursor.getString(0));
                    videoModel.setClassname(cursor.getString(1));
                    videoModel.setSubjectname(cursor.getString(2));
                    videoModel.setChaptername(cursor.getString(3));
                    videoModel.setVideoName(cursor.getString(4));
                    videoModel.setVideoUrl(cursor.getString(5));



                    videoModelArrayList.add(videoModel);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch (Exception ignored)
        {

        }

        return videoModelArrayList;
    }


    public void reset_table(){

        List<VideoModel> allmodelCategoryAll = new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();

        db.execSQL("Delete from "+ TABLE_VIDEO);

    }



    public void getsum()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + KEY_CLASS + ") as Total FROM " + TABLE_VIDEO, null);
        int total = cursor.getInt(cursor.getColumnIndex(KEY_CLASS));// get final total

        Log.d("total sum", String.valueOf(total));
    }

    public void updatecart(VideoModel videoModel)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, videoModel.getVideoId());
        values.put(KEY_CLASS, videoModel.getClassname());
        values.put(KEY_VIDEONAME, videoModel.getVideoName());
        values.put(KEY_CHAPTER, videoModel.getChaptername());
        values.put(KEY_VIDEOURL, videoModel.getVideoUrl());
        values.put(KEY_SUBJECT, videoModel.getSubjectname());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_VIDEO, values, KEY_ID + " = ?", new String[]{String.valueOf(videoModel.getVideoId())});
    }

    public long Delete(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        try
        {

            return db.delete(TABLE_VIDEO,KEY_ID+" =?",new String[]{String.valueOf(id)});

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }



    public boolean Exists(String user){
        String selectQuery = "SELECT  * FROM " + TABLE_VIDEO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        int flag=0;
        while (res.moveToNext()){
            String id =res.getString(0);
            if(id.equals(user)){
                flag++;
            }
        }

        if(flag==0){
            return false;
        }
        else {
            return true;
        }
    }



}

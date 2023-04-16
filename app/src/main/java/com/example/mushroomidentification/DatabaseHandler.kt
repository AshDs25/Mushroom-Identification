package com.example.mushroomidentification

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context

val DATABASE_NAME = "MyDB"
val TABLE_NAME = "Mushy"
val Col_Cname = "CName"
val Col_Sciname = "Sname"
val Col_ID = "id"


class DatabaseHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        // This function is executed when the device doesn't contain the db
        val createTable =
            "CREATE TABLE $TABLE_NAME ($Col_ID INTEGER PRIMARY KEY AUTOINCREMENT,$Col_Cname VARCHAR(256),$Col_Sciname VARCHAR(256));"
        // To execute(create table)
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVerison: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(mush_obj: mush_obj){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(Col_Cname,mush_obj.c_name)
        cv.put(Col_Sciname,mush_obj.sci_name)
        // inserting the values in table. It returns the row_id (caught in result)
        var result = db.insert(TABLE_NAME,null, cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }
}

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
val Col_Accuracy = "Accu"
val Col_ID = "id"


class DatabaseHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        // This function is executed when the device doesn't contain the db
        val createTable =
            "CREATE TABLE $TABLE_NAME ($Col_ID INTEGER PRIMARY KEY AUTOINCREMENT,$Col_Cname VARCHAR(256),$Col_Sciname VARCHAR(256),$Col_Accuracy VARCHAR(256));"
        // To execute(create table)
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVerison: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(MushroomItem: MushroomItem){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(Col_Cname,MushroomItem.c_name)
        cv.put(Col_Sciname,MushroomItem.sci_name)
        cv.put(Col_Accuracy,MushroomItem.accuracy)
        // inserting the values in table. It returns the row_id (caught in result)
        var result = db.insert(TABLE_NAME,null, cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }

    fun readData() : MutableList<MushroomItem>{
        var list : MutableList<MushroomItem> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                val obj = MushroomItem()
                obj.id = result.getString(0).toInt()
                obj.c_name = result.getString(1).toString()
                obj.sci_name = result.getString(2).toString()
                obj.accuracy = result.getString(3).toString()
                list.add(obj)
            }while(result.moveToNext())
        }
        result.close()
        return list
    }
}

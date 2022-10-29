package com.greedy.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.greedy.vincent.Comment

/* 안드로이드에는 경량형 데이터 베이스 SQLite가 내장되어 있다.
* 데이터베이스를 파일로 생성하고 코틀린 코드에서 사용할 수 있도록 데이터 베이스와 연결하는 역할을 하는
* Helper 클래스가 있다.
* Context, 데이터베이스명, 팩토리, 버전정보가 생성자로 전달되는데 팩토리는 사용하지 않으므로 세가지 정보만 전달한다. */
class SqliteHelper (context: Context, name: String, version: Int) : SQLiteOpenHelper(context, name, null, version) {

    /* 데이터베이스가 생성될 때 동작하는 메소드 */
    override fun onCreate(db: SQLiteDatabase?) {

        /* 데이터베이스가 생성될 때 테이블을 생성한다. */
        val create = "create table comment (" +
                "no integer primary key, " +
                "content text, " +
                "datetime integer " +
                ")"

        db?.execSQL(create)
    }

    /* 데이터베이스가 업그레이드 될 때 동작하는 메소드 */
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        /* 내용 없음 */
    }

    /* 1. insert */
    fun insertComment(comment: Comment) {

        /* 저장할 데이터를 ContentValues에 key value 방식으로 저장한다. */
        val values = ContentValues()
        values.put("content", comment.content)
        values.put("datetime", comment.datetime)

        /* writableDatabase 속성에 테이블명, 작성한 값을 전달하여 insert한다.
        * 두 번째 인자는 ContentValues가 null일 경우 insert 될 수 없어 한 개라도 컬럼을 지정해줘야 하기
        * 때문에 존재하는 것이지만 ContentValues가 잘 만들어져 있다면 null을 전달해도 된다. */
        val wd = writableDatabase
        wd.insert("comment", null, values)
        wd.close()
    }

    /* 2. select */
    @SuppressLint("Range")
    fun selectComment(): MutableList<Comment> {

        /* 데이터베이스가 존재하지 않는다면 onCreate를 호출해서 테이블을 생성한다. */
        if(readableDatabase == null) {
            onCreate(readableDatabase)
        }

        /* 조회 시에는 readableDatabase를 이용한다. */
        val rd = readableDatabase

        val select = "select * from comment"
        val list = mutableListOf<Comment>()

        /* 조회 결과는 cursor 형태로 되돌아 오는데 조회 시 쿼리문과 쿼리문에 전달할 값을 인자로 전달한다. */
        var cursor = rd.rawQuery(select, null)

        while(cursor.moveToNext()) {
            val no = cursor.getLong(cursor.getColumnIndex("no"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))
            list.add(Comment(no, content, datetime))
        }

        cursor.close()
        rd.close()

        return list
    }

    /* 3. delete */
    fun deleteMemo(comment: Comment) {
        val delete = "delete from comment where no = ${comment.no}"
        val db = writableDatabase
        db.execSQL(delete)
        db.close()
    }


}
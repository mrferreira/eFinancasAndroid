package br.com.efinancas.android.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author: Misael Ferreira
 * Date: 14/12/13
 * Time: 17:43
 */
public class MySQLiteContaHelper extends SQLiteOpenHelper {

    public static final String TABLE_CONTA = "conta";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_DESCRICAO = "descricao";
    public static final String COLUMN_VALOR_INICIAL = "valor_inicial";
    public static final String COLUMN_TOTAL = "total";

    private static final String DATABASE_NAME = "conta.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_CONTA + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NOME
            + " text not null, " + COLUMN_DESCRICAO
            + " text, " + COLUMN_VALOR_INICIAL
            + " real default 0 , " + COLUMN_TOTAL
            + " real);";

    public MySQLiteContaHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteContaHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTA);
        onCreate(db);
    }
}

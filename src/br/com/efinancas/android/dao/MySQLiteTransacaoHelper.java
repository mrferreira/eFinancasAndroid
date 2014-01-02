package br.com.efinancas.android.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author: Misael Ferreira
 * Date: 14/12/13
 * Time: 18:53
 */
public class MySQLiteTransacaoHelper extends SQLiteOpenHelper {

    public static final String TABLE_TRANSACAO = "transacao";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_ID_CONTA = "id_conta";
    public static final String COLUMN_TIPO = "tipo"; // entrada|saída
    public static final String COLUMN_ID_CATEGORIA = "id_categoria";
    public static final String COLUMN_DESCRICAO = "descricao";
    public static final String COLUMN_ID_TAG = "idTag";
    public static final String COLUMN_VALOR = "valor";

    private static final String DATABASE_NAME = "transacao.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRANSACAO + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATA
            + " real not null, " + COLUMN_NOME
            + " text, " + COLUMN_ID_CONTA
            + " integer not null, " + COLUMN_TIPO // 0 -> entrada | 1 -> saída
            + " integer not null, " + COLUMN_ID_CATEGORIA
            + " integer default 0, " + COLUMN_DESCRICAO
            + " text , " + COLUMN_ID_TAG
            + " integer, " + COLUMN_VALOR
            + " real not null);";

    public MySQLiteTransacaoHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteTransacaoHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACAO);
        onCreate(db);
    }
}

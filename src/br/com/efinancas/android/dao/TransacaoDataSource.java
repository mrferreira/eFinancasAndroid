package br.com.efinancas.android.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Misael Ferreira
 * Date: 14/12/13
 * Time: 19:46
 */
public class TransacaoDataSource {
    private SQLiteDatabase database;
    private MySQLiteTransacaoHelper dbHelper;
    private String[] allColumns = {
            MySQLiteTransacaoHelper.COLUMN_ID,
            MySQLiteTransacaoHelper.COLUMN_DATA ,
            MySQLiteTransacaoHelper.COLUMN_NOME ,
            MySQLiteTransacaoHelper.COLUMN_ID_CONTA ,
            MySQLiteTransacaoHelper.COLUMN_TIPO,
            MySQLiteTransacaoHelper.COLUMN_ID_CATEGORIA ,
            MySQLiteTransacaoHelper.COLUMN_DESCRICAO ,
            MySQLiteTransacaoHelper.COLUMN_ID_TAG ,
            MySQLiteTransacaoHelper.COLUMN_VALOR };

    public TransacaoDataSource(Context context) {
        dbHelper = new MySQLiteTransacaoHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Transacao createTransacao(Transacao transacao) {
        ContentValues values = getContentValues(transacao);
        long insertId = database.insert(MySQLiteTransacaoHelper.TABLE_TRANSACAO, null,
                values);
        Cursor cursor = database.query(MySQLiteTransacaoHelper.TABLE_TRANSACAO,
                allColumns, MySQLiteTransacaoHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Transacao newTransacao = cursorToTransacao(cursor);
        cursor.close();
        return newTransacao;
    }

    public Transacao updateTransacao(Transacao transacao) {
        ContentValues values = getContentValues(transacao);
        long insertId = database.update(MySQLiteTransacaoHelper.TABLE_TRANSACAO,values,
                String.format("_id = %d", transacao.getId()),null);
        Cursor cursor = database.query(MySQLiteTransacaoHelper.TABLE_TRANSACAO,
                allColumns, MySQLiteTransacaoHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Transacao newTransacao = cursorToTransacao(cursor);
        cursor.close();
        return newTransacao;
    }

    private ContentValues getContentValues(Transacao transacao) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteTransacaoHelper.COLUMN_DATA, transacao.getData().getTime());
        values.put(MySQLiteTransacaoHelper.COLUMN_NOME,transacao.getNome());
        values.put(MySQLiteTransacaoHelper.COLUMN_ID_CONTA,transacao.getIdConta());
        values.put(MySQLiteTransacaoHelper.COLUMN_TIPO, transacao.getTipo());
        values.put(MySQLiteTransacaoHelper.COLUMN_ID_CATEGORIA,transacao.getIdCategoria());
        values.put(MySQLiteTransacaoHelper.COLUMN_DESCRICAO,transacao.getDescricao());
        values.put(MySQLiteTransacaoHelper.COLUMN_ID_TAG,transacao.getIdTag());
        values.put(MySQLiteTransacaoHelper.COLUMN_VALOR,transacao.getValor());
        return values;
    }

    public void deletetransacao(Transacao transacao) {
        long id = transacao.getId();
        System.out.println("transacao deleted with id: " + id);
        database.delete(MySQLiteTransacaoHelper.TABLE_TRANSACAO, MySQLiteTransacaoHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Transacao> getAlltransacaos() {
        List<Transacao> transacaos = new ArrayList<Transacao>();

        Cursor cursor = database.query(MySQLiteTransacaoHelper.TABLE_TRANSACAO,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Transacao transacao = cursorToTransacao(cursor);
            transacaos.add(transacao);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return transacaos;
    }

    public List<Transacao> listTransacaoByIdConta(long idConta) {
        List<Transacao> result = new ArrayList<Transacao>();

        Cursor cursor = database.query(MySQLiteTransacaoHelper.TABLE_TRANSACAO,
                allColumns, MySQLiteTransacaoHelper.COLUMN_ID_CONTA + " = " + idConta, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Transacao transacao = cursorToTransacao(cursor);
            result.add(transacao);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return result;
    }

    private Transacao cursorToTransacao(Cursor cursor) {
        Transacao transacao = new Transacao();
        transacao.setId(cursor.getLong(0));

        Long tmpDt = cursor.getLong(1);
        Date dt = new Date(tmpDt);

        transacao.setData(dt);
        transacao.setNome(cursor.getString(2));
        transacao.setIdConta(cursor.getInt(3));
        transacao.setTipo(cursor.getInt(4));
        transacao.setIdCategoria(cursor.getInt(5));
        transacao.setDescricao(cursor.getString(6));
        transacao.setIdTag(cursor.getInt(7));
        transacao.setValor(cursor.getDouble(8));
        return transacao;
    }

    public Transacao getTransacaoById(long idTransacao) {

        Transacao result = new Transacao();

        Cursor cursor = database.query(MySQLiteTransacaoHelper.TABLE_TRANSACAO,
                allColumns, MySQLiteTransacaoHelper.COLUMN_ID + " = " + idTransacao, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result = cursorToTransacao(cursor);

            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return result;
    }
}

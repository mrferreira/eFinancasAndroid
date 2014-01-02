package br.com.efinancas.android.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Misael Ferreira
 * Date: 14/12/13
 * Time: 17:54
 */
public class ContaDataSource {
    private SQLiteDatabase database;
    private MySQLiteContaHelper dbHelper;
    private String[] allColumns = { MySQLiteContaHelper.COLUMN_ID,
            MySQLiteContaHelper.COLUMN_NOME ,
            MySQLiteContaHelper.COLUMN_DESCRICAO ,
            MySQLiteContaHelper.COLUMN_VALOR_INICIAL ,
            MySQLiteContaHelper.COLUMN_TOTAL };

    public ContaDataSource(Context context) {
        dbHelper = new MySQLiteContaHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Conta criarConta(Conta conta) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteContaHelper.COLUMN_NOME, conta.getNome());
        values.put(MySQLiteContaHelper.COLUMN_DESCRICAO, conta.getDescricao());
        values.put(MySQLiteContaHelper.COLUMN_VALOR_INICIAL,conta.getValorInicial());
        values.put(MySQLiteContaHelper.COLUMN_TOTAL,conta.getTotal());
        long insertId = database.insert(MySQLiteContaHelper.TABLE_CONTA, null,
                values);
        Cursor cursor = database.query(MySQLiteContaHelper.TABLE_CONTA,
                allColumns, MySQLiteContaHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Conta newConta = cursorToConta(cursor);
        cursor.close();
        return newConta;
    }
    
    public Conta atualizarConta(Conta conta){
        ContentValues values = new ContentValues();
        values.put(MySQLiteContaHelper.COLUMN_NOME,conta.getNome());
        values.put(MySQLiteContaHelper.COLUMN_DESCRICAO,conta.getDescricao());
        values.put(MySQLiteContaHelper.COLUMN_VALOR_INICIAL,conta.getValorInicial());
        long insertId = database.update(MySQLiteContaHelper.TABLE_CONTA,values, "_id = " + conta.getId(),null);
        Cursor cursor = database.query(MySQLiteContaHelper.TABLE_CONTA,
                allColumns, MySQLiteContaHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Conta newConta = cursorToConta(cursor);
        cursor.close();
        return newConta;
    }

    public void excluirConta(Conta conta) {
        long id = conta.getId();
        System.out.println("conta deleted with id: " + id);
        database.delete(MySQLiteContaHelper.TABLE_CONTA, MySQLiteContaHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Conta> listarTudo() {
        List<Conta> contas = new ArrayList<Conta>();

        Cursor cursor = database.query(MySQLiteContaHelper.TABLE_CONTA,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Conta conta = cursorToConta(cursor);
            contas.add(conta);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return contas;
    }

    public Conta getContaById(long idConta) {
        Conta result = new Conta();

        Cursor cursor = database.query(MySQLiteContaHelper.TABLE_CONTA,
                allColumns, MySQLiteContaHelper.COLUMN_ID + " = " + idConta, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result = cursorToConta(cursor);

            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return result;
    }

    private Conta cursorToConta(Cursor cursor) {
        Conta conta = new Conta();
        conta.setId(cursor.getLong(0));
        conta.setNome(cursor.getString(1));
        conta.setDescricao(cursor.getString(2));
        conta.setValorInicial(cursor.getDouble(3));
        conta.setTotal(cursor.getDouble(4));
        return conta;
    }
}

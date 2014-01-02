package br.com.efinancas.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.efinancas.android.dao.Conta;
import br.com.efinancas.android.dao.ContaDataSource;

/**
 * @author: Misael Ferreira
 * Date: 15/12/13
 * Time: 08:38
 */
public class EditarConta extends Activity implements View.OnClickListener{

    // LAYOUT FIELDS DECLARATIONS
    private Button btnSalvar;
    private EditText txtNome;
    private EditText txtDescricao;
    private EditText txtTipo;
    private EditText txtValorInicial;
    // FIELDS DECLARATIONS
    private ContaDataSource contaDataSource;
    Conta conta;
    Long _idConta;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_conta);

        inicializarTudo();
    }

    private void inicializarTudo() {
        inicializarCampos();
        inicializarDataSource();
        inicializarConta();
    }

    private void inicializarCampos() {
        d("inicializando campos");
        if(txtNome == null){
            txtNome = (EditText)findViewById(R.id.txtNomeContaEC);
        }
        if(txtDescricao == null){
            txtDescricao = (EditText)findViewById(R.id.txtDescricaoContaEC);
        }
        if(txtTipo == null){
            txtTipo = (EditText)findViewById(R.id.txtTipoContaEC);
        }
        if(txtValorInicial == null){
            txtValorInicial = (EditText)findViewById(R.id.txtValorInicContaEC);
        }
        if(btnSalvar == null){
            btnSalvar = (Button)findViewById(R.id.btnSalvarEC);
            btnSalvar.setOnClickListener(this);
        }
        d("campos inicializados");
    }

    private void inicializarDataSource() {
        d("inicializando datasource");
        contaDataSource = new ContaDataSource(this);
        contaDataSource.open();
        d("datasource inicializado");
    }

    private void inicializarConta() {
        d("inicializando conta");
        if(getIdConta() != 0){
            conta = contaDataSource.getContaById(getIdConta());
        }else{
            conta = new Conta();
        }
        contaToFields(conta);
        d("conta inicializada");
    }

    private long getIdConta() {
        if(_idConta == null){
        Intent i = getIntent();
            _idConta = i.getLongExtra(Constants.CONTA_SELECIONADA, 0);
        }
        d("conta retornada > " + _idConta);
        return _idConta;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSalvarEC:
                atualizarDados(fieldsToConta());
                concluir();

                break;
        }
    }

    private Conta fieldsToConta() {
        conta.setNome(txtNome.getText().toString());
        conta.setDescricao(txtDescricao.getText().toString());
        conta.setValorInicial(Double.valueOf(txtValorInicial.getText().toString()));
        return conta;
    }

    private void contaToFields(Conta conta) {
        txtNome.setText(conta.getNome());
        txtDescricao.setText(conta.getDescricao());
        txtValorInicial.setText(String.valueOf(conta.getValorInicial()));
        //TODO salvar tipo de conta (corrente | cheque | cartão de crédito | outro)
    }

    private void atualizarDados(Conta conta) {
        fieldsToConta();
        if(getIdConta() == 0){   // nova conta
            contaDataSource.criarConta(conta);
            Toast.makeText(EditarConta.this, "Conta " + conta.getNome() + " criada com sucesso",
                    Toast.LENGTH_LONG).show();
        }else{                  // atualizar conta
            contaDataSource.atualizarConta(conta);
            Toast.makeText(EditarConta.this,"Conta " + conta.getNome() + " atualizada com sucesso",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void concluir() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(EditarConta.this,MyActivity.class);
        startActivity(intent);
    }

    private void d(String msg){
        Log.d(Constants.LOG, msg);
    }

}

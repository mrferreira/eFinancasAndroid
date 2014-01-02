package br.com.efinancas.android;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import br.com.efinancas.android.dao.Transacao;
import br.com.efinancas.android.dao.TransacaoDataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author: Misael Ferreira
 * Date: 14/12/13
 * Time: 15:56
 */
public class EditarTransacao extends Activity {

    // LAYOUT FIELDS DECLARATIONS
    private EditText txtData;
    private EditText txtNome;
    private EditText txtDescricao;
    private Spinner spTipoTransacao;
    private EditText txtValor;
    private Button btnSalvar;
    // FIELDS DECLARATIONS
    private static int SELECTED_TIPO_TRANSACAO = 0;
    private Calendar myCalendar = Calendar.getInstance();
    private Transacao transacao;
    private TransacaoDataSource transacaoDataSource;
    private Long _idConta;
    private Long _idTransacao;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.editar_transacao);

        d("\n\ninicializando EditarTransacao");
        inicializarTudo();

    }

    private void inicializarTudo() {
        inicializarCampos();
        inicializarDatasource();
        inicializarTransacao();
    }

    private void inicializarCampos() {
        if(txtNome == null){
            txtNome = (EditText)findViewById(R.id.txtNomeTransacaoET);
        }
        if(txtValor == null){
            txtValor = (EditText)findViewById(R.id.txtValorET);
            txtValor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txtValor.selectAll();
                }
            });
            txtValor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(!view.hasFocus()){ // verificar se o campo contem algum valor quando perder o foco
                        if(txtValor.getText().toString() == ""){
                            toast("Digite um valor para a transação");
                            txtValor.requestFocus();
                        }
                    }
                }
            });
        }
        if(txtDescricao == null){
            txtDescricao = (EditText)findViewById(R.id.txtDescricaoET);
        }
        if(spTipoTransacao == null){
            spTipoTransacao = (Spinner)findViewById(R.id.spTipoTransacaoET);
        }
        if(txtData == null){
            txtData = (EditText)findViewById(R.id.txtDataET);
            txtData.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new DatePickerDialog(EditarTransacao.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
        if(btnSalvar == null){
            btnSalvar = (Button)findViewById(R.id.btnSalvarET);
            btnSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        fieldsToTransacao();
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(), "Não foi possível salvar a transação", Toast.LENGTH_SHORT);
                    }

                    if(transacao.getId() == 0){
                        transacaoDataSource.createTransacao(transacao);
                    }else{
                        transacaoDataSource.updateTransacao(transacao);
                    }

                    concluir();
                }
            });
        }
    }

    private void inicializarDatasource() {
        transacaoDataSource = new TransacaoDataSource(EditarTransacao.this);
        transacaoDataSource.open();
    }

    private void inicializarTransacao() {
        if(getIdTransacao() != 0){
            transacao = transacaoDataSource.getTransacaoById(getIdTransacao());
        }else{
            transacao = new Transacao();
        }
        transacaoToFields();
    }

    private void concluir() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(this,ListarTransacoesPorConta.class);
        intent.putExtra(Constants.CONTA_SELECIONADA,getIdConta());
        startActivity(intent);
    }

    private void transacaoToFields() {
        txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(transacao.getData()));
        txtNome.setText(transacao.getNome());
        txtValor.setText(String.valueOf(transacao.getValor()));
        txtDescricao.setText(transacao.getDescricao());
        spTipoTransacao.setAdapter(
                ArrayAdapter.createFromResource(this,
                        R.array.tipoTransacao, android.R.layout.simple_spinner_dropdown_item));
        spTipoTransacao.setOnItemSelectedListener(new ComboTipoTransacaoListener());
        spTipoTransacao.setSelection(transacao.getTipo());
    }

    private long getIdConta(){
        if(_idConta == null){
            Intent i = getIntent();
            _idConta = i.getLongExtra(Constants.CONTA_SELECIONADA, 0);
        }
        d("conta recuperada > " + _idConta);
        return _idConta;
    }

    private long getIdTransacao(){
        if(_idTransacao == null){
            _idTransacao = getIntent().getLongExtra(Constants.TRANSACAO_SELECIONADA, 0);
        }
        d("transacao recuperada > " + _idTransacao);
        return _idTransacao;
    }

    private void fieldsToTransacao() throws Exception{
        transacao.setNome(txtNome.getText().toString());
        try{
            String v = txtValor.getText().toString();
            if(v != "")
            transacao.setValor(Double.valueOf(v));
        }catch(NumberFormatException e){
            throw new Exception("Valor inválido");
        }
        transacao.setIdTag(0); //TODO configurar TAG
        transacao.setTipo(getTipoTransacaoSelecionada());
        transacao.setDescricao(txtDescricao.getText().toString());

        String dt = txtData.getText().toString();
        Date data = null;
        try {
            if(dt != ""){
                data = new SimpleDateFormat("dd/MM/yyyy").parse(dt);
                transacao.setData(data);
            }
        } catch (ParseException e) {
            toast(new StringBuilder("erro ao converter data ").append(dt).toString());
        }
        transacao.setIdCategoria(0);
        transacao.setIdConta(getIdConta());
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtData.setText(sdf.format(myCalendar.getTime()));
    }

    public void toast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void setSELECTED_TIPO_TRANSACAO(int value){
        SELECTED_TIPO_TRANSACAO = value;
    }

    public int getTipoTransacaoSelecionada(){
        return SELECTED_TIPO_TRANSACAO;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void d(String msg){
        Log.d(Constants.LOG,msg);
    }
}

class ComboTipoTransacaoListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
          EditarTransacao.setSELECTED_TIPO_TRANSACAO(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}
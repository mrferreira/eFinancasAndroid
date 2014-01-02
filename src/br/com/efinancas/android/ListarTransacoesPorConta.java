package br.com.efinancas.android;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import br.com.efinancas.android.dao.Transacao;
import br.com.efinancas.android.dao.TransacaoDataSource;

import java.util.List;

/**
 * @author: Misael Ferreira
 * Date: 15/12/13
 * Time: 11:58
 */
public class ListarTransacoesPorConta extends ListActivity {

    TransacaoDataSource transacaoDataSource;
    List<Transacao> transacoes;
    Button btnNovaTransacao;
    Long _idConta;
    private Transacao transacaoSelecionada;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_transacoes_por_conta);

        d("\n\ninicializando ListarTransacoesPorConta");
        inicializarDatasource();
        inicializarTransacoes();

        inicializarCampos();
    }

    private void inicializarDatasource() {
        d("inicializando datasource");
        transacaoDataSource = new TransacaoDataSource(this);
        transacaoDataSource.open();
        d("datasource inicializado");
    }

    private void inicializarTransacoes() {
        d("carregando transações");
        transacoes = transacaoDataSource.listTransacaoByIdConta(getIdConta());
        d("transações carregadas.");
    }

    private void inicializarCampos() {
        d("inicializando campos");
        setListAdapter(new LazyAdapter(this, transacoes));
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                d("setando onItemClickListener para listView");
                Intent intent = new Intent(ListarTransacoesPorConta.this, EditarTransacao.class);
                int indexTransacao = Integer.parseInt(Long.toString(l));
                d("indice transacao selecionada na listView > " + indexTransacao);
                long idTransacao = transacoes.get(indexTransacao).getId();
                d("idTransacao selecionada > " + idTransacao);
                intent.putExtra(Constants.TRANSACAO_SELECIONADA, idTransacao);
                intent.putExtra(Constants.CONTA_SELECIONADA, getIdConta());
                startActivity(intent);
            }
        });
        registerForContextMenu(getListView());

        if(btnNovaTransacao == null){
            btnNovaTransacao = (Button)findViewById(R.id.add);

            btnNovaTransacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    d("configurando onclickListener para btnNovaTransacao");
                    Intent intent = new Intent(ListarTransacoesPorConta.this, EditarTransacao.class);
                    intent.putExtra(Constants.CONTA_SELECIONADA, getIdConta());
                    startActivity(intent);
                }
            });
        }
        d("campos carregados");
    }

    private long getIdConta() {
        if(_idConta == null){
            Intent i = getIntent();
            _idConta = i.getLongExtra(Constants.CONTA_SELECIONADA,0);
        }
        d("idConta recuperado > " + _idConta);
        return _idConta;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==android.R.id.list) {
            d("criando contextMenu para a lista de transacoes");
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            transacaoSelecionada = transacoes.get(info.position);
            d("transacaoSelecionada :\n " + transacaoSelecionada.toString());
            menu.setHeaderTitle(transacaoSelecionada.getNome());
            String[] menuItems = getResources().getStringArray(R.array.contextMenu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
            d("contextMenu criado");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.contextMenu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = transacoes.get(info.position).getNome();
        switch(menuItemIndex){
            case 0:  // Editar transacao
                d("editando transacao");
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(ListarTransacoesPorConta.this,EditarTransacao.class);
                intent.putExtra(Constants.TRANSACAO_SELECIONADA, transacaoSelecionada.getId());
                startActivity(intent);
                break;
            case 1: // Excluir transacao
                d("excluindo transação");
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Excluir " + menuItemName)
                        .setMessage("Confirma exclusão da transação " + transacaoSelecionada.getNome() + "? ")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                d("excluindo transacao\n" + transacaoSelecionada.toString());
                                transacaoDataSource.deletetransacao(transacaoSelecionada);

                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setClass(ListarTransacoesPorConta.this, ListarTransacoesPorConta.class);
                                intent.putExtra(Constants.CONTA_SELECIONADA, transacaoSelecionada.getIdConta());
                                intent.putExtra(Constants.TRANSACAO_SELECIONADA, transacaoSelecionada.getId());
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Não",null)
                        .show();
                break;
        }

        return true;
    }

    private void d(String msg){
        Log.d(Constants.LOG,msg);
    }

}

package br.com.efinancas.android;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import br.com.efinancas.android.dao.Conta;
import br.com.efinancas.android.dao.ContaDataSource;

import java.util.List;

public class MyActivity extends ListActivity {

    private boolean doubleBackToExitPressedOnce = false;

    private ContaDataSource contaDataSource;

    List<Conta> contas;
    Conta contaSelecionada;
    Button btnNovaConta;
    TextView lblValorTotal;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        contaDataSource = new ContaDataSource(this);
        contaDataSource.open();

        contas = contaDataSource.listarTudo();

        ArrayAdapter<Conta> contaArrayAdapter = new ArrayAdapter<Conta>(this,
                android.R.layout.simple_list_item_1, contas);

        setListAdapter(contaArrayAdapter);

        btnNovaConta = (Button)findViewById(R.id.btnNovaConta);
        btnNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this,EditarConta.class);
                startActivity(intent);
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MyActivity.this, ListarTransacoesPorConta.class);
                intent.putExtra(Constants.CONTA_SELECIONADA, contas.get(Integer.valueOf(Long.toString(l))).getId());
                startActivity(intent);
            }
        });

        registerForContextMenu(getListView());
    }

    @Override
    protected void onResume() {
        contaDataSource.open();
        super.onResume();

        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    protected void onPause() {
        contaDataSource.close();
        super.onPause();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            contaSelecionada = contas.get(info.position);
            menu.setHeaderTitle(contaSelecionada.getNome());
            String[] menuItems = getResources().getStringArray(R.array.contextMenu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.contextMenu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = contas.get(info.position).getNome();
        Toast.makeText(MyActivity.this,String.format("Selected %s for item %s", menuItemName, listItemName),Toast.LENGTH_LONG).show();
        switch(menuItemIndex){
            case 0:  // Editar conta
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setClass(MyActivity.this,EditarConta.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.CONTA_SELECIONADA, contaSelecionada.getId());
                startActivity(intent);
                break;
            case 1: // Excluir conta
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Excluir " + menuItemName)
                        .setMessage("Confirma exclusão da conta " + contaSelecionada.getNome() + "? ")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                contaDataSource.excluirConta(contaSelecionada);

                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setClass(MyActivity.this, MyActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Não",null)
                        .show();
                break;
        }

        return true;
    }



    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione voltar novamente para sair", Toast.LENGTH_SHORT).show();
    }
}

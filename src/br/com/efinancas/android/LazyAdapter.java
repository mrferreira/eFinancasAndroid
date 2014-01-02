package br.com.efinancas.android;

/**
 * @author: Misael Ferreira
 * Date: 15/12/13
 * Time: 18:43
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.efinancas.android.dao.Transacao;

import java.text.SimpleDateFormat;
import java.util.List;

public class LazyAdapter extends BaseAdapter {

//    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_NOME = "nome";
    static final String KEY_DESCRICAO = "descricao";
    static final String KEY_DATA = "dados";
    //static final String KEY_THUMB_URL = "thumb_url";
    private Activity activity;
    private List<Transacao> dados;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;

    public LazyAdapter(Activity a, List<Transacao> d) {
        activity = a;
        dados = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //  imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return dados.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null) {
            vi = inflater.inflate(R.layout.transacao_list_row, null);
        }

        TextView nome = (TextView)vi.findViewById(R.id.nome_tlr); // title
        TextView descricao = (TextView)vi.findViewById(R.id.descricao_tlr); // artist name
        TextView data = (TextView)vi.findViewById(R.id.data_tlr); // duration
        TextView valor = (TextView)vi.findViewById(R.id.valor_tlr);
//        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image_tlr); // thumb image
        Transacao transacao = this.dados.get(position);
        nome.setText(transacao.getNome());
        descricao.setText(transacao.getDescricao());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        data.setText(sdf.format(transacao.getData() ));
        valor.setText(String.valueOf(transacao.getValor()));
//        imageLoader.DisplayImage(song.get(LazyAdapter.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}
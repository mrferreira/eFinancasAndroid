package br.com.efinancas.android.dao;

import br.com.efinancas.android.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Misael Ferreira
 * Date: 14/12/13
 * Time: 19:44
 */
public class Transacao {

    private long id;
    private String nome;
    private String descricao;
    private Date data;
    private long idConta;
    private int tipo; // 0 entrada | 1 saída
    private long idCategoria;
    private long idTag;
    private double valor;

    public Transacao(){
        id = 0;
        nome = "sem nome";
        descricao = "";
        data = new Date();
        idConta = 0;
        tipo = Constants.SAIDA; //default saída
        idCategoria = 0;
        idTag = 0;
        valor = 0.0f;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public long getIdConta() {
        return idConta;
    }

    public void setIdConta(long idConta) {
        this.idConta = idConta;
    }

    public long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public long getIdTag() {
        return idTag;
    }

    public void setIdTag(long idTag) {
        this.idTag = idTag;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int _tipo){
        tipo = _tipo;
    }

    @Override
    public String toString(){
        StringBuilder result = new
                StringBuilder("id > ").append(id).append("\n")
                .append("nome > ").append(nome).append("\n")
                .append("desc > ").append(descricao).append("\n")
                .append("data > ").append(new SimpleDateFormat("dd/MM/yyyy").format(data)).append("\n")
                .append("conta > ").append(idConta).append("\n")
                .append("tipo > ").append(tipo).append("\n")
                .append("categ > ").append(idCategoria).append("\n")
                .append("id tag > ").append(idTag).append("\n")
                .append("valor > ").append(valor);
        return result.toString();
    }
}

package br.com.efinancas.android.dao;

/**
 * @author: Misael Ferreira
 * @since 14/12/13 17:41
 * @version 1.0
 */
public class Conta {
    private long id;
    private String nome;
    private String descricao;
    private double valorInicial;
    private double total;

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

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(double valorInicial) {
        this.valorInicial = valorInicial;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString(){
        return nome;
    }
}

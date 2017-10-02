package fatec.rbr.serragensis.model;

/**
 * Created by VGs on 02/10/2017.
 */

public class Servico {
    private int id;
    private Cliente Cliente_Orcamento = null;
    private int valor;

    public Servico(){}

    public Servico(String nomeCliente, String enderecoCliente,int valor) {
        super();
        this.Cliente_Orcamento.setNome(nomeCliente);
        this.Cliente_Orcamento.setEndereco(enderecoCliente);
        this.valor = valor;
    }

    public String getNomeCliente() {
        return Cliente_Orcamento.getNome();
    }

    public void setNomeCliente(String nomeCliente) {
        this.Cliente_Orcamento.setNome(nomeCliente);
    }

    public String getEnderecoCliente() {
        return Cliente.getEnderecoEntrega();
    }

    public void setEnderecoCliente(String enderecoCliente) {
        this.Cliente_Orcamento.setEnderecoEntrega(enderecoCliente);
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

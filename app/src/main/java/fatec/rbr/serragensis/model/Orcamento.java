package fatec.rbr.serragensis.model;

public class Orcamento {

    private int id;
    private String nomeCliente;
    private String enderecoCliente;
    private int valor;

    public Orcamento(){}

    public Orcamento(String nomeCliente, String enderecoCliente,int valor) {
        super();
        this.nomeCliente = nomeCliente;
        this.enderecoCliente = enderecoCliente;
        this.valor = valor;
    }


    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(String enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
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

    @Override
    public String toString() {
        return "Nome: " + getNomeCliente() + " Endereço: " +
                getEnderecoCliente() + " Valor: " + getValor();
    }
}


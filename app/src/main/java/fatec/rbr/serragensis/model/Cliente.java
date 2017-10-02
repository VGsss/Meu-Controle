package fatec.rbr.serragensis.model;
public class Cliente {

    private int id;
    private String nome;
    private String endereco;
    private static String enderecoEntrega;
    private int telefone;
    private int telefoneLocalEntrega;
    private String nomeContatoLocalEntrega;

    public Cliente(int id, String nome, String endereco, int telefone, int telefoneLocalEntrega, String nomeContatoLocalEntrega) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.telefoneLocalEntrega = telefoneLocalEntrega;
        this.nomeContatoLocalEntrega = nomeContatoLocalEntrega;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public static String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public int getTelefoneLocalEntrega() {
        return telefoneLocalEntrega;
    }

    public void setTelefoneLocalEntrega(int telefoneLocalEntrega) {
        this.telefoneLocalEntrega = telefoneLocalEntrega;
    }

    public String getNomeContatoLocalEntrega() {
        return nomeContatoLocalEntrega;
    }

    public void setNomeContatoLocalEntrega(String nomeContatoLocalEntrega) {
        this.nomeContatoLocalEntrega = nomeContatoLocalEntrega;
    }
}


package fatec.rbr.serragensis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import fatec.rbr.serragensis.model.MySQLiteHelper;
import fatec.rbr.serragensis.model.Orcamento;


public class NovoOrcamento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_orcamento);
    }
    public void SalvarOrcamento(View v){
        TextView nome = (TextView)findViewById(R.id.txt_NomeCliOrcamento);
        TextView endereco = (TextView)findViewById(R.id.txt_EndeCliOrcamento);
        TextView valor = (TextView)findViewById(R.id.txt_ValorOrcamento);
        db.addOrcamento(new Orcamento(nome.getText().toString(),endereco.getText().toString(),Integer.valueOf(valor.getText().toString())));
        finish();
    }
    MySQLiteHelper db = new MySQLiteHelper(this);
}
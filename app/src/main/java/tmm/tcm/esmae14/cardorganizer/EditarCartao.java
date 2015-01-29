package tmm.tcm.esmae14.cardorganizer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;


public class EditarCartao extends ActionBarActivity {
    EditText txtNomeEdit, txtNumeroEdit ;
    Database db;
    String format1;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_cartao);

        db= new Database(this);
        final Cartao cartao=new Cartao();
        txtNomeEdit=(EditText)findViewById(R.id.txtNomeEdit);
        txtNumeroEdit=(EditText)findViewById(R.id.txtNumeroEdit);

        int id=getIntent().getExtras().getInt("id");
        String format=getIntent().getExtras().getString("format");

        cartao.setNomeCartao(getIntent().getExtras().getString("nome"));
        final String nomeInicial = getIntent().getExtras().getString("nome");
        cartao.setId(id);
        cartao.setNumero(getIntent().getExtras().getString("numero"));
        cartao.setFormato(format);
        //cartao.setTipo("");

        txtNomeEdit.setText(getIntent().getExtras().getString("nome"));
        txtNumeroEdit.setText(getIntent().getExtras().getString("numero"));

        //Toast.makeText(getApplicationContext(),db.getCardCount(),Toast.LENGTH_LONG).show();

        final Button btnLeitorEdit= (Button) findViewById(R.id.btnLeitorEdit);
        final Button btnVoltar= (Button) findViewById(R.id.btn_voltar);
        final Button btnEditar= (Button) findViewById(R.id.btn_edit);
        btnLeitorEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(getApplicationContext(), "Aguarde um momento... ", Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent("com.google.zxing.client.android.SCAN");
                    startActivityForResult(intent1, 0);
                } catch(Exception e){
                    if(e.toString().contains("com.google.zxing.client.android.SCAN")){
                        Toast.makeText(getApplicationContext(), "É necessária a Aplicação Barcode Scanner", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
                        startActivity(intent1);
                    }
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditarCartao.this,MainActivity.class));
                finish();

            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag==1){
                    cartao.setNomeCartao(txtNomeEdit.getText().toString());
                    cartao.setNumero(txtNumeroEdit.getText().toString());
                    cartao.setFormato(format1);
                }else{
                    cartao.setNomeCartao(txtNomeEdit.getText().toString());
                    cartao.setNumero(txtNumeroEdit.getText().toString());
                    cartao.setFormato("EAN_13");
                }

                if (!cartaoExists(cartao) || nomeInicial.equalsIgnoreCase(txtNomeEdit.getText().toString())) {
                    db.updateCartao(cartao);
                    startActivity(new Intent(EditarCartao.this,MainActivity.class));
                    finish();
                }else{Toast.makeText(getApplicationContext(),"O cartão "+ String.valueOf(txtNomeEdit.getText())+" já existe!"+ nomeInicial+ " "+ txtNomeEdit.getText().toString(),Toast.LENGTH_LONG).show();}

            }
        });

        txtNomeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                btnEditar.setEnabled((String.valueOf(txtNomeEdit.getText()).trim().length() * String.valueOf(txtNumeroEdit.getText()).trim().length()) > 0);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtNumeroEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                btnEditar.setEnabled((String.valueOf(txtNomeEdit.getText()).trim().length() * String.valueOf(txtNumeroEdit.getText()).trim().length()) > 0);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                format1 = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Adiciona o numero à caixa de texto
                EditText txtNumeroEdit = (EditText) findViewById(R.id.txtNumeroEdit);
                flag=1;
                txtNumeroEdit.setText(contents);
                txtNumeroEdit.setFocusable(false);
                txtNumeroEdit.setFocusableInTouchMode(false);

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    private boolean cartaoExists(Cartao cartao) {
        String name = cartao.getNomeCartao();
        int cardCount = db.getCardCount();
        List<Cartao> cartoes=db.getAllCards();

        for (int i = 0; i < cardCount; i++) {
            if (name.compareToIgnoreCase(cartoes.get(i).getNomeCartao()) == 0)
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}

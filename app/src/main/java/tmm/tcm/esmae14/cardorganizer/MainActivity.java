package tmm.tcm.esmae14.cardorganizer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    String format;
    int flag=0;
    ListView cardListView;
    List<Cartao> cartoes = new ArrayList<>();
    Database db;
    EditText txtNomeCartao, txtNumeroCartao ;
    TextView noCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /////////////////////////Nao me toca/////////////////////////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ///////////////////////////////////////////////////////////

        txtNomeCartao = (EditText) findViewById(R.id.txtNomeCartao);
        txtNumeroCartao = (EditText) findViewById(R.id.txtNumeroCartao);
        noCard=(TextView) findViewById(R.id.noCard);
        cardListView = (ListView) findViewById(R.id.listView);
        db= new Database(getApplicationContext());

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Lista");
        tabSpec.setContent(R.id.tabList);
        tabSpec.setIndicator("Lista");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Criar");
        tabSpec.setContent(R.id.tabAdd);
        tabSpec.setIndicator("Criar");
        tabHost.addTab(tabSpec);


        final Button btn_add=(Button) findViewById(R.id.btn_add);
        btn_add.setEnabled(false);
        final Button mscan=(Button) findViewById(R.id.btn_Leitor);

        mscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(getApplicationContext(), "Aguarde um momento... ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    startActivityForResult(intent, 0);
                } catch(Exception e){
                    if(e.toString().contains("com.google.zxing.client.android.SCAN")){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
                        startActivity(intent);
                    }
                }
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cartao cartao;
                /*int count=db.getCardCount();
                Toast.makeText(getApplicationContext(), String.valueOf(txtNomeCartao.getText())+".........."+count, Toast.LENGTH_SHORT).show();*/
                if(flag==1){
                    cartao = new Cartao(db.getCardCount(), String.valueOf(txtNomeCartao.getText()), String.valueOf(txtNumeroCartao.getText()), format);

                }else{
                    cartao = new Cartao(db.getCardCount(), String.valueOf(txtNomeCartao.getText()), String.valueOf(txtNumeroCartao.getText()), "EAN_13");
                }

                if (!cartaoExists(cartao)) {
                    db.createCartao(cartao);
                    cartoes.add(cartao);
                    noCard.setText("");
                    Toast.makeText(getApplicationContext(), String.valueOf(txtNomeCartao.getText())+ " foi adicionado à sua lista de cartões!", Toast.LENGTH_SHORT).show();
                    txtNomeCartao.setText("");
                    txtNumeroCartao.setText("");
                    txtNumeroCartao.setFocusable(true);
                    txtNumeroCartao.setFocusableInTouchMode(true );
                }else{Toast.makeText(getApplicationContext(),"O cartão "+ String.valueOf(txtNomeCartao.getText())+" já existe!",Toast.LENGTH_LONG).show();}

            }
        });
        txtNomeCartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    btn_add.setEnabled((String.valueOf(txtNomeCartao.getText()).trim().length() * String.valueOf(txtNumeroCartao.getText()).trim().length()) > 0);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtNumeroCartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                btn_add.setEnabled((String.valueOf(txtNomeCartao.getText()).trim().length() * String.valueOf(txtNumeroCartao.getText()).trim().length()) > 0);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        if (db.getCardCount() != 0) {
            noCard.setText("");
            cartoes.addAll(db.getAllCards());
        }
        preencherLista();


    }




    private boolean cartaoExists(Cartao cartao) {
        String name = cartao.getNomeCartao();
        int cardCount = cartoes.size();

        for (int i = 0; i < cardCount; i++) {
            if (name.compareToIgnoreCase(cartoes.get(i).getNomeCartao()) == 0)
                return true;
        }
        return false;
    }


    private void preencherLista() {
        ArrayAdapter<Cartao> adapter = new CardListAdapter();
        cardListView.setAdapter(adapter);
    }



    private class CardListAdapter extends ArrayAdapter<Cartao> {
        public CardListAdapter() {
            super (MainActivity.this, R.layout.layout_lista, cartoes);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.layout_lista, parent, false);

            Cartao currentCartao = cartoes.get(position);

            TextView nome = (TextView) view.findViewById(R.id.cardName);
            nome.setText(currentCartao.getNomeCartao());
            TextView numero = (TextView) view.findViewById(R.id.barNumber);
            numero.setText(currentCartao.getNumero());

            return view;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    // Adiciona o numero à caixa de texto
                    EditText txtNumeroCartao = (EditText) findViewById(R.id.txtNumeroCartao);
                    if(contents.matches("[0-9]+")){
                        flag=1;
                        txtNumeroCartao.setText(contents);
                        txtNumeroCartao.setFocusable(false);
                        txtNumeroCartao.setFocusableInTouchMode(false);
                    } else {
                        Toast.makeText(getApplicationContext(), "Código Inválido", Toast.LENGTH_SHORT).show();
                    }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

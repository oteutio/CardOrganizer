package tmm.tcm.esmae14.cardorganizer;

import android.content.Intent;
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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    List<Cartao> cartoes = new ArrayList<Cartao>();
    Database db;
    EditText txtNomeCartao = (EditText) findViewById(R.id.txtNomeCartao);
    EditText txtNumeroCartao = (EditText) findViewById(R.id.txtNumeroCartao);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

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
        final Button mscan=(Button) findViewById(R.id.btn_Leitor);

        mscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), String.valueOf(txtNomeCartao.getText()) + "Espere um pouco, carregando leitor...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(intent, 0);

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cartao cartao = new Cartao(db.getCardCount(), String.valueOf(txtNomeCartao.getText()), String.valueOf(txtNumeroCartao.getText()), null);
                if (!cartaoExists(cartao)) {
                    db.createCartao(cartao);
                    cartoes.add(cartao);
                    Toast.makeText(getApplicationContext(), String.valueOf(txtNomeCartao.getText())+ " has been added to your Contacts!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        txtNomeCartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                btn_add.setEnabled(String.valueOf(txtNomeCartao.getText()).trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

    private boolean cartaoExists(Cartao cartao) {
        String name = cartao.getNomeCartao();
        int cardCount = cartoes.size();

        for (int i = 0; i < cardCount; i++) {
            if (name.compareToIgnoreCase(cartoes.get(i).getNomeCartao()) == 0)
                return true;
        }
        return false;
    }

    /*private void populateList() {
        ArrayAdapter<Cartao> adapter = new CardListAdapter();
        CardListView.setAdapter(adapter);
    }*/

    /*private class CardListAdapter extends ArrayAdapter<Cartao> {
        public CardListAdapter() {
            super (MainActivity.this, R.layout.listview_item, Contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Cartao currentContact = cartoes.get(position);

            TextView name = (TextView) view.findViewById(R.id.contactName);
            name.setText(currentContact.getName());
            TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContact.getPhone());
            TextView email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContact.getEmail());
            TextView address = (TextView) view.findViewById(R.id.cAddress);
            address.setText(currentContact.getAddress());
            ImageView ivContactImage = (ImageView) view.findViewById(R.id.ivContactImage);
            ivContactImage.setImageURI(currentContact.getImageURI());

            return view;
        }
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    // Adiciona o numero à caixa de texto
                    EditText txtNumeroCartao = (EditText) findViewById(R.id.txtNumeroCartao);
                    if(contents.matches("[0-9]+")){
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
}

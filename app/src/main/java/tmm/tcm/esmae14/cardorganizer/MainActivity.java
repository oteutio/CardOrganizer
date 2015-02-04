package tmm.tcm.esmae14.cardorganizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final int ABRIR = 0, DELETE = 1;

    String format;
    Intent startbuttonintent;
    int flag=0;
    ListView cardListView;
    List<Cartao> cartoes = new ArrayList<>();
    Database db;
    EditText txtNomeCartao, txtNumeroCartao,txtNomeEdit, txtNumeroEdit ;
    TextView noCard;
    int longClickedItemIndex;
    ArrayAdapter<Cartao> cartaoAdapter;
//12122
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

        registerForContextMenu(cardListView);

        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gerarBarcode(position);
            }
        });

        cardListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });
////////////////////////Tab Host////////////////////////////////////////////
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Lista");
        tabSpec.setContent(R.id.tabList);
        tabSpec.setIndicator("Lista");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Adicionar");
        tabSpec.setContent(R.id.tabAdd);
        tabSpec.setIndicator("Adicionar");
        tabHost.addTab(tabSpec);
//////////////////////////////////////////////////////////////////////////////////

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
                        Toast.makeText(getApplicationContext(), "É necessária a Aplicação Barcode Scanner", Toast.LENGTH_SHORT).show();
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
                    cartao = new Cartao(db.getCardCount(), String.valueOf(txtNomeCartao.getText()), String.valueOf(txtNumeroCartao.getText()), format, "");
                }else{
                    cartao = new Cartao(db.getCardCount(), String.valueOf(txtNomeCartao.getText()), String.valueOf(txtNumeroCartao.getText()), "EAN_13", "");
                }

                if (!cartaoExists(cartao)) {

                    db.createCartao(cartao);
                   // cartoes.add(cartao);
                    cartaoAdapter.notifyDataSetChanged();
                    noCard.setText("");
                    Toast.makeText(getApplicationContext(), String.valueOf(txtNomeCartao.getText())+ " foi adicionado à sua lista de cartões!", Toast.LENGTH_SHORT).show();
                    txtNomeCartao.setText("");
                    txtNumeroCartao.setText("");
                    txtNumeroCartao.setFocusable(true);
                    txtNumeroCartao.setFocusableInTouchMode(true);
                    hideKeyboard();


                       // noCard.setText("");
                    cartoes.clear();
                    cartoes.addAll(db.getAllCards());

                    preencherLista();


                }else{Toast.makeText(getApplicationContext(),"O cartão "+ String.valueOf(txtNomeCartao.getText())+" já existe!",Toast.LENGTH_LONG).show();}


            }
        });
        txtNomeCartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                if((String.valueOf(txtNomeCartao.getText()).trim().length() * String.valueOf(txtNumeroCartao.getText()).trim().length()) > 0){
                    btn_add.setEnabled(true);
                    btn_add.setBackgroundColor(Color.parseColor("#3f51b5"));

                }else{

                    btn_add.setEnabled(false);
                    btn_add.setBackgroundColor(Color.LTGRAY);

                }

                //btn_add.setEnabled((String.valueOf(txtNomeCartao.getText()).trim().length() * String.valueOf(txtNumeroCartao.getText()).trim().length()) > 0);

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

                if((String.valueOf(txtNomeCartao.getText()).trim().length() * String.valueOf(txtNumeroCartao.getText()).trim().length()) > 0){
                    btn_add.setEnabled(true);
                    btn_add.setBackgroundColor(Color.parseColor("#3f51b5"));

                }else{

                    btn_add.setEnabled(false);
                    btn_add.setBackgroundColor(Color.LTGRAY);

                }

                //btn_add.setEnabled((String.valueOf(txtNomeCartao.getText()).trim().length() * String.valueOf(txtNumeroCartao.getText()).trim().length()) > 0);

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

    private void gerarBarcode(int position) {
        try{
            Toast.makeText(getApplicationContext(), "A gerar o Barcode... ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("com.google.zxing.client.android.ENCODE");
            intent.putExtra("ENCODE_TYPE", "TEXT_TYPE");
            intent.putExtra("ENCODE_FORMAT", cartoes.get(position).getFormato());
            intent.putExtra("ENCODE_DATA", cartoes.get(position).getNumero());
            startActivityForResult(intent,0);
        } catch(Exception e){
            if(e.toString().contains("com.google.zxing.client.android.ENCODE")){
                Toast.makeText(getApplicationContext(), "É necessária a Aplicação Barcode Scanner", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
                startActivity(intent);
            }
        }
    }


    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderIcon(R.drawable.ic_pencil);
        menu.setHeaderTitle("Opções de Cartão");
        menu.add(Menu.NONE, ABRIR, Menu.NONE, "Editar Cartão");
        menu.add(Menu.NONE, DELETE, Menu.NONE, "Apagar Cartão");
    }



    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case ABRIR:

                Cartao cartao1=cartoes.get(longClickedItemIndex);
                //Toast.makeText(getApplicationContext(),cartao1.toString(),Toast.LENGTH_LONG);
                startbuttonintent = new Intent(MainActivity.this, EditarCartao.class);

                startbuttonintent.putExtra("nome",cartao1.getNomeCartao());
                startbuttonintent.putExtra("numero",cartao1.getNumero());
                startbuttonintent.putExtra("id",cartao1.getId());
                startbuttonintent.putExtra("format",cartao1.getFormato());
                startActivity(startbuttonintent);
                finish();

                    //editarCartao(cartao1.getNomeCartao(),cartao1.getNumero(),cartao1.getFormato(), cartao1.getId());
                break;
            case DELETE:
                db.deleteCard(cartoes.get(longClickedItemIndex));
                //Toast.makeText(getApplicationContext(), cartoes.get(longClickedItemIndex).toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), db.getCardCount(), Toast.LENGTH_SHORT).show();

                cartoes.remove(longClickedItemIndex);
                cartaoAdapter.notifyDataSetChanged();
                if(db.getCardCount()==0) {
                    noCard.setText("Ainda não adicionou nenhum cartão!");
                }

                cartaoAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
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
        cartaoAdapter = new CardListAdapter();
        cardListView.setAdapter(cartaoAdapter);
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
                    flag=1;
                    txtNumeroCartao.setText(contents);
                    txtNumeroCartao.setFocusable(false);
                    txtNumeroCartao.setFocusableInTouchMode(false);

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
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Créditos");
            builder.setMessage("Miguel Carneiro - 4140026\nNuno Castilho - 4140221\nPedro Correia - 4140032\nUnidade Curricular TMM");
            builder.setPositiveButton("FECHAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // You don't have to do anything here if you just want it dismissed when clicked
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

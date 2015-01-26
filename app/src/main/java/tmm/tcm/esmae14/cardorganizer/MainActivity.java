package tmm.tcm.esmae14.cardorganizer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

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

        final EditText txtNomeCartao = (EditText) findViewById(R.id.txtNomeCartao);
        EditText txtNumeroCartao = (EditText) findViewById(R.id.txtNumeroCartao);
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
                //Contact contact = new Contact(dbHandler.getContactsCount(), String.valueOf(nameTxt.getText()), String.valueOf(phoneTxt.getText()), String.valueOf(emailTxt.getText()), String.valueOf(addressTxt.getText()), imageUri);
               // if (!contactExists(contact)) {
                  //  dbHandler.createContact(contact);
                  //  Contacts.add(contact);
                    Toast.makeText(getApplicationContext(), String.valueOf(txtNomeCartao.getText())+ " has been added to your Contacts!", Toast.LENGTH_SHORT).show();
                    return;
                }

            //}
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
}

package tmm.tcm.esmae14.cardorganizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "cardManager",
    TABLE_CARTOES = "cartoes",
    KEY_ID = "id",
    KEY_NOME = "nome",
    KEY_NUMERO = "numero",
    KEY_FORMATO = "formato",
    KEY_TIPO = "tipo";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CARTOES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NOME + " TEXT," + KEY_NUMERO + " TEXT," + KEY_FORMATO + " TEXT," + KEY_TIPO + " TEXT)" /*+ KEY_CATEGORIAS + " TEXT"*/);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTOES);

        onCreate(db);
    }

    public void createCartao(Cartao cartao) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NOME, cartao.getNomeCartao());
        values.put(KEY_NUMERO, cartao.getNumero());
        values.put(KEY_FORMATO, cartao.getFormato());
        values.put(KEY_TIPO, cartao.getTipo());
        //values.put(KEY_CATEGORIAS, cartao.getCategorias());


        db.insert(TABLE_CARTOES, null, values);
        db.close();
    }

    public Cartao getCartao(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_CARTOES, new String[] { KEY_ID, KEY_NOME, KEY_NUMERO, KEY_FORMATO, KEY_TIPO/*, KEY_CATEGORIAS*/ }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        Cartao cartao = new Cartao(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), "");
        db.close();
        cursor.close();
        return cartao;
    }

    public void deleteCard(Cartao cartao) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CARTOES, KEY_ID + "=?", new String[] { String.valueOf(cartao.getId()) });
        db.close();
    }

    public int getCardCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CARTOES, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    public int updateCartao(Cartao cartao) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NOME, cartao.getNomeCartao());
        values.put(KEY_NUMERO, cartao.getNumero());
        values.put(KEY_FORMATO, cartao.getFormato());
        values.put(KEY_TIPO, cartao.getTipo());
        //values.put(KEY_CATEGORIAS, cartao.getCategorias());

        int rowsAffected = db.update(TABLE_CARTOES, values, KEY_ID + "=?", new String[] { String.valueOf(cartao.getId()) });
        db.close();

        return rowsAffected;
    }

    public List<Cartao> getAllCards() {
        List<Cartao> cards = new ArrayList<Cartao>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CARTOES, null);

        if (cursor.moveToFirst()) {
            do {
                cards.add(new Cartao(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), ""));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cards;
    }
}

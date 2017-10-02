package fatec.rbr.serragensis.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;


public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "MySQLiteHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "orcamentoDB";

    // orcamentos table name
    private static final String TABLE_ORCAMENTOS = "orcamentos";

    // orcamentos Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NOMECLIENTE = "nome";
    private static final String KEY_ENDERECOCLIENTE = "endereco";

    private static final String[] COLUMNS = {KEY_ID, KEY_NOMECLIENTE, KEY_ENDERECOCLIENTE};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create orcamento table
        String CREATE_orcamento_TABLE = "CREATE TABLE orcamentos ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "author TEXT )";
        // create orcamentos table
        db.execSQL(CREATE_orcamento_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older orcamentos table if existed
        db.execSQL("DROP TABLE IF EXISTS orcamentos");

        // create fresh orcamentos table
        this.onCreate(db);
    }

    public void addOrcamento(Orcamento orcamento){
        Log.d("addorcamento", orcamento.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NOMECLIENTE, orcamento.getNomeCliente());
        values.put(KEY_ENDERECOCLIENTE, orcamento.getEnderecoCliente());

        // 3. insert
        db.insert(TABLE_ORCAMENTOS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Orcamento getOrcamento(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_ORCAMENTOS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build orcamento object
        Orcamento orcamento = new Orcamento();
        orcamento.setId(Integer.parseInt(cursor.getString(0)));
        orcamento.setNomeCliente(cursor.getString(1));
        orcamento.setEnderecoCliente(cursor.getString(2));

        Log.d("getorcamento("+id+")", orcamento.toString());

        // 5. return orcamento
        return orcamento;
    }

    // Get All orcamentos
    public List<Orcamento> getAllOrcamentos() {
        List<Orcamento> orcamentos = new LinkedList<Orcamento>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ORCAMENTOS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build orcamento and add it to list
        Orcamento orcamento = null;
        if (cursor.moveToFirst()) {
            do {
                orcamento = new Orcamento();
                orcamento.setId(Integer.parseInt(cursor.getString(0)));
                orcamento.setNomeCliente(cursor.getString(1));
                orcamento.setEnderecoCliente(cursor.getString(2));

                // Add orcamento to orcamentos
                orcamentos.add(orcamento);
            } while (cursor.moveToNext());
        }

        Log.d("getAllorcamentos()", orcamentos.toString());

        // return orcamentos
        return orcamentos;
    }

    // Updating single orcamento
    public int updateOrcamento(Orcamento orcamento) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", orcamento.getNomeCliente()); // get title
        values.put("author", orcamento.getEnderecoCliente()); // get author

        // 3. updating row
        int i = db.update(TABLE_ORCAMENTOS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(orcamento.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single orcamento
    public void deleteOrcamento(Orcamento orcamento) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ORCAMENTOS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(orcamento.getId()) });

        // 3. close
        db.close();

        Log.d("deleteorcamento", orcamento.toString());
    }


}

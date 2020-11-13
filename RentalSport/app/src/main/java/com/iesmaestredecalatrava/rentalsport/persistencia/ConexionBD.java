package com.iesmaestredecalatrava.rentalsport.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class ConexionBD extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.iesmaestredecalatrava.rentalsport/databases/";
    private static String DB_NAME = "rentalsport";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public ConexionBD(Context context) {
        super(context, DB_NAME, null, 5);
        this.myContext = context;

        String myPath = DB_PATH + DB_NAME; // also check the extension of you db file
        File dbfile = new File(myPath);
        if (dbfile.exists()) {

        } else {
            try {
                createDataBase();
                Toast.makeText(context,"Crreando",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public ConexionBD(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory,
                      int version) {

        super(contexto, nombre, factory, version);
        this.myContext = contexto;

        try {

            String myPath = DB_PATH + DB_NAME; // also check the extension of you db file
            File dbfile = new File(myPath);
            if (dbfile.exists()) {

                Toast.makeText(myContext, "database exists file", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(myContext, "cant find database", Toast.LENGTH_LONG).show();
            }

        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }


    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // Si existe, no haemos nada!


        } else {
            // Llamando a este método se crea la base de datos vacía en la ruta
            // por defecto del sistema de nuestra aplicación por lo que
            // podremos sobreescribirla con nuestra base de datos.

            this.getReadableDatabase();

            Toast.makeText(myContext,"Creando",Toast.LENGTH_SHORT).show();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copiando database");
            }
        }
    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            // Base de datos no creada todavia
        }

        if (checkDB != null) {

            checkDB.close();
        }

        return checkDB != null ? true : false;

       /* File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();*/
    }

    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open("cache.sqlite");

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws IOException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


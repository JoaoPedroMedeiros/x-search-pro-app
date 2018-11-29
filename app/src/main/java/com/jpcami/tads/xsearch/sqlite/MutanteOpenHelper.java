package com.jpcami.tads.xsearch.sqlite;

import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class MutanteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MutanteBD";
    private static final int DATABASE_VERSION = 4   ;

    private static final String CRIACAO_TABELA_MUTANTES =
            "CREATE TABLE MUTANTES (\n" +
            "  ID   INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
            "  NOME TEXT         NOT NULL UNIQUE\n" +
            ")";

    private static final String CRIACAO_TABELA_HABILIDADES =
            "CREATE TABLE HABILIDADES (\n" +
            "  ID            INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "  NOME          TEXT NOT NULL UNIQUE\n" +
            ")";

    private static final String CRIACAO_TABELA_HABILIDADES_MUTANTE =
            "CREATE TABLE HABILIDADES_MUTANTE (\n" +
            "  MUTANTE_ID    INTEGER,\n" +
            "  HABILIDADE_ID INTEGER,\n" +
            "  PRIMARY KEY (MUTANTE_ID, HABILIDADE_ID),\n" +
            "  FOREIGN KEY (MUTANTE_ID) REFERENCES MUTANTES (ID) \n" +
            "    ON DELETE CASCADE ON UPDATE NO ACTION,\n" +
            "  FOREIGN KEY (HABILIDADE_ID) REFERENCES HABILIDADES (ID) \n" +
            "    ON DELETE RESTRICT ON UPDATE NO ACTION\n" +
            ")";

    private List<String> CARGA_INICIAL_MUTANTES = Arrays.asList(
            "INSERT INTO MUTANTES (ID, NOME) VALUES (1, 'Gambit'    )",
            "INSERT INTO MUTANTES (ID, NOME) VALUES (2, 'Wolverine' )",
            "INSERT INTO MUTANTES (ID, NOME) VALUES (3, 'Noturno'   )",
            "INSERT INTO MUTANTES (ID, NOME) VALUES (4, 'Fenix'     )",
            "INSERT INTO MUTANTES (ID, NOME) VALUES (5, 'Vampira'   )",
            "INSERT INTO MUTANTES (ID, NOME) VALUES (6, 'Mercúrio'  )",
            "INSERT INTO MUTANTES (ID, NOME) VALUES (7, 'Fera'      )",
            "INSERT INTO MUTANTES (ID, NOME) VALUES (8, 'Mistica'   )",
            "INSERT INTO MUTANTES (ID, NOME) VALUES (9, 'Tempestade')",
            "INSERT INTO MUTANTES (ID, NOME) VALUES (10, 'Deadpool'  )"
    );

    private List<String> CARGA_INICIAL_HABILIDADES = Arrays.asList(
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (1, 'Velocidade Aprimorada')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (2, 'Regeneracao')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (3, 'Teletransporte')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (4, 'Telepatia')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (5, 'Absorver habilidades')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (6, 'Super Velocidade')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (7, 'Fisico Animal')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (8, 'Metamorfose')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (9, 'Controle Climatico')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (10, 'Forca Aprimorada')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (11, 'Garras retráteis')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (12, 'Visao Noturna')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (13, 'Voar')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (14, 'Absorver Memorias')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (15, 'Metabolismo Aprimorado')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (16, 'Inteligencia Aprimorada')",
            "INSERT INTO HABILIDADES (ID, NOME) VALUES (17, 'Respirar na agua')"
    );

    private List<String> CARGA_INICIAL_HABILIDADES_MUTANTES = Arrays.asList(
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (1 , 1 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (1 , 2 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (2 , 2 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (2 , 11)",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (3 , 3 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (3 , 12)",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (4 , 4 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (4 , 13)",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (5 , 5 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (5,  14)",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (6,  6 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (6,  15)",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (7,  7 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (7,  2) ",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (8,  8 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (8,  2)",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (9,  9 )",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (9,  17)",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (10, 10)",
            "INSERT INTO HABILIDADES_MUTANTE (MUTANTE_ID, HABILIDADE_ID) VALUES (10, 2) "
    );

    public MutanteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS HABILIDADES");
        //db.execSQL("DROP TABLE IF EXISTS MUTANTES");
        //db.execSQL("DROP TABLE IF EXISTS HABILIDADES_MUTANTE");

        // Criação tabela Mutantes
        db.execSQL(CRIACAO_TABELA_MUTANTES);

        // Criação tabela de Habilidades
        db.execSQL(CRIACAO_TABELA_HABILIDADES);

        // Criação tabela de habilidades dos mutantes
        db.execSQL(CRIACAO_TABELA_HABILIDADES_MUTANTE);

        // Carga inicial dos mutantes
        for (String sql: CARGA_INICIAL_MUTANTES) {
            db.execSQL(sql);
        }

        // Carga inicial das habilidades
        for (String sql: CARGA_INICIAL_HABILIDADES) {
            db.execSQL(sql);
        }

        // Carga inicial das habilidades dos mutantes
        for (String sql: CARGA_INICIAL_HABILIDADES_MUTANTES) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS HABILIDADES");
        db.execSQL("DROP TABLE IF EXISTS MUTANTES");
        db.execSQL("DROP TABLE IF EXISTS HABILIDADES_MUTANTE");

        onCreate(db);
    }
}

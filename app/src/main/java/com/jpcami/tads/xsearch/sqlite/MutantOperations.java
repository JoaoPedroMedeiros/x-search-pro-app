package com.jpcami.tads.xsearch.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jpcami.tads.xsearch.entity.Mutant;
import com.jpcami.tads.xsearch.entity.Skill;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MutantOperations {

    private MutanteOpenHelper bdHelper;

    private static final String MUTANTES = "MUTANTES";

    private static final String HABILIDADES = "HABILIDADES";

    private static final String HABILIDADES_MUTANTES = "HABILIDADES_MUTANTE";

    private static final String ID = "ID";

    private static final String NOME = "NOME";

    public MutantOperations(Context context) {
        bdHelper = new MutanteOpenHelper(context);
    }

    public boolean existsMutant(String name) {
        SQLiteDatabase db = bdHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(MUTANTES, new String[]{NOME}, "NOME = ?", new String[]{name}, null, null, null, null);
            boolean b = cursor.moveToFirst();
            cursor.close();
            return b;
        }
        finally {
            if (db.isOpen())
                db.close();
        }
    }

    public boolean existsMutantWithDifferentId(String name, Integer id) {
        SQLiteDatabase db = bdHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(MUTANTES, new String[]{NOME}, "NOME = ? AND ID <> ?", new String[]{name, id.toString()}, null, null, null, null);
            boolean b = cursor.moveToFirst();
            cursor.close();
            return b;
        }
        finally {
            if (db.isOpen())
                db.close();
        }
    }

    public Skill createSkill(String name) {
        SQLiteDatabase db = bdHelper.getReadableDatabase();
        try {
            Skill skill = new Skill();

            ContentValues contentValues = new ContentValues();
            contentValues.put(NOME, name);

            Long id = db.insertOrThrow(HABILIDADES, null, contentValues);

            skill.setId(id.intValue());
            skill.setName(name);
            return skill;
        }
        finally {
            if (db.isOpen())
                db.close();
        }
    }

    public List<Skill> getAllSkills() {
        SQLiteDatabase db = bdHelper.getReadableDatabase();
        try {
            List<Skill> skills = new ArrayList<>();

            Cursor cursor = db.query(HABILIDADES, new String[]{ID, NOME}, null, null, null, null, NOME);
            while (cursor.moveToNext()) {
                Skill skill = new Skill();
                skill.setId(cursor.getInt(0));
                skill.setName(cursor.getString(1));
                skills.add(skill);
            }

            return skills;
        }
        finally {
            if (db.isOpen())
                db.close();
        }
    }

    public List<Mutant> getAllMutants() {
        SQLiteDatabase db = bdHelper.getReadableDatabase();
        try {
            List<Mutant> mutants = new ArrayList<>();
            List<Skill> skills = new ArrayList<>();

            Cursor cursor = db.query(MUTANTES, new String[]{ID, NOME}, null, null, null, null, NOME);
            while (cursor.moveToNext()) {
                Mutant mutant = new Mutant();
                mutant.setId(cursor.getInt(0));
                mutant.setName(cursor.getString(1));
                mutant.setSkills(new ArrayList<Skill>());
                mutants.add(mutant);
            }

            Cursor cursorHab = db.query(HABILIDADES, new String[]{ID, NOME}, null, null, null, null, null);
            while (cursorHab.moveToNext()) {
                Skill skill = new Skill();
                skill.setId(cursorHab.getInt(0));
                skill.setName(cursorHab.getString(1));
                skills.add(skill);
            }

            Cursor cursorHabMut = db.query(HABILIDADES_MUTANTES, new String[]{"MUTANTE_ID", "HABILIDADE_ID"}, null, null, null, null, null);
            while (cursorHabMut.moveToNext()) {
                Mutant mutantFound = null;
                for (Mutant mutant : mutants) {
                    if (mutant.getId().equals(cursorHabMut.getInt(0))) {
                        mutantFound = mutant;
                        break;
                    }
                }

                Skill skillFound = null;
                for (Skill skill : skills) {
                    if (skill.getId().equals(cursorHabMut.getInt(1))) {
                        skillFound = skill;
                        break;
                    }
                }

                mutantFound.getSkills().add(skillFound);
            }

            return mutants;
        }
        finally {
            if (db.isOpen())
                db.close();
        }
    }

    public Mutant updateMutant(Mutant mutant) {
        SQLiteDatabase db = bdHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(NOME, mutant.getName());

            db.update(MUTANTES, values, "ID = ?", new String[] {mutant.getId().toString()});

            db.delete(HABILIDADES_MUTANTES, "MUTANTE_ID = ?", new String[] {mutant.getId().toString()});

            for (Skill skill : mutant.getSkills()) {
                ContentValues skillValues = new ContentValues();
                skillValues.put("MUTANTE_ID", mutant.getId());
                skillValues.put("HABILIDADE_ID", skill.getId());

                db.insert(HABILIDADES_MUTANTES, null, skillValues);
            }

            db.setTransactionSuccessful();

            return mutant;
        }
        finally {
            db.endTransaction();
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    public Mutant createMutant(String name, List<Skill> skills) {
        SQLiteDatabase db = bdHelper.getWritableDatabase();
        try {
            Mutant mutant = new Mutant();

            ContentValues contentValues = new ContentValues();
            contentValues.put(NOME, name);

            db.beginTransaction();

            Long id = db.insertOrThrow(MUTANTES, null, contentValues);
            System.out.println(id);
            mutant.setId(id.intValue());
            mutant.setName(name);

            for (Skill skill : skills) {
                ContentValues skillValues = new ContentValues();
                skillValues.put("MUTANTE_ID", mutant.getId());
                skillValues.put("HABILIDADE_ID", skill.getId());

                db.insert(HABILIDADES_MUTANTES, null, skillValues);
            }

            mutant.setSkills(skills);

            db.setTransactionSuccessful();
            return mutant;
        }
        finally {
            db.endTransaction();
            if (db.isOpen()) {
                db.close();
            }
        }


    }
}

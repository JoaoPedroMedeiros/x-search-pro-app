package com.jpcami.tads.xsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.jpcami.tads.xsearch.entity.Mutant;
import com.jpcami.tads.xsearch.entity.Skill;
import com.jpcami.tads.xsearch.service.ApplicationService;
import com.jpcami.tads.xsearch.sqlite.MutantOperations;
import com.jpcami.tads.xsearch.util.DefaultTask;
import com.jpcami.tads.xsearch.view.MutantSkillAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MutanteActivity extends AppCompatActivity {

    private ListView lvSkills;

    private EditText etName;

    private Mutant mutant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mutante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        etName = findViewById(R.id.etName);

        lvSkills = findViewById(R.id.lvSkills);
        final MutantSkillAdapter adapter = new MutantSkillAdapter(this);
        adapter.setOnSkillRemoved(new MutantSkillAdapter.OnSkillRemoved() {
            @Override
            public void onRemoved(View view, Skill skill) {
                adapter.remove(skill);
                Toast.makeText(MutanteActivity.this, "Removido com sucesso!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        lvSkills.setAdapter(adapter);

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("mutant") != null) {
            mutant = (Mutant) getIntent().getExtras().getSerializable("mutant");
            etName.setText(mutant.getName());

            ((ArrayAdapter) lvSkills.getAdapter()).addAll(mutant.getSkills());

        }
        else {
            mutant = null;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(MutanteActivity.this, "Por favor, digite o nome :)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lvSkills.getAdapter().getCount() == 0) {
                    Toast.makeText(MutanteActivity.this, "Mutante sem habilidade é humano :)", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Skill> skills = new ArrayList<>();

                for (int i=0;i < lvSkills.getAdapter().getCount();i++){
                    skills.add(((ArrayAdapter<Skill>) lvSkills.getAdapter()).getItem(i));
                }

                MutantOperations mutantOperations = new MutantOperations(MutanteActivity.this);

                if (mutant == null) {
                    Mutant tempmutant = new Mutant();
                    tempmutant.setName(etName.getText().toString());
                    tempmutant.setSkills(skills);

                    InsertMutant task = new InsertMutant(MutanteActivity.this);
                    task.execute(tempmutant);
                }
                else {
                    mutant.setSkills(skills);
                    mutant.setName(etName.getText().toString());

                    ChangeMutant task = new ChangeMutant(MutanteActivity.this);
                    task.execute(mutant);
                }
            }
        });

        ImageButton btnAdd = findViewById(R.id.ibtnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MutanteActivity.this, SelectingSkillActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            if (mutant == null || mutant.getId() == null) {
                Toast.makeText(this, "Mutante ainda não existe :/", Toast.LENGTH_SHORT).show();
            }
            else {
                DeleteMutant task = new DeleteMutant(MutanteActivity.this);
                task.execute(mutant);
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mutant, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 100) {
            Skill skill = (Skill) data.getSerializableExtra("skill");

            for (int i = 0; i < lvSkills.getAdapter().getCount(); i++) {
                if (lvSkills.getAdapter().getItem(i).equals(skill)) {
                    Toast.makeText(this, "Habilidade já adicionada :(", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            ((ArrayAdapter) lvSkills.getAdapter()).add(skill);
        }
    }

    public class DeleteMutant extends DefaultTask<Mutant, Void, List<String>> {

        private Mutant name;

        public DeleteMutant(Context context) {
            super(context);
        }

        @Override
        protected List<String> executeTask(Mutant... voids) throws IOException {
            this.name = voids[0];
            return new ApplicationService().delete(voids[0].getId());
        }

        @Override
        protected void onFinish(List<String> strings) {
            if (strings.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("mutant", this.name);
                setResult(202, resultIntent);
                finish();
            }
            else {
                Toast.makeText(context, strings.get(1), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ChangeMutant extends DefaultTask<Mutant, Void, List<String>> {

        private Mutant name;

        public ChangeMutant(Context context) {
            super(context);
        }

        @Override
        protected List<String> executeTask(Mutant... voids) throws IOException {
            this.name = voids[0];
            return new ApplicationService().changeMutant(voids[0]);
        }

        @Override
        protected void onFinish(List<String> strings) {
            if (strings.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("mutant", this.name);
                setResult(201, resultIntent);
                finish();
            }
            else {
                Toast.makeText(context, strings.get(1), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class InsertMutant extends DefaultTask<Mutant, Void, List<String>> {

        private Mutant name;

        public InsertMutant(Context context) {
            super(context);
        }

        @Override
        protected List<String> executeTask(Mutant... voids) throws IOException {
            this.name = voids[0];
            return new ApplicationService().newMutant(voids[0]);
        }

        @Override
        protected void onFinish(List<String> strings) {
            if (strings.size() == 1) {
                this.name.setId(Integer.valueOf(strings.get(0)));

                Intent resultIntent = new Intent();
                resultIntent.putExtra("mutant", this.name);
                setResult(200, resultIntent);
                finish();
            }
            else {
                Toast.makeText(context, strings.get(1), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

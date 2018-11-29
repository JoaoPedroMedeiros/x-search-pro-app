package com.jpcami.tads.xsearch;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jpcami.tads.xsearch.entity.Mutant;
import com.jpcami.tads.xsearch.entity.Skill;
import com.jpcami.tads.xsearch.service.ApplicationService;
import com.jpcami.tads.xsearch.sqlite.MutantOperations;
import com.jpcami.tads.xsearch.util.DefaultTask;
import com.jpcami.tads.xsearch.view.MutantAdapter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SelectingSkillActivity extends AppCompatActivity {

    private ArrayAdapter<Skill> adapter;

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting_skill);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MutantOperations mutantOperations = new MutantOperations(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder( SelectingSkillActivity.this);
                builder.setTitle("Nova habilidade");

                // Set up the input
                final EditText input = new EditText(SelectingSkillActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);


            // Set up the buttons
            builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    InsertSkill task = new InsertSkill( SelectingSkillActivity.this);
                    task.execute(input.getText().toString());
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            }
        });

        list = findViewById(R.id.lvSkills);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectSkill(adapter.getItem(i));
            }
        });

        SearchSkill task = new SearchSkill(this);
        task.execute((Void) null);
    }

    private void selectSkill(Skill skill) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("skill", skill);
        setResult(100, resultIntent);
        finish();
    }

    public class SearchSkill extends DefaultTask<Void, Void, List<Skill>> {

        public SearchSkill(Context context) {
            super(context);
        }

        @Override
        protected List<Skill> executeTask(Void... voids) throws IOException {
            return new ApplicationService().getSkills();
        }

        @Override
        protected void onFinish(List<Skill> mutants) {
            adapter = new ArrayAdapter<>(this.context, android.R.layout.simple_list_item_1);
            adapter.addAll(mutants);
            list.setAdapter(adapter);
        }
    }

    public class InsertSkill extends DefaultTask<String, Void, List<String>> {

        private String name;

        public InsertSkill(Context context) {
            super(context);
        }

        @Override
        protected List<String> executeTask(String... voids) throws IOException {
            this.name = voids[0];
            return new ApplicationService().newSkill(voids[0]);
        }

        @Override
        protected void onFinish(List<String> strings) {
            if (strings.size() == 1) {
                Skill skill = new Skill();
                skill.setId(Integer.valueOf(strings.get(0)));
                skill.setName(this.name);
                selectSkill(skill);
            }
            else {
                Toast.makeText(context, strings.get(1), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

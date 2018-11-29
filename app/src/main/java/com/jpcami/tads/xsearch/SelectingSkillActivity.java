package com.jpcami.tads.xsearch;

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

import com.jpcami.tads.xsearch.entity.Skill;
import com.jpcami.tads.xsearch.sqlite.MutantOperations;

import java.sql.SQLException;
import java.util.List;

public class SelectingSkillActivity extends AppCompatActivity {

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
                    Skill skill = mutantOperations.createSkill(input.getText().toString());
                    selectSkill(skill);
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

        ListView list = findViewById(R.id.lvSkills);

        final List<Skill> skills = mutantOperations.getAllSkills();


        ArrayAdapter<Skill> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(skills);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectSkill(skills.get(i));
            }
        });
    }

    private void selectSkill(Skill skill) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("skill", skill);
        setResult(100, resultIntent);
        finish();
    }
}

package com.jpcami.tads.xsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.jpcami.tads.xsearch.entity.Mutant;
import com.jpcami.tads.xsearch.service.ApplicationService;
import com.jpcami.tads.xsearch.sqlite.MutantOperations;
import com.jpcami.tads.xsearch.util.DefaultTask;
import com.jpcami.tads.xsearch.view.MutantAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Mutant> adapter;

    private ListView lvMutant;

    private View layoutView;

    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MutanteActivity.class);
                startActivityForResult(intent, 200);
            }
        });

        lvMutant = findViewById(R.id.lvMutantes);
        lvMutant.requestFocus();

        lvMutant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, MutanteActivity.class);
                intent.putExtra("mutant", (Serializable) adapterView.getAdapter().getItem(i));
                startActivityForResult(intent, 200);
            }
        });

        ((Button) findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchMutants task = new MainActivity.SearchMutants(MainActivity.this,
                        (((EditText) findViewById(R.id.etSearch)).getText().toString().isEmpty() ? null : ((EditText) findViewById(R.id.etSearch)).getText().toString()));
                task.execute((Void) null);
            }
        });

        MutantOperations mutantOperations = new MutantOperations(this);

//        List<Mutant> mutants = mutantOperations.getAllMutants();
//        adapter = new MutantAdapter(this, mutants);
//        lvMutant.setAdapter(adapter);

        EditText etSearch = findViewById(R.id.etSearch);
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                adapter.getFilter().filter(charSequence);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                adapter.notifyDataSetChanged();
//            }
//        });

        layoutView = findViewById(R.id.layoutMain);
        progressView = findViewById(R.id.progress);

        SearchMutants task = new MainActivity.SearchMutants(this, null);
        task.execute((Void) null);
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
        if (id == R.id.action_exit) {
            finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 200) {
            Mutant mutant = (Mutant) data.getSerializableExtra("mutant");
            if (mutant != null) {
                adapter.add(mutant);
            }
        }
        if (resultCode == 201) {
            Mutant mutant = (Mutant) data.getSerializableExtra("mutant");
            if (mutant != null) {
                adapter.remove(mutant);
                adapter.add(mutant);
            }
        }
        if (resultCode == 202) {
            Mutant mutant = (Mutant) data.getSerializableExtra("mutant");
            adapter.remove(mutant);
        }

        adapter.sort(new Comparator<Mutant>() {
            @Override
            public int compare(Mutant mutant, Mutant t1) {
                return mutant.getName().compareTo(t1.getName());
            }
        });
        adapter.notifyDataSetChanged();
    }

    public class SearchMutants extends DefaultTask<Void, Void, List<Mutant>> {

        private String filter;

        public SearchMutants(Context context, String filter) {
            super(context);
            this.filter = filter;
        }

        @Override
        protected List<Mutant> executeTask(Void... voids) throws IOException {
            return new ApplicationService().getMutants(this.filter);
        }

        @Override
        protected void onFinish(List<Mutant> mutants) {
            adapter = new MutantAdapter((Activity) this.context, mutants);
            lvMutant.setAdapter(adapter);
        }
    }
}

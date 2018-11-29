package com.jpcami.tads.xsearch.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.jpcami.tads.xsearch.R;
import com.jpcami.tads.xsearch.entity.Mutant;
import com.jpcami.tads.xsearch.entity.Skill;

import java.util.ArrayList;
import java.util.List;

public class MutantAdapter extends ArrayAdapter<Mutant> {

    private Activity context;

    public MutantAdapter(Activity context, List<Mutant> mutants) {
        super(context, R.layout.mutant_cell, mutants);
        this.context = context;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mutant_cell, null, true);

        TextView tvName = rowView.findViewById(R.id.tvName);
        tvName.setText(getItem(position).getName());

        LinearLayout layoutSkills = rowView.findViewById(R.id.layoutSkills);
        for (Skill skill : getItem(position).getSkills()) {
            TextView tvSkill = new TextView(this.context);
            tvSkill.setBackground(ContextCompat.getDrawable(context, R.drawable.skill_shape));
            tvSkill.setText(skill.getName());
            tvSkill.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tvSkill.setPadding(5, 5, 5, 5);
            tvSkill.setTextColor(R.color.colorPrimary);

            layoutSkills.addView(tvSkill);

            Space space = new Space(this.context);
            space.setMinimumWidth(10);
            layoutSkills.addView(space);
        }

        return rowView;
    }
}

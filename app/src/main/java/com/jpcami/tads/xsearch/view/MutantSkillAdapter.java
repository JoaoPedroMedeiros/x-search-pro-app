package com.jpcami.tads.xsearch.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jpcami.tads.xsearch.R;
import com.jpcami.tads.xsearch.entity.Skill;

public class MutantSkillAdapter extends ArrayAdapter<Skill> {

    private OnSkillRemoved onSkillRemoved;

    public MutantSkillAdapter(Context context) {
        super(context, R.layout.skill_cell);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.skill_cell, null, true);

        TextView tvName = rowView.findViewById(R.id.tvSkill);
        tvName.setText(getItem(position).getName());

        ImageButton button = rowView.findViewById(R.id.ibtnRemove);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnSkillRemoved() != null)
                    getOnSkillRemoved().onRemoved(v, getItem(position));
            }
        });

        return rowView;
    }

    public OnSkillRemoved getOnSkillRemoved() {
        return onSkillRemoved;
    }

    public void setOnSkillRemoved(OnSkillRemoved onSkillRemoved) {
        this.onSkillRemoved = onSkillRemoved;
    }

    public interface OnSkillRemoved {

        void onRemoved(View view, Skill skill);
    }
}

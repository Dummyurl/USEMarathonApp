package ru.use.marathon.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.typeform.Forms;

/**
 * Created by ilyas on 01-Oct-18.
 */

public class TypeformFormsAdapter extends RecyclerView.Adapter<TypeformFormsAdapter.TypeformViewHolder> {

    private Forms forms;


    public TypeformFormsAdapter(Forms forms) {
        this.forms = forms;
    }

    @NonNull
    @Override
    public TypeformViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_typeform_form_item, parent, false);
        return new TypeformViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeformViewHolder holder, int position) {
        holder.title.setText(String.valueOf(forms.itemTitle(position)));
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }

    class TypeformViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.form_title)
        TextView title;

        public TypeformViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

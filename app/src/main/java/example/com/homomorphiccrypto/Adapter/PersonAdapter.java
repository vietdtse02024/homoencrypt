package example.com.homomorphiccrypto.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.homomorphiccrypto.Main.DetailActivity;
import example.com.homomorphiccrypto.Model.Person;
import example.com.homomorphiccrypto.R;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private List<Person> personList;
    private Activity mContext;

    public PersonAdapter(Activity mContext, List<Person> personList) {
        this.personList = personList;
        this.mContext = mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.placeholder)
        ImageView placeholder;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.note)
        TextView note;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.checkbox)
        CheckBox checkbox;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    personList.get(getAdapterPosition()).setSelected(isChecked);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("person", personList.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {
        }
    }


    @Override
    public int getItemCount() {
        return personList.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_persion, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.note.setText(personList.get(position).getAddress());
        holder.name.setText(personList.get(position).getName());
        holder.description.setText(personList.get(position).getDescription());
        holder.checkbox.setChecked(personList.get(position).getSelected());

        Glide.with(mContext)
                .load(personList.get(position).getImgUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .centerCrop()
                .into(holder.placeholder);
    }
}
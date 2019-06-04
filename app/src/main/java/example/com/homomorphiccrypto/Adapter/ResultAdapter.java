package example.com.homomorphiccrypto.Adapter;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.homomorphiccrypto.Model.Person;
import example.com.homomorphiccrypto.R;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private List<Person> personList;
    private Activity mContext;

    public ResultAdapter(Activity mContext, List<Person> personList) {
        this.personList = personList;
        this.mContext = mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.placeholder)
        ImageView placeholder;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.note)
        TextView note;
        @BindView(R.id.tvResult)
        TextView tvResult;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
                .inflate(R.layout.row_result, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.note.setText(personList.get(position).getAddress());
        holder.name.setText(personList.get(position).getName());
        holder.tvResult.setText("" + personList.get(position).getVoteCount());

        Glide.with(mContext)
                .load(personList.get(position).getImgUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .centerCrop()
                .into(holder.placeholder);
    }
}
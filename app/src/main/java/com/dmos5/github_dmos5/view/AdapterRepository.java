package com.dmos5.github_dmos5.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmos5.github_dmos5.R;
import com.dmos5.github_dmos5.model.Repository;

import java.util.List;

public class AdapterRepository extends RecyclerView.Adapter<AdapterRepository.RepositoryViewHolder>  {

    private List<Repository> repositories;
    private Context mContext;

    public AdapterRepository(@NonNull List<Repository> repositories, Context context) {
        this.repositories = repositories;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AdapterRepository.RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repository_item, parent, false);
        RepositoryViewHolder viewHolder =new RepositoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRepository.RepositoryViewHolder holder, int position) {
        holder.txtvRepositoryName.setText(repositories.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (repositories != null){
            return repositories.size();
        }
        return 0;
    }


    class RepositoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtvRepositoryName;

        public RepositoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvRepositoryName = itemView.findViewById(R.id.txtv_repository);
        }
    }

    public void update(List<Repository> repositories, ImageView imageView, RecyclerView recyclerView) {
        this.repositories = repositories;
        imageView.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
    }
}

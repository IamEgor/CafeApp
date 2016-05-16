package com.example.yegor.cafeapp.adapters;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yegor.cafeapp.App;
import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.activities.ListOffersActivity;
import com.example.yegor.cafeapp.models.Category;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<Category> models;

    public CategoriesAdapter(List<Category> models) {
        this.models = models;
    }

    public void setModels(List<Category> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Category model = models.get(position);
        holder.setImage(model.getImage());
        holder.setText(model.getCategory());
        holder.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), ListOffersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Category.ID_EXTRA, model.getId());
                App.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private static final int LAYOUT_ID = R.layout.item_categories;

        private ImageView image;
        private TextView text;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(LAYOUT_ID, parent, false));

            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        public void setImage(@DrawableRes int resId) {
            image.setImageResource(resId);
        }

        public void setText(String text) {
            this.text.setText(text);
        }

        public void setClickListener(View.OnClickListener clickListener) {
            itemView.setOnClickListener(clickListener);
        }

    }

}

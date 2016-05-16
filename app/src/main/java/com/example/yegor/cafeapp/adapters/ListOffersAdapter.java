package com.example.yegor.cafeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.Utils;
import com.example.yegor.cafeapp.activities.OfferActivity;
import com.example.yegor.cafeapp.models.Offer;
import com.example.yegor.cafeapp.models.Param;
import com.example.yegor.cafeapp.storage.MySQLiteClass;

import java.util.List;
import java.util.Map;

public class ListOffersAdapter extends RecyclerView.Adapter<ListOffersAdapter.ViewHolder> {

    private Context context;
    private List<Offer> models;
    private Map<Integer, Integer> map;

    public ListOffersAdapter(Context context, List<Offer> models) {
        this.models = models;
        this.context = context;
        map = (new MySQLiteClass(context)).getId2ImageResMap();
    }

    public void setModels(List<Offer> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    @Override
    public ListOffersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ListOffersAdapter.ViewHolder holder, int position) {
        final Offer offer = models.get(position);

        holder.setIcon(Integer.parseInt(offer.getCategoryId()));
        holder.setLabel(offer.getName());
        holder.setPrice(offer.getPrice());
        holder.setWeight(offer.getParams());

        holder.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OfferActivity.class);
                intent.putExtra(Offer.EXTRA, offer);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private static final int LAYOUT_ID = R.layout.item_list_offer;

        private ImageView icon;
        private TextView label, price, weight;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(LAYOUT_ID, parent, false));

            icon = (ImageView) itemView.findViewById(R.id.icon);
            label = (TextView) itemView.findViewById(R.id.label);
            price = (TextView) itemView.findViewById(R.id.price);
            weight = (TextView) itemView.findViewById(R.id.weight);
        }

        public void setIcon(int id) {
            Log.w("setIcon", "icon = null" + (icon == null));
            Log.w("setIcon", "map.get(" + id + ") = null" + (map.get(id) == null));
            if (map.get(id) == null)
                icon.setImageResource(R.drawable.ic_cat_unknown_0);
            else
                icon.setImageResource(map.get(id));
        }

        public void setLabel(String name) {
            this.label.setText(String.format(context.getString(R.string.name), name));
        }

        public void setPrice(String price) {
            this.price.setText(String.format(context.getString(R.string.price), price));
        }

        public void setWeight(List<Param> params) {
            String weight = Utils.getParamByName(params, "Вес");
            this.weight.setText(weight.length() == 0 ? weight :
                    String.format(context.getString(R.string.weight), weight));
        }

        public void setClickListener(View.OnClickListener clickListener) {
            itemView.setOnClickListener(clickListener);
        }

    }

}

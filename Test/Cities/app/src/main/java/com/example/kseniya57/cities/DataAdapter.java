package com.example.kseniya57.cities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<City> cities;

    DataAdapter(Context context, List<City> cities) {
        this.cities = cities;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        City city = cities.get(position);
        holder.contentView.setText(city.getName() + " / " + city.getCountry() + " (" + city.getLat() + ", " + city.getLon() + ")");
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView contentView;
        ViewHolder(View view){
            super(view);
            contentView = (TextView) view.findViewById(R.id.content);
        }
    }
}

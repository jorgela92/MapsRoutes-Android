package es.fingercode.racehub.mapsroute.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.fingercode.racehub.mapsroute.R;
import es.fingercode.racehub.mapsroute.model.Route;

/**
 * Created by jorge on 28/11/16.
 */

public class ListRoutesAdapter extends RecyclerView.Adapter<ListRoutesAdapter.RoutesViewHolder>{
    private List<Route> routes;
    public static RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    public static RecyclerViewOnItemClickListener getRecyclerViewOnItemClickListener(){
        return recyclerViewOnItemClickListener;
    }
    public ListRoutesAdapter(List<Route> routes, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener){
        this.routes = routes;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }
    @Override
    public RoutesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_route, parent, false);
        RoutesViewHolder pvh = new RoutesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RoutesViewHolder holder, int position) {
        holder.titleRoute.setText(routes.get(position).getTitle());
        holder.descriptionRoute.setText(routes.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public static class RoutesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView titleRoute;
        TextView descriptionRoute;
        RoutesViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cv.setOnClickListener(this);
            titleRoute = (TextView)itemView.findViewById(R.id.title_route);
            descriptionRoute = (TextView)itemView.findViewById(R.id.description_route);
        }

        @Override
        public void onClick(View view) {
            ListRoutesAdapter.getRecyclerViewOnItemClickListener().onClick(view, getAdapterPosition());
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
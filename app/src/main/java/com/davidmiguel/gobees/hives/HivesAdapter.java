package com.davidmiguel.gobees.hives;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.utils.BaseViewHolder;
import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Hives list adapter.
 */
class HivesAdapter extends RecyclerView.Adapter<HivesAdapter.ViewHolder> {

    private List<Hive> hives;
    private HivesAdapter.HiveItemListener listener;

    HivesAdapter(List<Hive> hives, HivesAdapter.HiveItemListener listener) {
        this.hives = checkNotNull(hives);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hives_list_item, parent, false);
        return new HivesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(hives.get(position));
    }

    @Override
    public int getItemCount() {
        return hives == null ? 0 : hives.size();
    }

    void replaceData(List<Hive> hives) {
        this.hives = checkNotNull(hives);
        notifyDataSetChanged();
    }

    interface HiveItemListener {
        void onHiveClick(Hive clickedHive);

        void onHiveDelete(Hive clickedHive);
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements BaseViewHolder<Hive>, View.OnClickListener, ItemTouchHelperViewHolder {

        private CardView card;
        private TextView hiveName;
        private Drawable background;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            card = (CardView) itemView.findViewById(R.id.card);
            hiveName = (TextView) itemView.findViewById(R.id.hive_name);
            background = card.getBackground();
        }

        public void bind(@NonNull Hive hive) {
            hiveName.setText(hive.getName());
        }

        @Override
        public void onClick(View view) {
            listener.onHiveClick(hives.get(getAdapterPosition()));
        }

        @Override
        public void onItemSelected() {
            card.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            card.setBackground(background);
        }
    }
}
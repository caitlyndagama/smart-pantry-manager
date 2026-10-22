package com.smartpantrymanager.adapters;

import com.smartpantrymanager.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.smartpantrymanager.logic.ExpiryUtils;
import com.smartpantrymanager.logic.UnitConverter;
import com.smartpantrymanager.model.PantryItem;
import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder>{
        public interface OnItemClickListener {
            void onItemClick(PantryItem item);
        }

        private final List<PantryItem> items = new ArrayList<>();
        private final OnItemClickListener listener;
        private boolean expiryAlertsEnabled = true;
        private int warningDays = 3;

        public PantryAdapter(OnItemClickListener listener) {
            this.listener = listener;
        }

        public void setItems(List<PantryItem> newItems) {
            items.clear();
            if (newItems != null) {
                items.addAll(newItems);
            }
            notifyDataSetChanged();
        }
        public void setExpirySettings(boolean alertsEnabled, int warningDays) {
            this.expiryAlertsEnabled = alertsEnabled;
            this.warningDays = warningDays;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_pantry, parent, false);
            return new PantryViewHolder(row);
        };

        @Override
        public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class PantryViewHolder extends RecyclerView.ViewHolder {
            private final TextView textItemName;
            private final TextView textItemQuantity;
            private final TextView textExpiryBadge;

            PantryViewHolder(@NonNull View itemView) {
                super(itemView);
                textItemName = itemView.findViewById(R.id.textItemName);
                textItemQuantity = itemView.findViewById(R.id.textItemQuantity);
                textExpiryBadge = itemView.findViewById(R.id.textExpiryBadge);
            }

            void bind(PantryItem item) {
                textItemName.setText(item.getDisplayName());
                textItemQuantity.setText(UnitConverter.format(item.getQuantity(), item.getBaseUnit()));
                showExpiryBadge(item.getExpiryDate());
                itemView.setOnClickListener(v -> listener.onItemClick(item));
            }

            private void showExpiryBadge(String expiryDate) {
                int days = ExpiryUtils.daysUntil(expiryDate);
                boolean hide = !expiryAlertsEnabled
                        || days == Integer.MAX_VALUE
                        || days > warningDays;
                if (hide) {
                    textExpiryBadge.setVisibility(View.GONE);
                    return;
                }
                textExpiryBadge.setVisibility(View.VISIBLE);
                if (days < 0) {
                    textExpiryBadge.setText(R.string.badge_expired);
                    textExpiryBadge.setBackgroundResource(R.drawable.background_badge_exp);
                    textExpiryBadge.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.alert_red));
                } else {
                    String text = days == 0
                            ? itemView.getContext().getString(R.string.badge_expires_today)
                            : itemView.getResources().getQuantityString(R.plurals.badge_expires_in, days, days);
                    textExpiryBadge.setText(text);
                    textExpiryBadge.setBackgroundResource(R.drawable.background_badge_warn);
                    textExpiryBadge.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.warning_amber_text));
                }
            }
        }
}
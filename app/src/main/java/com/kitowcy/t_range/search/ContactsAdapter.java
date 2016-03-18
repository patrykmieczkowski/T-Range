package com.kitowcy.t_range.search;

/**
 * Created by Łukasz Marczak
 *
 * @since 18.03.16
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitowcy.t_range.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsAdapterHolder> {
    public static final String TAG = ContactsAdapter.class.getSimpleName();

    public interface OnClick {
        void onClick(Contact c);
    }

    public List<Contact> dataSet = new ArrayList<>();
    @NonNull
    android.content.Context context;

    OnClick listener;

    public ContactsAdapter(@NonNull android.content.Context context,
                           @Nullable List<Contact> dataSet,
                           OnClick listener) {
        this.context = context;
        if (dataSet != null)
            this.dataSet.addAll(dataSet);
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public ContactsAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,
                parent, false);
        return new ContactsAdapterHolder(view);
    }


    @Override
    public void onBindViewHolder(final ContactsAdapterHolder holder, final int position) {
        final Contact item = dataSet.get(position);
        holder.name.setText(item.name);
        holder.number.setText(item.phoneNumber);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + item.name);
                listener.onClick(item);
            }
        });
    }

    public void refresh(@NonNull List<Contact> newDataSet) {
        dataSet.clear();
        dataSet.addAll(newDataSet);
        notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
    }

    static class ContactsAdapterHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.phoneName)
        TextView name;
        @Bind(R.id.phoneNumber)
        TextView number;

        public ContactsAdapterHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            itemView.setClickable(true);
        }
    }
}
    
package com.kitowcy.t_range.search;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.kitowcy.t_range.R;
import com.kitowcy.t_range.utils.AnimateUtils;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class AdditionalSearchActivity extends Activity {
    public static final String TAG = AdditionalSearchActivity.class.getSimpleName();
    public static final String CONTACT = "CONTACT";
    @Bind(R.id.editText)
    SearchView searchView;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    ContactsAdapter contactsAdapter;
    final android.os.Handler mHandler = new android.os.Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_additional_search);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactsAdapter = new ContactsAdapter(this, Collections.<Contact>emptyList(), new ContactsAdapter.OnClick() {
            @Override
            public void onClick(final Contact c) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra(CONTACT, c);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setVisibility(View.GONE);

        SearchEngine engine = new SearchEngine();
        engine.getSuggestions(searchView).subscribe(new Action1<List<Contact>>() {
            @Override
            public void call(List<Contact> contacts) {
                Log.e(TAG, "call: " + contacts.size());
                if (recyclerView.getVisibility() == View.GONE) {

                    recyclerView.setVisibility(View.VISIBLE);
                    AnimateUtils.animateFade(recyclerView, 0, 1, 300);
                }
                AnimateUtils.compositeFade(mHandler, recyclerView, 1, .5f, 200);
                contactsAdapter.refresh(contacts);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "call: " + throwable.getMessage());
            }
        });
        searchView.requestFocus();
    }

}

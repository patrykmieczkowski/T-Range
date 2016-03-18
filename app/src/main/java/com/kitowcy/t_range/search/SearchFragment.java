package com.kitowcy.t_range.search;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitowcy.t_range.App;
import com.kitowcy.t_range.R;
import com.kitowcy.t_range.utils.AnimateUtils;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    public static final String TAG = SearchFragment.class.getSimpleName();
    @Bind(R.id.editText)
    SearchView searchView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.microphone)
    ImageView microphone;
    @Bind(R.id.messageTo)
    TextView messageTo;
    public  boolean contactChosen = false;
    ContactsAdapter contactsAdapter;
    final android.os.Handler mHandler = new android.os.Handler();

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, v);
        App.INSTANCE.currentFragment = this;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactsAdapter = new ContactsAdapter(getActivity(), Collections.<Contact>emptyList(), new ContactsAdapter.OnClick() {
            @Override
            public void onClick(final Contact c) {
                contactChosen = true;
                AnimateUtils.animateFade(recyclerView, 1, 0, 300, mHandler, new AnimateUtils.EndCallback() {
                    @Override
                    public void onEnd() {
                        recyclerView.setVisibility(View.GONE);
                        startAnimateRecorder(c);
                    }
                });
            }
        });

        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactChosen) {
                    performCall();
                } else {
                    showPopUpContactPicker();
                }
                AnimateUtils.compositeFade(mHandler, microphone, 1, 0, 600);
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

                    microphone.setVisibility(View.GONE);
                    messageTo.setVisibility(View.GONE);
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
        return v;
    }

    private void showPopUpContactPicker() {
        Intent intent = new Intent(getActivity(), AdditionalSearchActivity.class);
        getActivity().startActivityForResult(intent, 1);
    }

    private void performCall() {
        Toast.makeText(getActivity(), "CALL", Toast.LENGTH_SHORT).show();
    }

    public void startAnimateRecorder(final Contact contact) {
        Log.d(TAG, "startAnimateRecorder: ");
        microphone.setVisibility(View.VISIBLE);
        messageTo.setVisibility(View.VISIBLE);
        String message = "Message To: " + contact.name;
        messageTo.setText(message);
        AnimateUtils.animateFade(messageTo, 0, 1, 300);
        AnimateUtils.animateFade(microphone, 0, 1, 300);
    }
}

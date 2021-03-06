package com.kitowcy.t_range.search;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kitowcy.t_range.App;
import com.kitowcy.t_range.MainActivity;
import com.kitowcy.t_range.R;
import com.kitowcy.t_range.dialog.SendDialog;
import com.kitowcy.t_range.utils.AnimateUtils;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    public static final String TAG = SearchFragment.class.getSimpleName();
    @Bind(R.id.editText)
    SearchView searchView;

    @Bind(R.id.textHint)
    TextView textHint;


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.fragment_search_content)
    RelativeLayout relativeLayout;

    @Bind(R.id.microphone)
    ImageView microphone;

    @Bind(R.id.record_stop_text)
    TextView recordStopText;

    @Bind(R.id.record_stop_underscore)
    View recordStopUnderscore;

    @Bind(R.id.send_message_button)
    Button sendMessageButton;

    @Bind(R.id.messageTo)
    TextView messageTo;

    @Bind(R.id.textCounter)
    TextView textCounter;

    public boolean contactChosen = false;
    public boolean isSignal = true;
    private IntentFilter mIntentFilter;
    ContactsAdapter contactsAdapter;

    boolean aliveAnimation = true;
    boolean triggerForStop;

    final android.os.Handler mHandler = new android.os.Handler();

    RecorerHelper recorerHelper = new RecorerHelper();

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
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(MainActivity.mBroadcastNoSignal);
        mIntentFilter.addAction(MainActivity.mBroadcastSignalBack);
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
                searchView.setQuery("", false);
                hideKeyboard(getActivity());
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
                if (triggerForStop) {
                    triggerForStop = false;
                    stopRecord();
                    clearFlags();
                    microphone.animate().scaleX(1).setDuration(500).start();
                    microphone.animate().scaleY(1).setDuration(500).start();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textCounter.setText("message ready");
                                }
                            });
                        }
                    }, 500);
                    return;
                }
                if (contactChosen) {
                    microphone.setImageResource(R.drawable.record_stop);
                    recordStopText.setText("stop");
                    startRecord(new Callable() {
                        @Override
                        public void call(Boolean bool) {
                            if (bool) {
                                recorerHelper.recorder.start();
                                performCall(aliveAnimation);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textCounter.setText("recording...");
                                    }
                                });
                            } else {
                                Toast.makeText(SearchFragment.this
                                        .getActivity(), "failed to setup recorder", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    textHint.setVisibility(View.VISIBLE);
                    textHint.setText("Select Contact...");
                    AnimateUtils.compositeFade(mHandler, textHint, 0, 1, 600, new AnimateUtils.EndCallback() {
                        @Override
                        public void onEnd() {
                            textHint.setVisibility(View.GONE);
                        }
                    });
                    searchView.requestFocus();
                    searchView.setFocusable(true);
                    searchView.animate().scaleY(3f).setDuration(300).start();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchView.animate().scaleY(1f).setDuration(300).start();
                        }
                    }, 300);

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
                if (contacts.size() == 0) {
                    return;
                }
                if (recyclerView.getVisibility() == View.GONE) {
                    microphone.setVisibility(View.GONE);
                    messageTo.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recordStopText.setVisibility(View.GONE);
                    recordStopUnderscore.setVisibility(View.GONE);
                    sendMessageButton.setVisibility(View.GONE);
                    textCounter.setText("");
                    textCounter.setVisibility(View.GONE);
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void loopAnimation() {
        microphone.setScaleX(1);
        microphone.setScaleY(1);
        microphone.animate().scaleX(0.5f).setDuration(500).start();
        microphone.animate().scaleY(0.5f).setDuration(500).start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                microphone.animate().scaleX(1).setDuration(500).start();
                microphone.animate().scaleY(1).setDuration(500).start();
            }
        }, 700);
        performCall(aliveAnimation);
    }

    private void performCall(boolean boo) {
        triggerForStop = true;
        if (boo) mHandler.postDelayed(loopingAnimationRunnable, 1400);
    }


    private void startRecord(final Callable callable) {
        recorerHelper.prepareMediaRecorder().subscribeOn(Schedulers.io())
                .subscribe(new Action1<MediaRecorder>() {
                    @Override
                    public void call(MediaRecorder mediaRecorder) {
                        callable.call(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callable.call(false);
                    }
                });
    }

    private void stopRecord() {
        Log.d(TAG, "stopRecord: ");
        recorerHelper.recorder.stop();
    }

    void clearFlags() {
        contactChosen = false;
        triggerForStop = false;
        mHandler.removeCallbacks(loopingAnimationRunnable);
        aliveAnimation = true;
        microphone.setImageResource(R.drawable.record_start);
        recordStopText.setText("record");
    }

    public interface Callable {
        void call(Boolean bool);
    }

    private final Runnable loopingAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            loopAnimation();
        }
    };

    public void startAnimateRecorder(final Contact contact) {
        Log.d(TAG, "startAnimateRecorder: ");
        microphone.setVisibility(View.VISIBLE);
        messageTo.setVisibility(View.VISIBLE);
        recordStopText.setVisibility(View.VISIBLE);
        recordStopUnderscore.setVisibility(View.VISIBLE);
        sendMessageButton.setVisibility(View.VISIBLE);
        textCounter.setVisibility(View.VISIBLE);
        String message = "to: " + contact.name;
        messageTo.setText(message);

        AnimateUtils.animateFade(messageTo, 0, 1, 300);
        AnimateUtils.animateFade(microphone, 0, 1, 300);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MainActivity.mBroadcastNoSignal)) {
                Log.d(TAG, "No signal!");
            }
            if (intent.getAction().equals(MainActivity.mBroadcastSignalBack)) {
                Log.d(TAG, "Signal back");
            }
        }
    };

    @OnClick(R.id.send_message_button)
    public void sendMessageGo(){

        SendDialog sendDialog = new SendDialog(getActivity(), "message sent");
        sendDialog.show();
    }
}

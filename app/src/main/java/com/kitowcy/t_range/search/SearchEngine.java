package com.kitowcy.t_range.search;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.kitowcy.t_range.App;
import com.kitowcy.t_range.utils.GenericUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Łukasz Marczak
 *
 * @since 18.03.16
 */
public class SearchEngine {
    public List<Contact> currentContacts = new ArrayList<>();

    public rx.Observable<String> emitInputs(final SearchView searchView) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (!subscriber.isUnsubscribed() && query.length() > 1)
                            subscriber.onNext(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (!subscriber.isUnsubscribed() && newText.length() > 1)
                            subscriber.onNext(newText);
                        return false;
                    }
                });
            }
        }).debounce(200, TimeUnit.MILLISECONDS);
    }

    public Observable<List<Contact>> getSuggestions(final SearchView searchView) {
        return allContacts().flatMap(new Func1<List<Contact>, Observable<String>>() {
            @Override
            public Observable<String> call(List<Contact> _contacts) {
                currentContacts.clear();
                currentContacts.addAll(GenericUtils.removeDuplicates(_contacts));
                return emitInputs(searchView);
            }
        }).map(new Func1<String, List<Contact>>() {
            @Override
            public List<Contact> call(String input) {
                List<Contact> contacts = new ArrayList<Contact>();
                for (Contact c : currentContacts) {
                    if (c.name.toLowerCase().contains(input.toLowerCase()) ||
                            c.phoneNumber.toLowerCase().contains(input.toLowerCase())) {
                        contacts.add(c);
                    }
                }
                return contacts;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Contact>> allContacts() {
        return Observable.create(new Observable.OnSubscribe<List<Contact>>() {
            @Override
            public void call(Subscriber<? super List<Contact>> subscriber) {
                long startnow = System.currentTimeMillis();
                long endnow = 0;
                List<Contact> arrContacts = new ArrayList<>();

                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;
                Cursor cursor = App.INSTANCE.getContentResolver().query(uri,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone._ID,
                                ContactsContract.Contacts._ID}, selection, null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                if (cursor == null) {
                    subscriber.onError(new Throwable("Cursor is null!!!"));
                    return;
                }
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    int contactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Log.d("con ", "name " + contactName + " " + " PhoeContactID " + phoneContactID + "  ContactID " + contactID);
                    arrContacts.add(new Contact(contactName, contactNumber));
                    cursor.moveToNext();
                }
                cursor.close();
                cursor = null;

                endnow = System.currentTimeMillis();
                Log.d("END", "TimeForContacts " + (endnow - startnow) + " ms");
                subscriber.onNext(arrContacts);
            }
        });
    }

}

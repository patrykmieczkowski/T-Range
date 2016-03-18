package com.kitowcy.t_range.search;

import java.io.Serializable;

/**
 * Created by Łukasz Marczak
 *
 * @since 18.03.16
 */
public class Contact implements Serializable {
    public String name;
    public String phoneNumber;

    public Contact() {
    }

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (!name.equals(contact.name)) return false;
        return phoneNumber.equals(contact.phoneNumber);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        return result;
    }
}

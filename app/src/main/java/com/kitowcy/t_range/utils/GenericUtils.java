package com.kitowcy.t_range.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Łukasz Marczak
 *
 * @since 18.03.16
 */
public class GenericUtils {

    public static <T> List<T> removeDuplicates(Collection<T> collection) {
        List<T> uniques = new ArrayList<>();
        Set<T> set = new HashSet<>();
        set.addAll(collection);
        uniques.addAll(set);
        return uniques;
    }
}

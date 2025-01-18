package com.library.management.util;

import java.util.Comparator;
import java.util.List;

public class SortingAndSearchingUtils {

    public static <T> void mergeSort(List<T> list, Comparator<? super T> comparator) {
        if (list.size() <= 1) {
            return;
        }
        int mid = list.size() / 2;
        List<T> left = list.subList(0, mid);
        List<T> right = list.subList(mid, list.size());

        mergeSort(left, comparator);
        mergeSort(right, comparator);
        merge(list, left, right, comparator);
    }

    private static <T> void merge(List<T> list, List<T> left, List<T> right, Comparator<? super T> comparator) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) {
            list.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            list.set(k++, right.get(j++));
        }
    }

    public static <T> int binarySearch(List<T> list, T key, Comparator<? super T> comparator) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = comparator.compare(list.get(mid), key);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}

package com.tofa.circular.listener;

import android.util.Pair;

public interface OnClickItemListener {
    void OnItemClick(Pair<String, String> item);
    void OnItemClick(String item);
}

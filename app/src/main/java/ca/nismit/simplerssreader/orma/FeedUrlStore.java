package ca.nismit.simplerssreader.orma;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class FeedUrlStore {

    public static FeedUrlStore create(@NonNull String title, @NonNull String url, @Nullable String category) {
        FeedUrlStore feedUrlStore = new FeedUrlStore();
        feedUrlStore.title = title;
        feedUrlStore.url = url;
        feedUrlStore.category = category;
        return feedUrlStore;
    }

    @PrimaryKey
    public long id;

    @Column(indexed = true)
    public String title;

    @Column
    public String url;

    @Column
    @Nullable
    public String category;
}

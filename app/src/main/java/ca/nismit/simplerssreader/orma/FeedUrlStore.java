package ca.nismit.simplerssreader.orma;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class FeedUrlStore {

    private static FeedUrlStore_Relation relation;

    public static void initRelaion(Context context) {
        relation = getRelation(context);
    }

    @Nullable
    static FeedUrlStore_Relation getRelation() {
        return relation;
    }

    @Nullable
    static FeedUrlStore_Relation getRelation(Context context) {
        OrmaDatabase orma = OrmaDatabase.builder(context).build();
        return orma.relationOfFeedUrlStore();
    }

//    public static FeedUrlStore create(@NonNull String title, @NonNull String url, @Nullable String category) {
//        FeedUrlStore feedUrlStore = new FeedUrlStore();
//        feedUrlStore.title = title;
//        feedUrlStore.url = url;
//        feedUrlStore.category = category;
//        return feedUrlStore;
//    }

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(indexed = true)
    public String title;

    @Column(indexed = true)
    public String url;

    @Column
    @Nullable
    public String category;

    @Column(defaultExpr = "false")
    @NonNull
    public boolean delete = false;
}

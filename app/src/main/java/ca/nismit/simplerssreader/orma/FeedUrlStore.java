package ca.nismit.simplerssreader.orma;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.util.List;

@Table
public class FeedUrlStore {

//    public static FeedUrlStore create(@NonNull String title, @NonNull String url, @Nullable String category) {
//        FeedUrlStore feedUrlStore = new FeedUrlStore();
//        feedUrlStore.title = title;
//        feedUrlStore.url = url;
//        feedUrlStore.category = category;
//        return feedUrlStore;
//    }


    public FeedUrlStore() {
    }

    public FeedUrlStore(@NonNull String title, @NonNull String url, @Nullable String category) {
        this.title = title;
        this.url = url;
        this.category = category;
    }

    private static FeedUrlStore_Relation relation;

    public static void initRelaion(Context context) {
        relation = getRelation(context);
    }

    @Nullable
    public static FeedUrlStore_Relation getRelation() {
        return relation;
    }

    @Nullable
    static FeedUrlStore_Relation getRelation(Context context) {
        OrmaDatabase orma = OrmaDatabase.builder(context).build();
        return orma.relationOfFeedUrlStore();
    }

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(indexed = true)
    @NonNull
    public String title;

    @Column(indexed = true)
    @NonNull
    public String url;

    @Column
    @Nullable
    public String category;

    @Column(defaultExpr = "false")
    @NonNull
    public boolean delete = false;

    @WorkerThread
    public void insertData() {
        id = getRelation().inserter().execute(this);
    }

    @WorkerThread
    public static List<FeedUrlStore> getAll() {
        return getRelation().selector().toList();
    }

    @WorkerThread
    public static List<FeedUrlStore> relationGetAll(FeedUrlStore_Relation relation) { return relation.selector().toList(); }
}

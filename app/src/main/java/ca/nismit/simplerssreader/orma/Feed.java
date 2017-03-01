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
public class Feed {

    public Feed() {
    }

    public Feed(@NonNull String title, @NonNull String url, @Nullable String category) {
        this.title = title;
        this.url = url;
        this.category = category;
    }

    private static Feed_Relation relation;

    public static void initRelaion(Context context) {
        relation = getRelation(context);
    }

    @Nullable
    public static Feed_Relation getRelation() {
        return relation;
    }

    @Nullable
    static Feed_Relation getRelation(Context context) {
        OrmaDatabase orma = OrmaDatabase.builder(context).build();
        return orma.relationOfFeed();
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

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }

    @WorkerThread
    public void insertData() {
        id = getRelation().inserter().execute(this);
    }

    @WorkerThread
    public static List<Feed> getAll() {
        return getRelation().selector().toList();
    }

    @WorkerThread
    public static List<Feed> relationGetAll(Feed_Relation relation) { return relation.selector().toList(); }

    @WorkerThread
    public void updateTable(long id) {
        getRelation().updater()
                .idEq(id)
                .title(title)
                .url(url)
                .category(category)
                .execute();

    }

    @WorkerThread
    public void deleteTable(long id) {
        getRelation().deleter()
                .idEq(id)
                .execute();
    }


}

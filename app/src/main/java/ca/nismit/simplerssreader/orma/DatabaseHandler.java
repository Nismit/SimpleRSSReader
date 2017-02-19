package ca.nismit.simplerssreader.orma;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DatabaseHandler {
    OrmaDatabase orma;

    void createUrl(@NonNull String title, @NonNull String url, @Nullable String category) {
        orma.insertIntoFeedUrlStore(FeedUrlStore.create(title, url, category));
    }

    void update(long id, String title) {
        orma.updateFeedUrlStore()
                .idEq(id)
                .titleEq(title)
                .execute();
    }

    void delete(long id) {
        orma.deleteFromFeedUrlStore()
                .idEq(id)
                .execute();
    }
}

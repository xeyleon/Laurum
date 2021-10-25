package com.laurum.Resources;

import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.laurum.Database.LaurumDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ResourceContent {
    /**
     * An array of sample (placeholder) items.
     */
    public static final List<ResourceItem> ITEMS = new ArrayList<ResourceItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, ResourceItem> ITEM_MAP = new HashMap<String, ResourceItem>();

    private static final int COUNT = 10;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createResourceItem(i));
        }
    }

    private static void addItem(ResourceItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ResourceItem createResourceItem(int position) {
        return new ResourceItem(Integer.toString(position), "Resource " + position, getDetails(position), "", "");
    }

    private static String getDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Resource ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\n...");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class ResourceItem {
        public final String id;
        public final String title;
        public final String description;
        public final String icon;
        public final String url;

        public ResourceItem(String id, String title, String description, String url, String icon) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.url = url;
            this.icon = icon;
        }

        @NonNull
        @Override
        public String toString() {
            return title;
        }
    }
}
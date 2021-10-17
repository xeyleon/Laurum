package com.example.laurum.Reminders;

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
public class ReminderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<ReminderItem> ITEMS = new ArrayList<ReminderItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, ReminderItem> ITEM_MAP = new HashMap<String, ReminderItem>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createReminderItem(i));
        }
    }

    private static void addItem(ReminderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ReminderItem createReminderItem(int position) {
        return new ReminderItem("Reminder " + position, "Reminder Description", makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class ReminderItem {
        public final String id;
        public final String content;
        public final String details;

        public ReminderItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
package dev.emi.floralisia.recipe;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class FloralisiaIngredient {
    private static final Predicate<? super FloralisiaIngredient.Entry> NON_EMPTY = (entry) -> {
        return !entry.getStacks().stream().allMatch(ItemStack::isEmpty);
     };
     public static final FloralisiaIngredient EMPTY = new FloralisiaIngredient(Stream.empty());
     public final FloralisiaIngredient.Entry[] entries;
     public ItemStack[] matchingStacks;
  
     private FloralisiaIngredient(Stream<? extends FloralisiaIngredient.Entry> stream_1) {
        this.entries = (FloralisiaIngredient.Entry[])stream_1.filter(NON_EMPTY).toArray((int_1) -> {
           return new FloralisiaIngredient.Entry[int_1];
        });
    }
  
     private void cacheMatchingStacks() {
        if (this.matchingStacks == null) {
           this.matchingStacks = (ItemStack[])Arrays.stream(this.entries).flatMap((ingredient$Entry_1) -> {
              return ingredient$Entry_1.getStacks().stream();
           }).distinct().toArray((int_1) -> {
              return new ItemStack[int_1];
           });
        }
    }
  
    public boolean test(ItemStack stack) {
        if (stack == null) {
           return false;
        } else if (this.entries.length == 0) {
           return stack.isEmpty();
        } else {
           this.cacheMatchingStacks();
           ItemStack[] stacks = this.matchingStacks;
           for(int i = 0; i < stacks.length; i++) {
                ItemStack entry = stacks[i];
                if (entry.getItem() == stack.getItem() && stack.getCount() >= entry.getCount()) {
                    return true;
                }
           }
           return false;
        }
    }

    public int getInputCount(ItemStack stack) {
        this.cacheMatchingStacks();
        ItemStack[] stacks = this.matchingStacks;
        for(int i = 0; i < stacks.length; i++) {
            ItemStack entry = stacks[i];
            if (entry.getItem() == stack.getItem() && stack.getCount() >= entry.getCount()) {
                 return entry.getCount();
            }
        }
        return 0;
    }
  
    public boolean isEmpty() {
        return this.entries.length == 0 && (this.matchingStacks == null || this.matchingStacks.length == 0);
    }


    private static FloralisiaIngredient ofEntries(Stream<? extends FloralisiaIngredient.Entry> stream) {
        FloralisiaIngredient ingredient = new FloralisiaIngredient(stream);
        return ingredient.entries.length == 0 ? EMPTY : ingredient;
    }
  
    public static FloralisiaIngredient fromJson(JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                return ofEntries(Stream.of(entryFromJson(json.getAsJsonObject())));
            } else if (json.isJsonArray()) {
                JsonArray array = json.getAsJsonArray();
                if (array.size() == 0) {
                    throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                } else {
                    return ofEntries(StreamSupport.stream(array.spliterator(), false).map((element) -> {
                        return entryFromJson(JsonHelper.asObject(element, "item"));
                    }));
                }
            } else {
                throw new JsonSyntaxException("Expected item to be object or array of objects");
            }
        } else {
           throw new JsonSyntaxException("Item cannot be null");
        }
    }
  
    public static FloralisiaIngredient.Entry entryFromJson(JsonObject json) {
        if (json.has("item") && json.has("tag")) {
           throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
        } else {
            Identifier identifier_2;
            if (json.has("item")) {
                identifier_2 = new Identifier(JsonHelper.getString(json, "item"));
                Item item_1 = (Item)Registry.ITEM.getOrEmpty(identifier_2).orElseThrow(() -> {
                    return new JsonSyntaxException("Unknown item '" + identifier_2 + "'");
                });
                int count = 1;
                if (json.has("count")) {
                    count = JsonHelper.getInt(json, "count");
                }
                return new FloralisiaIngredient.StackEntry(new ItemStack(item_1, count));
            } else if (json.has("tag")) {
                identifier_2 = new Identifier(JsonHelper.getString(json, "tag"));
                Tag<Item> tag = ItemTags.getTagGroup().getTag(identifier_2);
                if (tag == null) {
                    throw new JsonSyntaxException("Unknown item tag '" + identifier_2 + "'");
                } else {
                    int count = 1;
                    if (json.has("count")) {
                        count = JsonHelper.getInt(json, "count");
                    }
                    return new FloralisiaIngredient.TagEntry(tag, count);
                }
            } else {
                throw new JsonParseException("An ingredient entry needs either a tag or an item");
            }
        }
    }
  
    public static class TagEntry implements FloralisiaIngredient.Entry {
        private final Tag<Item> tag;
        private int count;
  
        private TagEntry(Tag<Item> tag, int count) {
            this.tag = tag;
            this.count = count;
        }
  
        public Collection<ItemStack> getStacks() {
            List<ItemStack> list = Lists.newArrayList();
            Iterator<Item> iterator = this.tag.values().iterator();
    
            while (iterator.hasNext()) {
                Item item = iterator.next();
                list.add(new ItemStack(item, count));
            }
    
            return list;
        }
    }
  
    static class StackEntry implements FloralisiaIngredient.Entry {
        private final ItemStack stack;
  
        private StackEntry(ItemStack stack) {
            this.stack = stack;
        }
  
        public Collection<ItemStack> getStacks() {
            return Collections.singleton(this.stack);
        }
    }
  
    public interface Entry {
        Collection<ItemStack> getStacks();
    }
}
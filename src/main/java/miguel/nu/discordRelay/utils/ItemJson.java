package miguel.nu.discordRelay.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class ItemJson {
    public static JsonObject getItem(int i, ItemStack item){
        JsonObject itemJson = new JsonObject();
        itemJson.addProperty("slot", i);
        itemJson.addProperty("type", item.getType().name());
        itemJson.addProperty("amount", item.getAmount());

        ItemMeta meta = item.getItemMeta();
        if(meta instanceof Damageable){
            Damageable damageable = (Damageable) meta;
            int maxDurability = item.getType().getMaxDurability();
            int used = damageable.getDamage();
            int remaining = maxDurability - used;
            itemJson.addProperty("durability", remaining);
            itemJson.addProperty("max_durability", maxDurability);
        }

        JsonArray enchantsArray = new JsonArray();
        Map<Enchantment, Integer> enchantments = meta.getEnchants();
        for(Enchantment enchantment : enchantments.keySet()){
            int level = enchantments.get(enchantment);

            JsonObject enchantJson = new JsonObject();
            itemJson.addProperty("name", enchantment.getKey().getKey());
            itemJson.addProperty("level", level);

            enchantsArray.add(enchantJson);
        }
        itemJson.add("enchants", enchantsArray);
        return itemJson;
    }
}

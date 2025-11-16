package miguel.nu.discordRelay.API;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import miguel.nu.discordRelay.Main;
import miguel.nu.discordRelay.http.RequestHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class DiscordAPI {
    public static void sendModLog(OfflinePlayer victim, String type, String reason, long duration, OfflinePlayer responsible){

    }
    public static void sendChatFlagLog(OfflinePlayer victim, String message){
        JsonObject json = new JsonObject();
        json.addProperty("key", Main.config.getString("secret"));
        json.addProperty("victim", victim.getName());
        json.addProperty("message", message);

        String requestBody = new Gson().toJson(json);

        RequestHandler.sendPost("api/log/whisper", requestBody);
    }
    public static void sendWhisperLog(OfflinePlayer victim, OfflinePlayer responsible, String message){
        JsonObject json = new JsonObject();
        json.addProperty("key", Main.config.getString("secret"));
        json.addProperty("victim", victim.getName());
        json.addProperty("responsible", responsible.getName());
        json.addProperty("message", message);

        String requestBody = new Gson().toJson(json);

        RequestHandler.sendPost("api/log/whisper", requestBody);
    }
    public static void sendDeathLog(OfflinePlayer victim, String message, Location location, Inventory inventory){
        JsonObject json = new JsonObject();
        json.addProperty("key", Main.config.getString("secret"));
        json.addProperty("victim", victim.getName());
        json.addProperty("message", message);

        JsonObject locationJson = new JsonObject();
        json.addProperty("world", location.getWorld().getName());
        json.addProperty("x", location.getX());
        json.addProperty("y", location.getY());
        json.addProperty("z", location.getZ());

        json.add("location", locationJson);

        JsonArray itemsArray = new JsonArray();
        for(int i = 0; i < inventory.getSize(); i++){
            ItemStack item = inventory.getItem(i);
            if(item == null || item.getType() == Material.AIR) continue;
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
            itemsArray.add(itemJson);
        }
        json.add("inventory", itemsArray);
        String requestBody = new Gson().toJson(json);

        RequestHandler.sendPost("api/log/whisper", requestBody);
    }
}

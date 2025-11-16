package miguel.nu.discordRelay.API;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import miguel.nu.discordRelay.Main;
import miguel.nu.discordRelay.http.RequestHandler;
import miguel.nu.discordRelay.utils.ItemJson;
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

        RequestHandler.sendPost("api/log/chat_flag", requestBody);
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
    public static void sendDeathLog(Player victim, String message, Location location){
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
        for(int i = 0; i < victim.getInventory().getSize(); i++){
            ItemStack item = victim.getInventory().getItem(i);
            if(item == null || item.getType() == Material.AIR) continue;

            itemsArray.add(ItemJson.getItem(i + 1, item));
        }
        if (victim.getInventory().getItemInOffHand().getType() != Material.AIR){
            ItemStack item = victim.getInventory().getItemInOffHand();

            itemsArray.add(ItemJson.getItem(37, item));
        }
        if (victim.getInventory().getHelmet() != null){
            ItemStack item = victim.getInventory().getHelmet();

            itemsArray.add(ItemJson.getItem(38, item));
        }
        if (victim.getInventory().getChestplate() != null){
            ItemStack item = victim.getInventory().getChestplate();

            itemsArray.add(ItemJson.getItem(39, item));
        }
        if (victim.getInventory().getLeggings() != null){
            ItemStack item = victim.getInventory().getLeggings();

            itemsArray.add(ItemJson.getItem(40, item));
        }
        if (victim.getInventory().getBoots() != null){
            ItemStack item = victim.getInventory().getBoots();

            itemsArray.add(ItemJson.getItem(41, item));
        }
        json.add("inventory", itemsArray);
        String requestBody = new Gson().toJson(json);

        RequestHandler.sendPost("api/log/death/", requestBody);
    }
}

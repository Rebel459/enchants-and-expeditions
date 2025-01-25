package net.legacy.enchants_and_expeditions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class EaEConfig {

    public static boolean rebalanced_vanilla_enchants;
    public static boolean mod_integration_datapacks;

    public EaEConfig() {
    }

    public static void main() throws IOException {
        Path configPath = Path.of(FabricLoader.getInstance().getConfigDir().toString(), "enchants-and-expeditions-config-v1.json");

        try {
            JsonObject obj;
            if (configPath.toFile().createNewFile()) {
                obj = getJsonObject();
                PrintWriter pw = new PrintWriter(configPath.toString());
                Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
                pw.print(gson.toJson(obj));
                pw.flush();
                pw.close();
            }

            obj = (JsonObject)JsonParser.parseReader(new FileReader(configPath.toString()));
            JsonObject config = (JsonObject)obj.get("config");
            rebalanced_vanilla_enchants = config.get("rebalanced_vanilla_enchants").getAsBoolean();
            mod_integration_datapacks = config.get("mod_integration_datapacks").getAsBoolean();
        } catch (IOException var4) {
            System.err.println("An error occurred, delete the enchants and expeditions config file in your config/ folder and relaunch");
        }

    }

    private static JsonObject getJsonObject() {
        JsonObject jsonObjects = new JsonObject();
        JsonObject configObject = new JsonObject();
        configObject.addProperty("rebalanced_vanilla_enchants", true);
        configObject.addProperty("mod_integration_datapacks", true);
        jsonObjects.add("config", configObject);
        return jsonObjects;
    }
}

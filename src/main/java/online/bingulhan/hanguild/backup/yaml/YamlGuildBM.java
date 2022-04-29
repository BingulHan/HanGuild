package online.bingulhan.hanguild.backup.yaml;

import online.bingulhan.hanguild.Guild;
import online.bingulhan.hanguild.HanGuild;
import online.bingulhan.hanguild.backup.IGuildBackupManager;
import online.bingulhan.hanguild.util.FileUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class YamlGuildBM implements IGuildBackupManager {


    public File folder = new File(HanGuild.getInstance().getDataFolder().getPath(), "guilds");

    public YamlGuildBM() {
        if (!folder.exists()) {

            folder.mkdir();

        }
    }
    @Override
    public void backup(Guild guild) {
        File file = new File(folder.getPath(), "guild"+System.currentTimeMillis()+".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileConfiguration c = YamlConfiguration.loadConfiguration(file);

        c.set("name", guild.guildName);
        c.set("owner", guild.ownerName);
        c.set("users", guild.userList);
        c.set("access", guild.access);

        try {
            c.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public ArrayList<Guild> load() {

        ArrayList<Guild> guilds = new ArrayList<>();

        for (File x : folder.listFiles()) {

                FileConfiguration c = YamlConfiguration.loadConfiguration(x);

                String ownerName = c.getString("owner");
                ArrayList<String> userList = (ArrayList<String>) c.get("users");
                String guildName = c.getString("name");
                boolean access = c.getBoolean("access");


                Guild guild = new Guild(guildName, ownerName, userList, access);
                guilds.add(guild);

                FileUtil.delete(x);



        }

        return guilds;
    }
}

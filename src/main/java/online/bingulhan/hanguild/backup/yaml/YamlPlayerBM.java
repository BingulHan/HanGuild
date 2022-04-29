package online.bingulhan.hanguild.backup.yaml;

import online.bingulhan.hanguild.HanGuild;
import online.bingulhan.hanguild.PlayerGuildAccount;
import online.bingulhan.hanguild.backup.IPlayerBackupManager;
import online.bingulhan.hanguild.bll.GuildManager;
import online.bingulhan.hanguild.util.FileUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlPlayerBM implements IPlayerBackupManager {


    public File folder = new File(HanGuild.getInstance().getDataFolder().getPath(), "players");

    public YamlPlayerBM() {
        if (!folder.exists()) {

            folder.mkdir();

        }
    }


    @Override
    public void backup(PlayerGuildAccount account) {

        if (account.guildName==null) return;

        File file = new File(folder.getPath(), "accounts"+System.currentTimeMillis()+".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileConfiguration c = YamlConfiguration.loadConfiguration(file);

        c.set("name", account.playerName);
        c.set("guild", account.guildName);


        try {
            c.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public PlayerGuildAccount getBackup(String playerName) {
        for (File x: folder.listFiles()) {
            FileConfiguration c = YamlConfiguration.loadConfiguration(x);
            String name = c.getString("name");
            if (name.equals(playerName)) {
                String guild = c.getString("guild");
                PlayerGuildAccount account = new PlayerGuildAccount(name, guild);
                FileUtil.delete(x);

                if (new GuildManager().getGuild(guild)!=null) {
                    return account;
                }

                return new PlayerGuildAccount(playerName);

            }
        }
        return new PlayerGuildAccount(playerName);
    }
}

package online.bingulhan.hanguild.bll;

import online.bingulhan.hanguild.Guild;
import online.bingulhan.hanguild.HanGuild;
import online.bingulhan.hanguild.PlayerGuildAccount;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GuildManager {

    public GuildManager() {}

    public void createGuild(Player owner, String guildName) {
        if (HanGuild.getInstance().economyDepend.hasMoney(owner.getPlayer(), HanGuild.getInstance().getConfig().getDouble("req"))) {
            if (getGuild(guildName)==null) {
                ArrayList<String> ul = new ArrayList<>();
                ul.add(owner.getName());

                Guild guild = new Guild(guildName, owner.getName(), ul, false);

                HanGuild.getPlugin(HanGuild.class).guilds.add(guild);
                owner.sendMessage(ChatColor.GREEN+guildName+" Loncası kuruldu.");

                HanGuild.getInstance().economyDepend.withdraw(owner.getPlayer(), HanGuild.getInstance().getConfig().getDouble("req"));


                new GuildPlayerManager().getAccount(owner.getPlayer()).guildName=guildName;

            }
        }else {
            owner.sendMessage(ChatColor.RED+"Yeterli paran yok.");
        }

    }

    public Guild getGuild(String guildName) {
        for (Guild guild : HanGuild.getInstance().guilds) {
            if (guild.guildName.equals(guildName)) {
                return guild;
            }
        }
        return null;
    }

    public void removeGuild(String guildName) {

        Guild guild = getGuild(guildName);

        for (String l : guild.userList) {

            OfflinePlayer c = HanGuild.getInstance().getServer().getOfflinePlayer(l);

            if (!c.isOnline()) {
                PlayerGuildAccount a = HanGuild.getInstance().playerBackupManager.getBackup(c.getName());
                a.guildName=null;
                HanGuild.getInstance().playerBackupManager.backup(a);


            }else {

                PlayerGuildAccount a = new GuildPlayerManager().getAccount(c);
                c.getPlayer().sendMessage(ChatColor.RED+"Klan silindi bu yüzden artık loncada değilsin.");
                a.guildName=null;

            }
        }

        HanGuild.getInstance().guilds.remove(guild);

    }
}

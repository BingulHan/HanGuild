package online.bingulhan.hanguild.listeners;

import online.bingulhan.hanguild.HanGuild;
import online.bingulhan.hanguild.PlayerGuildAccount;
import online.bingulhan.hanguild.bll.GuildPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {

        PlayerGuildAccount account = HanGuild.getInstance().playerBackupManager.getBackup(e.getPlayer().getName());
        HanGuild.getInstance().accounts.add(account);



    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {

        PlayerGuildAccount account = new GuildPlayerManager().getAccount(e.getPlayer());
        HanGuild.getInstance().playerBackupManager.backup(account);

        HanGuild.getInstance().getServer().getScheduler().runTaskLater(HanGuild.getInstance(), () -> {

            HanGuild.getInstance().accounts.remove(account);

        }, 2);




    }

}

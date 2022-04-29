package online.bingulhan.hanguild;

import online.bingulhan.hanguild.backup.IGuildBackupManager;
import online.bingulhan.hanguild.backup.IPlayerBackupManager;
import online.bingulhan.hanguild.backup.yaml.YamlGuildBM;
import online.bingulhan.hanguild.backup.yaml.YamlPlayerBM;
import online.bingulhan.hanguild.bll.GuildPlayerManager;
import online.bingulhan.hanguild.cmd.CMDGuild;
import online.bingulhan.hanguild.economy.IEconomyDepend;
import online.bingulhan.hanguild.economy.VaultEconomy;
import online.bingulhan.hanguild.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class HanGuild extends JavaPlugin {

    public ArrayList<Guild> guilds = new ArrayList<>();
    public ArrayList<PlayerGuildAccount> accounts = new ArrayList<>();

    private static HanGuild instance;

    public IGuildBackupManager guildBackupManager;
    public IPlayerBackupManager playerBackupManager;
    public IEconomyDepend economyDepend;

    public static GuildPlayerManager guildPlayerManager = new GuildPlayerManager();




    @Override
    public void onEnable() {
        instance=this;

        getConfig().options().copyDefaults(true);
        saveConfig();


        //Yaml Alınmıştır siz isterseniz Mysql vb destekler sağlayabilirsiniz.
        guildBackupManager=new YamlGuildBM();
        playerBackupManager=new YamlPlayerBM();


        //Vault Depend alımmıştır siz düzenleyebilirsiniz.

        economyDepend=new VaultEconomy();

        registerListeners();


        guilds=guildBackupManager.load();


        getCommand("guild").setExecutor(new CMDGuild());
        getCommand("lonca").setExecutor(new CMDGuild());
        getCommand("klan").setExecutor(new CMDGuild());
        getCommand("clan").setExecutor(new CMDGuild());


        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void event(InventoryClickEvent e) {
                if (e.getWhoClicked().getOpenInventory().getTitle().equals(ChatColor.RED+"Lonca Üyeleri")) e.setCancelled(true);
            }
        }, this);

        for (Player player : getServer().getOnlinePlayers()) {
            PlayerGuildAccount account = HanGuild.getInstance().playerBackupManager.getBackup(player.getName());
            HanGuild.getInstance().accounts.add(account);
        }

        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            //Registering placeholder will be use here
            new GuildHook().register();
        }

    }

    @Override
    public void onDisable() {

        for (Guild guild : guilds) {
            guildBackupManager.backup(guild);
        }

        for (PlayerGuildAccount account :accounts) {
            playerBackupManager.backup(account);
        }



    }

    public static HanGuild getInstance() {
        return instance;

    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }
}

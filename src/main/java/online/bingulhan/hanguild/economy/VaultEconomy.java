package online.bingulhan.hanguild.economy;

import net.milkbowl.vault.economy.Economy;
import online.bingulhan.hanguild.HanGuild;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEconomy implements IEconomyDepend {

    private static Economy econ = null;

    public VaultEconomy() {
        if (HanGuild.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
           HanGuild.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED+"HanGuild - Vault Yuklenemedi bu yuzden eklenti devre disi");
           HanGuild.getInstance().getServer().getPluginManager().disablePlugin(HanGuild.getInstance());
        }
        RegisteredServiceProvider<Economy> rsp = HanGuild.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            HanGuild.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED+"HanGuild - Vault Yuklenemedi bu yuzden eklenti devre disi");
            HanGuild.getInstance().getServer().getPluginManager().disablePlugin(HanGuild.getInstance());
        }
        econ = rsp.getProvider();
    }

    @Override
    public void withdraw(OfflinePlayer player, Double money) {

        econ.withdrawPlayer(player, money);

    }

    @Override
    public void deposit(OfflinePlayer player, Double money) {

        econ.depositPlayer(player, money);
    }

    @Override
    public boolean hasMoney(OfflinePlayer player, Double money) {

        if (econ.getBalance(player)>=money) {

            return true;

        }
        return false;
    }
}

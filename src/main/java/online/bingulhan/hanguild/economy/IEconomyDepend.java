package online.bingulhan.hanguild.economy;

import org.bukkit.OfflinePlayer;

public interface IEconomyDepend {

    public void withdraw(OfflinePlayer player, Double money);
    public void deposit(OfflinePlayer player, Double money);
    public boolean hasMoney(OfflinePlayer player, Double money);

}

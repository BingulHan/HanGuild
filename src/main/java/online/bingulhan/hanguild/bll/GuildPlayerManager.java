package online.bingulhan.hanguild.bll;

import com.cryptomorin.xseries.XMaterial;
import online.bingulhan.hanguild.Guild;
import online.bingulhan.hanguild.GuildInvite;
import online.bingulhan.hanguild.HanGuild;
import online.bingulhan.hanguild.PlayerGuildAccount;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class GuildPlayerManager {


    public boolean isJoinedPlayerGuild(OfflinePlayer player) {



        return false;
    }

    public PlayerGuildAccount getAccount(OfflinePlayer player) {

        for (PlayerGuildAccount account : HanGuild.getInstance().accounts) {
            if (account.playerName.equals(player.getName())) {
                return account;

            }
        }
        return null;
    }

    public boolean isInvitedPlayer(OfflinePlayer reciver, Guild guild) {

        PlayerGuildAccount account = getAccount(reciver);

        for (GuildInvite invite : account.invites) {
            if (invite.guild.guildName.equals(guild.guildName)) {
                return true;
            }
        }


        return false;


    }

    public void invitePlayerGuild(OfflinePlayer reciver, Guild guild) {


        GuildInvite invite = new GuildInvite(reciver.getName(), guild);

        getAccount(reciver).invites.add(invite);
        reciver.getPlayer().sendMessage(ChatColor.GREEN+""+guild.ownerName+" Lonca daveti gönderdi katılmak için /lonca katıl "+guild.guildName+" ");

        HanGuild.getInstance().getServer().getScheduler().runTaskLater(HanGuild.getInstance(), () -> {

            if (isInvitedPlayer(reciver, guild)) {
                getAccount(reciver).invites.remove(invite);
                reciver.getPlayer().sendMessage(ChatColor.RED+"Klan davetini kabul etmediğinizden iptal edildi.");
            }

        }, 20*30);

    }

    public void kickGuildToPlayer(OfflinePlayer kicked, Guild guild) {

        guild.userList.remove(kicked.getName());

        for (String i : guild.userList) {
            OfflinePlayer d = HanGuild.getInstance().getServer().getOfflinePlayer(i);
            if (d.isOnline()) {
                d.getPlayer().sendMessage(ChatColor.RED+""+kicked.getName()+" Loncadan atıldı.");
            }
        }


    }

    public void joinGuild(OfflinePlayer reciver, Guild guild) {
        if (isInvitedPlayer(reciver, guild)) {


            getAccount(reciver).guildName=guild.guildName;
            guild.userList.add(reciver.getName());
            reciver.getPlayer().sendMessage(ChatColor.GREEN+"Başarıyla "+guild.guildName+" Loncasına katıldınız.");

            try {
                for (GuildInvite x: getAccount(reciver).invites) {
                    if (x.guild.guildName.equals(guild.guildName)) {

                        getAccount(reciver).invites.remove(x);



                    }
                }

            }catch (Exception exception) {

            }

        }else {
            reciver.getPlayer().sendMessage(ChatColor.RED+""+guild.guildName+" Loncasına katılmak için davetiniz yok.");
        }
    }

    public void leaveGuild(OfflinePlayer player) {

        if (getAccount(player).guildName!=null) {

            try {
                Guild guild = new GuildManager().getGuild(getAccount(player).guildName);
                guild.userList.remove(player.getName());
                getAccount(player).guildName=null;

                for (String i : guild.userList) {
                    OfflinePlayer d = HanGuild.getInstance().getServer().getOfflinePlayer(i);
                    if (d.isOnline()) {
                        d.getPlayer().sendMessage(ChatColor.RED+""+player.getName()+" Loncadan ayrıldı.");
                    }
                }

                player.getPlayer().sendMessage(ChatColor.RED+"Loncadan ayrıldın.");

            }catch (Exception exception) {

            }


        }else {
            player.getPlayer().sendMessage(ChatColor.RED+"Bir loncada bulunmuyorsun.");
        }

    }


    public void openGuildUserList(OfflinePlayer player) {
        if (getAccount(player).guildName!=null) {
            GuildManager guildManager = new GuildManager();
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.RED+"Lonca Üyeleri");
            Guild guild = guildManager.getGuild(getAccount(player).guildName);

            for (String user : guild.userList) {

                if (guild.userList.indexOf(user)<27) {
                    ItemStack item = new ItemStack(XMaterial.PLAYER_HEAD.parseItem());
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    meta.setOwner(user);
                    OfflinePlayer p = HanGuild.getInstance().getServer().getOfflinePlayer(user);
                    if (p.isOnline()) {
                        meta.setDisplayName(ChatColor.GREEN+""+user);
                    }else {
                        meta.setDisplayName(ChatColor.RED+""+user);
                    }
                    if (guild.ownerName.equals(user)) {
                        ArrayList<String> lore = new ArrayList<>();
                        lore.add(" ");
                        lore.add(ChatColor.RED+"Lonca lideri");
                        lore.add("  ");

                        meta.setLore(lore);
                    }
                    item.setItemMeta(meta);
                    inventory.addItem(item);
                }

            }

            player.getPlayer().closeInventory();

            HanGuild.getInstance().getServer().getScheduler().runTaskLater(HanGuild.getInstance(), () -> {
                player.getPlayer().openInventory(inventory);
            }, 2);

        }else {
            player.getPlayer().sendMessage(ChatColor.RED+"Bir loncan yok!");
        }
    }
}

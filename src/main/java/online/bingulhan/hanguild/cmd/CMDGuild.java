package online.bingulhan.hanguild.cmd;

import online.bingulhan.hanguild.Guild;
import online.bingulhan.hanguild.GuildInvite;
import online.bingulhan.hanguild.HanGuild;
import online.bingulhan.hanguild.PlayerGuildAccount;
import online.bingulhan.hanguild.bll.GuildManager;
import online.bingulhan.hanguild.bll.GuildPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CMDGuild implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            GuildPlayerManager guildPlayerManager = new GuildPlayerManager();
            PlayerGuildAccount account = guildPlayerManager.getAccount(player);


            if (args.length<1) {

                if (account.guildName!=null) {

                    Guild guild = new GuildManager().getGuild(account.guildName);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "  "));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Lonca ismi: &e"+account.guildName));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Lonca sahibi: &e"+guild.ownerName));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Üye Sayısı: &f&l"+guild.userList.size()));
                    if (guild.access) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Lonca Durumu: &aHerkes Açık"));
                    }else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Lonca Durumu: &CÖzel"));
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "  "));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lonca listele &fLoncadaki üyelere bakabilirsin."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "   "));

                    if (guild.ownerName.equals(player.getName())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lonca sil &fLoncayı silebilirsin."));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lonca davet <oyuncu adı> &fOyuncuyu loncaya davet edebilirsin"));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lonca at <oyuncu adı> &fOyuncuyu loncadan atabilirsin"));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "   "));
                    }else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/lonca ayrıl &fLoncadan ayrılabilirsin."));
                    }

                }else {
                    player.sendMessage(ChatColor.RED+"Bir loncada bulunmuyorsun.");
                    player.sendMessage(ChatColor.RED+"Bir loncadan davet aldıysan "+ChatColor.WHITE+"/lonca katıl <lonca-adı>");
                    player.sendMessage(ChatColor.RED+"Lonca davetlerine bakmak için "+ChatColor.WHITE+"/lonca davetler");

                }
            }else {
                if (args.length>0) {
                    if (args[0].equals("olustur")) {
                        if (account.guildName==null) {
                            if (args.length>1) {

                                String guildName = args[1];
                                new GuildManager().createGuild(player, guildName);

                            }

                        }else {
                            player.sendMessage(ChatColor.RED+"Zaten bir loncadasın.");
                        }
                    }

                    if (args[0].equals("at")) {

                        if (account.guildName!=null) {

                            Guild guild = new GuildManager().getGuild(account.guildName);
                            GuildPlayerManager playerManager = new GuildPlayerManager();
                            if (guild.ownerName.equals(player.getName())) {

                                if (args.length>1) {

                                    String target = args[1];

                                    if (target.equals(player.getName()))  {

                                        player.sendMessage(ChatColor.RED+"Kendini atamazsın");
                                        return true;

                                    }

                                    for (String t : guild.userList) {
                                        if (t.equals(target)) {

                                            OfflinePlayer c = HanGuild.getInstance().getServer().getOfflinePlayer(t);

                                            if (!c.isOnline()) {
                                                PlayerGuildAccount f = HanGuild.getInstance().playerBackupManager.getBackup(t);
                                                f.guildName=null;
                                                HanGuild.getInstance().playerBackupManager.backup(f);

                                                new GuildPlayerManager().kickGuildToPlayer(c, guild);
                                            }else {
                                                PlayerGuildAccount f = new GuildPlayerManager().getAccount(c);
                                                f.guildName=null;

                                                new GuildPlayerManager().kickGuildToPlayer(c, guild);
                                            }


                                            return true;
                                        }

                                    }

                                    player.sendMessage(ChatColor.RED+"Bu oyuncu klanda bulunmuyor.");


                                }else {
                                    player.sendMessage(ChatColor.RED+"Bir oyuncu adı giriniz.");
                                }

                            }else {
                                player.sendMessage(ChatColor.RED+"Bu komutu sadece lonca sahipleri kullanabilir.");
                            }

                        }else {
                            player.sendMessage(ChatColor.RED+"Bir loncada değilsin.");
                        }
                    }

                    if (args[0].equals("davetler")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Davetler"));
                        PlayerGuildAccount ac = new GuildPlayerManager().getAccount(player);

                        for (GuildInvite invite : ac.invites) {

                            int id = ac.invites.indexOf(invite)+1;
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f"+id+"&f. Lonca: &e"+invite.guild.guildName));


                        }

                    }

                    if (args[0].equals("ayrıl")) {

                        new GuildPlayerManager().leaveGuild(player);
                    }

                    if (args[0].equals("listele")) {

                        new GuildPlayerManager().openGuildUserList(player);

                    }

                    if (args[0].equals("sil")) {

                        if (account.guildName!=null) {
                            Guild guild = new GuildManager().getGuild(account.guildName);
                            if (guild.ownerName.equals(player.getName())) {

                                new GuildManager().removeGuild(account.guildName);

                                player.sendMessage(ChatColor.GREEN+"Lonca başarıyla silindi.");

                            }else {
                                player.sendMessage(ChatColor.RED+"Bu komutu sadece lonca sahipleri kullanabilir.");
                            }

                        }
                    }

                    if (args[0].equals("davet")) {
                        if (account.guildName!=null) {
                            Guild guild = new GuildManager().getGuild(account.guildName);
                            if (guild.ownerName.equals(player.getName())) {
                                if (args.length>1) {
                                    String target = args[1];
                                    OfflinePlayer p = HanGuild.getInstance().getServer().getOfflinePlayer(target);
                                    if (p.isOnline()) {
                                        GuildPlayerManager gpm = new GuildPlayerManager();
                                        if (!gpm.isInvitedPlayer(p,guild)) {
                                            gpm.invitePlayerGuild(p, guild);
                                            player.sendMessage(ChatColor.GREEN+"Başarıyla klan daveti gönderildi.");

                                        }else {
                                            player.sendMessage(ChatColor.RED+"Zaten bir klan daveti göndermişsin.");
                                        }
                                    }else{
                                        player.sendMessage(ChatColor.RED+""+target+" Aktif değil.");
                                    }
                                }
                            }else {
                                player.sendMessage(ChatColor.RED+"Bu komutu sadece lonca sahipleri kullanabilir.");
                            }
                        }else {
                            sender.sendMessage(ChatColor.RED+"Bir loncada değilsin.");
                        }
                    }

                    if (args[0].equals("katıl")) {

                        if (new GuildPlayerManager().getAccount(player).guildName==null) {
                            if (args.length>1) {
                                String guildName = args[1];
                                if (new GuildManager().getGuild(guildName)!=null) {

                                    Guild guild = new GuildManager().getGuild(guildName);
                                    new GuildPlayerManager().joinGuild(player, guild);




                                }else {
                                    player.sendMessage(ChatColor.RED+"Bu isim de bir lonca yok.");
                                }


                            }
                        }else {
                            player.sendMessage(ChatColor.RED+"Zaten bir loncada varsın.");
                        }

                    }
                }
            }


        }


        return true;
    }
}

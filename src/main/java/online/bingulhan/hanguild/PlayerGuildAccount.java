package online.bingulhan.hanguild;

import java.util.ArrayList;

public class PlayerGuildAccount {


    public String playerName;
    public String guildName;

    public ArrayList<GuildInvite> invites = new ArrayList<>();

    public PlayerGuildAccount(String playerName, String guildName) {

        this.playerName=playerName;
        this.guildName=guildName;

    }

    public PlayerGuildAccount(String playerName) {

        this.playerName=playerName;
        this.guildName=null;

    }
}

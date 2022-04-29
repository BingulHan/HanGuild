package online.bingulhan.hanguild;

import java.util.ArrayList;

public class Guild {

    public String ownerName;
    public String guildName;
    public boolean access;

    public ArrayList<String> userList;

    public Guild(String guildName, String ownerName, ArrayList<String> userList, boolean access) {

        this.ownerName=ownerName;
        this.guildName=guildName;
        this.userList=userList;
        this.access=access;

    }


}

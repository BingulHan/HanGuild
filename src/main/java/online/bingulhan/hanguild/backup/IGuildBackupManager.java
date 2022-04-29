package online.bingulhan.hanguild.backup;

import online.bingulhan.hanguild.Guild;

import java.util.ArrayList;

public interface IGuildBackupManager {

    public void backup(Guild guild);

    public ArrayList<Guild> load();


}

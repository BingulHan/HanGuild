package online.bingulhan.hanguild.backup;

import online.bingulhan.hanguild.PlayerGuildAccount;

public interface IPlayerBackupManager
{

    public void backup(PlayerGuildAccount account);
    public PlayerGuildAccount getBackup(String playerName);

}

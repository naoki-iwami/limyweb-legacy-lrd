package org.limy.lrd.common;

import java.util.Collection;
import java.util.Date;

import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.bean.LrdLightBean;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;

/**
 * 空のLrdリポジトリクラスです。ダミー用
 * @author Naoki Iwami
 */
public class EmptyLrdRepository implements LrdRepository {

    public void addAndcommitMultiFile(CommitFileInfo[] commitInfos)
            throws LrdException {
        // TODO Auto-generated method stub

    }

    public void addDirectory(String lrdPath) throws LrdException {
        // TODO Auto-generated method stub

    }

    public void addFile(String lrdPath) throws LrdException {
        // TODO Auto-generated method stub

    }

    public void commitMultiFile(CommitFileInfo[] commitInfos)
            throws LrdException {
        // TODO Auto-generated method stub

    }

    public String getAllMenu(LrdWriter writer) throws LrdException {
        // TODO Auto-generated method stub
        return null;
    }

    public LrdNode getDirectoryRoot() {
        return new LrdNode(new LrdPath(null, ""), false);
    }

    public LrdPath[] getFilesFromDirectory(String repositoryUrl)
            throws LrdException {
        // TODO Auto-generated method stub
        return null;
    }

    public CommitFileInfo getRepositoryFile(String lrdPath) throws LrdException {
        // TODO Auto-generated method stub
        return null;
    }

    public CommitFileInfo[] getRepositoryFiles(String[] lrdPaths)
            throws LrdException {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getTimestamp(String lrdPath, String authUser, String authPass) {
        return new Date();
    }

    public boolean isDirectory(String repositoryUrl) throws LrdException {
        // TODO Auto-generated method stub
        return false;
    }

    public Collection<CommitFileInfo> getRecentUpdateFiles(int count) throws LrdException {
        // TODO Auto-generated method stub
        return null;
    }

}

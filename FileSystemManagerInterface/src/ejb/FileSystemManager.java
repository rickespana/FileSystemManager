
package ejb;

/**
 * 
 * @author rick_espana
 */

import filemanager.model.ClaimFS;
import filemanager.model.MetaDataStatistic;
import javax.ejb.Remote;

@Remote
public interface FileSystemManager {
   
    /* FS Metadata definitions */
    public boolean createUserFSMetaData(String _userId, float quota);
    public void deleteUserFSMetaData(String _userId);
    public boolean setUserFSMetadataQuota(String _userId, float new_quota);
    public MetaDataStatistic getFSMetadataStatistics(String _userId);
    
    /* FS claim collection definitions */
    public boolean createFSforClaim(String _userId, String _claim_id);
    public void saveClaimFStoDB();
    public String UploadFileforClaim
            (filemanager.model.FsClaimFile... file_objects) throws java.io.FileNotFoundException;

    public void loadClaimFS(java.lang.String _claimid);
    public String getClaimFSByteOccupation();
    public java.lang.String getClaimFSNumFiles();
    public java.lang.String getClaimFSId();
    
    public void claimFSChangeDirectory(int index_change);
    public java.lang.String claimFSgetWorkingPath();
    public java.lang.String claimFSgetRootContents();
    public java.lang.String claimFSgetActualDirContents();
    public void claimFScreateDir(java.lang.String dir_name);
    public void claimFSgoBackDir();
    public void claimFSrenameDirectory(int index_change, java.lang.String new_name);
    //TODO: think if it has to be permanent??
    public javax.swing.tree.DefaultMutableTreeNode getClaimFSTree();
    public void claimFSdeleteDir(int index_change);
    public void claimFSgoBackToRoot();
    
}

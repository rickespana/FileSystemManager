package filemanager.ejb;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import ejb.FileSystemManager;
import filemanager.model.ClaimFS;
import filemanager.model.FsClaimDirectory;
import filemanager.model.FsClaimFile;
import filemanager.model.FsClaimPermissionDocument;
import filemanager.model.MetaDataStatistic;
import filemanager.model.fsmetadata;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Timeout;
import javax.persistence.Cacheable;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


/**
 *
 * @author rick_espana
 */

@Stateful
@javax.ejb.StatefulTimeout(value=5L,unit= TimeUnit.MINUTES)
public class FileSystemManagerBean implements FileSystemManager, Serializable {
    
    
    @EJB
    private FileSystemResourceSingleton fs_resources;
    private ClaimFS claimFS;
    
    
    @PostConstruct
    private void setClaimFSResources(){
        this.claimFS = new ClaimFS();
    }
    @PrePassivate
    private void saveState(){
        System.out.println("going to Passivate");
        saveClaimFStoDB();
    }
    @PreDestroy
    private void destroyState(){
        System.out.println("Im about to be destroyed");
    }
    
    
    private void activateState(){
        System.out.println("Im about to be activated");
    }
       
    public boolean createUserFSMetaData(String _userId, float _quota) {
        
        DBCollection fsmetadata_coll = fs_resources.getDBInstance().
                                               getCollection("fsmetadata");        
        fsmetadata _fsmetadataInstance = new fsmetadata(_userId,_quota);
       
       try {
        fsmetadata_coll.insert(_fsmetadataInstance.toDBObject());
       } catch (MongoException.DuplicateKey dup){   
           Logger log_write = Logger.getLogger(FileSystemManagerBean.class.getName());
           log_write.log(Level.SEVERE,"Trying to add Duplicate Register", dup);           
           return false;
       }
       
       return true;
    }
    
    public void deleteUserFSMetaData(String _userId) { 
        DBCollection fsmetadata_coll = fs_resources.getDBInstance().
                                               getCollection("fsmetadata");
        fsmetadata_coll.remove(new BasicDBObject("_id", _userId));    
      
    }
    public boolean setUserFSMetadataQuota(String _userId, float new_quota) { 
        DBCollection fsmetadata_coll = fs_resources.getDBInstance().
                                               getCollection("fsmetadata");
         
        BasicDBObject quota_updt = new BasicDBObject("$set",
                                        new BasicDBObject("quota", new_quota));
        fsmetadata_coll.update(new BasicDBObject("_id",_userId),
                               quota_updt);
        return true;
     
    }
    
    public MetaDataStatistic getFSMetadataStatistics(String _userId){
        
        DBCollection fsmetadata_coll = fs_resources.getDBInstance().
                                               getCollection("fsmetadata");
         
        BasicDBObject _match = new BasicDBObject("$match",
                                            new BasicDBObject("_id",_userId));
        
        BasicDBObject _project_fields = new BasicDBObject();
        _project_fields.put("_id",0);
        _project_fields.put("used_quota",1);
        _project_fields.put("number_of_files",1);
        _project_fields.put("quota",1);
        
        List _mutiply_list = Arrays.asList(100,BasicDBObjectBuilder.start()
                          .add("$divide",Arrays.asList("$used_quota","$quota")).get()); 
        
        _project_fields.put("perc_used_quota", BasicDBObjectBuilder.start()
                                               .add("$multiply", _mutiply_list).get());
        
        BasicDBObject _project = new BasicDBObject("$project",_project_fields);
        
        Iterable<DBObject> _stat =  fsmetadata_coll.aggregate(_match,_project).results();
        return new MetaDataStatistic(_stat.iterator().next());
        
    }

    
    public boolean createFSforClaim(String _userId, String _claim_id) {
    
        DBCollection fs_claim_coll = fs_resources.getDBInstance().
                                              createCollection("fs_claim_"+
                                                                _claim_id,null);
        
        //TODO: Permission document
        fs_claim_coll.insert(new FsClaimPermissionDocument(new Vector()));
        ClaimFS claim_new = new ClaimFS(1);
        fs_claim_coll.insert(claim_new);
        
        DBCollection fsmetadata_coll = fs_resources.getDBInstance().
                                               getCollection("fsmetadata");
        
        // Update fsmetadata, add new claim to list and increment quota usage
        //TODO: get empty filesystem quota from properties
        fsmetadata_coll.update(new BasicDBObject("_id",_userId),
                               BasicDBObjectBuilder.start()
                               .add("$push",new BasicDBObject("fs_claims",_claim_id))
                               .add("$inc",new BasicDBObject("used_quota",0.8)).get());
        //Change actual claim fs to newly created space
        this.claimFS =(ClaimFS) claim_new.clone();
        this.claimFS.setClaim_id(_claim_id);
        this.claimFS.setPath_in(this.claimFS.getFs_tree().getPath());
        this.claimFS.setNode_in((DefaultMutableTreeNode)this.claimFS.getFs_tree().getRoot());
        return true;  
    }

    public String getClaimFSId(){
         return this.claimFS.getClaim_id();
    }
    
    public void loadClaimFS(String _claimid){
         DBCollection claimFS_coll = fs_resources.getDBInstance().
                                               getCollection("fs_claim_"+_claimid);
         //TODO: What if it doesn't find any?
         //TODO: load permissions
         DBObject db_obj = claimFS_coll.findOne(new BasicDBObject("_id","tree"));         
         this.claimFS = ClaimFS.fromDBObject(db_obj);
         this.claimFS.setClaim_id(_claimid);
    }
    
    //saves _claim fs to db with its changes
    // TODO: save ClaimFS state on destruction?
    
    public void saveClaimFStoDB(){
       
        //TODO? update fsmetadata and FSCLAIM parameters
        System.out.println("Im saving!");
        DBCollection fsclaim_coll = fs_resources.getDBInstance().
                                            getCollection("fs_claim_"+this.claimFS.getClaim_id());
        
        try{
            fsclaim_coll.update(new BasicDBObject("_id","tree"),
                                 new BasicDBObject("$set",this.claimFS.toDBObject()));
            
        }catch (MongoException mongoex){
             Logger log_write = Logger.getLogger(FileSystemManagerBean.class.getName());
             log_write.log(Level.SEVERE,"Cannot save claim to DB",mongoex);           
        }catch (Exception e){
            Logger log_write = Logger.getLogger(FileSystemManagerBean.class.getName());
            log_write.log(Level.SEVERE,e.getMessage(),e);
            e.printStackTrace();
        }
        System.out.println("Saved");
    }
    
    public String UploadFileforClaim(FsClaimFile... file_objects) 
                                                        throws FileNotFoundException{
        GridFS myFS = new GridFS(fs_resources.getDBInstance());
        File f_add;
        DBCollection fsclaim_coll = fs_resources.getDBInstance().
                                            getCollection("fs_claim_"+this.claimFS.getClaim_id());
        
        for(FsClaimFile fsc : file_objects){
      
           f_add = new File(fsc.getFile_path());
           if(f_add.exists()){ 
               try {
                    myFS.createFile(f_add).save(); // save to gridFS
                    // updates tree with new node as leaf
                    this.claimFS.getNode_in().add(new DefaultMutableTreeNode(fsc,false));
                    // delete tmp file
                } catch (IOException ex) {
                    Logger.getLogger(FileSystemManagerBean.class.getName())
                                                  .log(Level.SEVERE, null, ex);
                }
           } else{
               throw new FileNotFoundException(f_add.getAbsolutePath());
           }
        }
       return null;
    }
 /** Tree based operations ******/ 
    
    // updates node_in to root of fs_tree
    public void claimFSgoBackToRoot(){
        this.claimFS.setNode_in((DefaultMutableTreeNode)this.claimFS.getFs_tree().getRoot()); 
        this.claimFS.setPath_in(this.claimFS.getNode_in().getPath());
    }
    
    //deletes a whole directory with all children. Mantains actual dir
    public void claimFSdeleteDir(int index_change){
        DefaultMutableTreeNode node_to_erase = (DefaultMutableTreeNode)
                                            this.claimFS.getNode_in()
                                                        .getChildAt(index_change);
        this.claimFS.getNode_in().remove(node_to_erase);
    }
    
    //rename directory based on index from node_in. mantains node in and path
    public void claimFSrenameDirectory(int index_change, String new_name) {
        
        DefaultMutableTreeNode node_to_change = (DefaultMutableTreeNode)
                                            this.claimFS.getNode_in().getChildAt(index_change);
        FsClaimDirectory _dir = (FsClaimDirectory) node_to_change.getUserObject();
        _dir.setDir_name(new_name);
    }
    
    // updates node_in to previous directory set in path_in
    public void claimFSgoBackDir(){        
        
        TreeNode[] path_in = this.claimFS.getPath_in();
        this.claimFS.setNode_in((DefaultMutableTreeNode)path_in[path_in.length-2]);
        this.claimFS.setPath_in(Arrays.copyOf(path_in,path_in.length-1));
    }
    
    //Changes to directory based on child index of node_in attribute
    public void claimFSChangeDirectory(int index_change){
        DefaultMutableTreeNode _new_node_in;
        _new_node_in = (DefaultMutableTreeNode)
                                         this.claimFS.getNode_in().getChildAt(index_change);
        this.claimFS.setNode_in(_new_node_in);
        this.claimFS.setPath_in(_new_node_in.getPath());
    }
  
    // returns current working path, from root in string version
    public String claimFSgetWorkingPath(){
        
        StringBuilder readable_path = new StringBuilder();
        readable_path.append("/");
        TreeNode[] path_in = this.claimFS.getPath_in();
        for(int i = 1; i<path_in.length; i++){
            readable_path.append(path_in[i].toString());
            readable_path.append("/");
        }
        return readable_path.toString();
    }
   
    public String claimFSgetRootContents(){
        return this.claimFS.getJsonNodeDisplay(this.claimFS.getFs_tree().getRoot());
    }
    
    // Json Display from node_in node
    public String claimFSgetActualDirContents() {
        return this.claimFS.getJsonNodeDisplay(this.claimFS.getNode_in());
    }
    
    // creates a new directory as a child of node_in and changes to newly created dir
    public void claimFScreateDir(String dir_name){
       FsClaimDirectory new_dir = new FsClaimDirectory(dir_name);
       DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(new_dir,true); 
       this.claimFS.getNode_in().add(new_node);
       this.claimFS.setNode_in(new_node);
       this.claimFS.setPath_in(this.claimFS.getNode_in().getPath());
    }
 /*** Tree based operations ******/
    
    
 /***Claim definition query******/  
    
    // return claim byte occupation
    public String getClaimFSByteOccupation(){
        // return formated by locale
        Formatter formatter = new Formatter();
        //TODO: i18n locale?
        formatter.format(Locale.ENGLISH,"%1.2f",this.claimFS.getByte_ocuppation());
        return formatter.toString();
    }
    
    public String getClaimFSNumFiles(){
        return String.valueOf(this.claimFS.getNum_files());
    }
    
    public DefaultMutableTreeNode getClaimFSTree(){
        return this.claimFS.getFs_tree();
    }

}

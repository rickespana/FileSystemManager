package filemanager.model;
import java.io.Serializable;

/**
 *
 * @author rick_espana
 */
public class FsClaimDirectory implements Serializable {

    private String TYPE = "dir";
    private String dir_name;
   
    //TODO: Permission implementation
    
    public FsClaimDirectory() {}
    
    // TODO: define permission
    public FsClaimDirectory(String dir_name){
        this.dir_name = dir_name;
    }
                
    public String getDir_name() {
        return dir_name;
    }
    public String getType() {
        return TYPE;
    }

    public void setDir_name(String _dir_name) {
        this.dir_name = _dir_name;
    }
    
    @Override
    public String toString(){
        return this.dir_name;
    }
       
}

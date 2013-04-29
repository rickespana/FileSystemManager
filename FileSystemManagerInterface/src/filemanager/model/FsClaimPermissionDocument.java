
package filemanager.model;

import com.mongodb.BasicDBObject;
import java.util.Vector;

/**
 *
 * @author rick_espana
 */
public class FsClaimPermissionDocument extends BasicDBObject {
    
    private final String id = "permissions";
    private Vector<String> group;
    
   public FsClaimPermissionDocument() {}
   
   public FsClaimPermissionDocument(Vector _group){
       put("_id", this.id);
       put("group", _group);
   }

    public Vector<String> getGroup() {
        return group;
    }

    public void setGroup(Vector<String> group) {
        this.group = group;
    }
      
}

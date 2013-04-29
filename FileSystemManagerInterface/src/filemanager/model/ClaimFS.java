package filemanager.model;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


/**
 *
 * @author rick_espana
 */
public class ClaimFS extends BasicDBObject implements Serializable {

    private final String id = "tree";
    private DefaultMutableTreeNode fs_tree;
    private long num_files;
    private double byte_ocuppation;
    
    
    // helper attributes for tree path
    private DefaultMutableTreeNode node_in;
    private TreeNode[] path_in;
    private String claim_id;
    
    //TODO: add permission document elements
    
    // create new plain FSClaim structure
    public ClaimFS(int _flag) { 
    
        // init tree with default folders
        // root of tree
        fs_tree = new DefaultMutableTreeNode("/");
        
       
        FsClaimDirectory dir1 = new FsClaimDirectory("Reports");
        FsClaimDirectory dir2 = new FsClaimDirectory("Files");
         
        fs_tree.add(new DefaultMutableTreeNode(dir1,true));
        fs_tree.add(new DefaultMutableTreeNode(dir2,true));
        
        // serialize for db insert
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder xmlencoder = new XMLEncoder(baos);
        xmlencoder.writeObject(fs_tree);
        xmlencoder.flush();
        xmlencoder.close();
        
        put ("_id", id);
        put ("file_tree", baos.toByteArray());
        put ("num_files", 0L);
        put ("byte_occupation", 0D);
        
    }
    
    public ClaimFS(){}

    public static ClaimFS fromDBObject(DBObject doc){
      
        ClaimFS _claimFS = new ClaimFS();
        _claimFS.num_files =  (Long)doc.get("num_files");
        
        _claimFS.byte_ocuppation = (Double)doc.get("byte_occupation");
        
        ByteArrayInputStream bin_fs_tree = new ByteArrayInputStream
                                                ((byte[]) doc.get("file_tree"));
  
        XMLDecoder xmldeco = new XMLDecoder(bin_fs_tree);
        _claimFS.fs_tree = (DefaultMutableTreeNode)xmldeco.readObject();
        xmldeco.close();
       
       _claimFS.node_in = (DefaultMutableTreeNode) _claimFS.fs_tree.getRoot();
       _claimFS.path_in = _claimFS.node_in.getPath();
       return _claimFS;
    }
    
    public BasicDBObject toDBObject(){
        BasicDBObject doc = new BasicDBObject();
        
        //TODO: permissions attributes
              
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder xmlenco = new XMLEncoder(baos);
        xmlenco.writeObject(this.fs_tree);
        xmlenco.flush();
        byte[] towrite = baos.toByteArray();
        doc.put("file_tree",towrite); 
        doc.put("num_files",this.num_files);
        doc.put("byte_occupation",this.byte_ocuppation);
        return doc;
    }
    
    
    /* display node children and return json representation
     * 
     * type: node type (Directory or File)
     * name: display name
     * tree_index: index relative to node_in child array
     * 
     */
    public String getJsonNodeDisplay(TreeNode _node){
            
        Gson gson = new Gson();
        DefaultMutableTreeNode object_node;
     
        ArrayList output_arr = new ArrayList();
        HashMap output_hm = new HashMap();
        Enumeration child_enum = _node.children();
        
        while (child_enum.hasMoreElements()){
            object_node = (DefaultMutableTreeNode) child_enum.nextElement();
            output_hm.put("description",object_node.getUserObject());
            output_hm.put("tree_index", _node.getIndex(object_node));
            output_arr.add(output_hm.clone());   
        }
       return gson.toJson(output_arr);
    }
                    
    // creates a new file as leaf of the file tree
    public void createFile(String file_name) throws IOException{
        
       FsClaimFile new_file = new FsClaimFile(file_name);
       DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(new_file,false); 
       this.node_in.add(new_node);  
    }
    
   // DEBUG traverse whole tree in preorder putting indexes to children debug purposes
    public String walkTreePreOrder(){
        DefaultMutableTreeNode _root = (DefaultMutableTreeNode)
                                                this.fs_tree.getRoot();
        Enumeration pre_enum = _root.preorderEnumeration();
        DefaultMutableTreeNode _element_node;
        DefaultMutableTreeNode _element_parent;
        StringBuilder _s_tree_rep = new StringBuilder();
        
        pre_enum.nextElement();
        _s_tree_rep.append("ROOT");
        _s_tree_rep.append(System.getProperty("line.separator"));
        
        while(pre_enum.hasMoreElements()){
                
            _element_node = (DefaultMutableTreeNode) pre_enum.nextElement();
            _element_parent = (DefaultMutableTreeNode)_element_node.getParent();
            _s_tree_rep.append(String.format("%1$d.%2$d-%3$s%n",_element_parent.getLevel(),
                                                          _element_parent.getIndex(_element_node),
                                                          _element_node.getUserObject().toString()
                                            )
                              );
        }
        return _s_tree_rep.toString();
        
    }
    
    /************** Getters and Setters ***************************/
    public DefaultMutableTreeNode getFs_tree() {
        return fs_tree;
    }

     
    public void setFs_tree(DefaultMutableTreeNode fs_tree) {
        this.fs_tree = fs_tree;
    }

    public long getNum_files() {
        return num_files;
    }

    public void setNum_files(long num_files) {
        this.num_files = num_files;
    }

    public double getByte_ocuppation() {
        return byte_ocuppation;
    }

    public void setByte_ocuppation(double byte_ocuppation) {
        this.byte_ocuppation = byte_ocuppation;
    }

    public DefaultMutableTreeNode getNode_in() {
        return node_in;
    }

    public void setNode_in(DefaultMutableTreeNode node_in) {
        this.node_in = node_in;
    }

    public TreeNode[] getPath_in() {
        return path_in;
    }

    public void setPath_in(TreeNode[] path_in) {
        this.path_in = path_in;
    }

    public String getClaim_id() {
        return claim_id;
    }

    public void setClaim_id(String claim_id) {
        this.claim_id = claim_id;
    }
    
    
    
   /**********************************************************/
    
    
}

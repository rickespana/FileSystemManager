/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.List;
import java.util.Vector;
/**
 *
 * @author rick_espana
 */
public class fsmetadata {
    
    private String user;
    private float quota;
    private float used_quota;
    private long number_of_files;
    private List<String> fs_claims; 
 
    public fsmetadata(String _user, float _quota){
        setUser(_user);
        setQuota(_quota);
        setUsed_quota(0F);
        setNumber_of_files(0L);
        setFs_claims(new Vector<String>());
    }
    public fsmetadata(){

    }
           
    public BasicDBObject toDBObject() {
        BasicDBObject doc = new BasicDBObject();
        
        doc.put("_id", user);
        doc.put("quota", quota);
        doc.put("used_quota", used_quota);
        doc.put("number_of_files", number_of_files);
        doc.put("fs_claims",fs_claims);
        
        return doc;
    }
    public static fsmetadata fromDBObject(DBObject doc){
        fsmetadata fsmetadata_record = new fsmetadata();
        
        fsmetadata_record.user = (String) doc.get("_id");
        fsmetadata_record.quota = (Float) doc.get("quota");
        fsmetadata_record.used_quota = (Float) doc.get("used_quota");
        fsmetadata_record.number_of_files = (Long) doc.get("number_of_files");
        fsmetadata_record.fs_claims = (List) doc.get("fs_claims");
        
        return fsmetadata_record;
    }

    public long getNumber_of_files() {
        return number_of_files;
    }

    public void setNumber_of_files(long number_of_files) {
        this.number_of_files = number_of_files;
    }

    public List<String> getFs_claims() {
        return fs_claims;
    }

    public void setFs_claims(List<String> fs_claims) {
        this.fs_claims = fs_claims;
    }
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public float getQuota() {
        return quota;
    }

    public void setQuota(float quota) {
        this.quota = quota;
    }

    public float getUsed_quota() {
        return used_quota;
    }

    public void setUsed_quota(float used_quota) {
        this.used_quota = used_quota;
    }
    
    
}


package filemanager.model;

import com.mongodb.DBObject;
import java.io.Serializable;

/**
 *
 * @author rick_espana
 */
public class MetaDataStatistic implements Serializable{

    private double used_quota;
    private Long num_files;
    private double quota;
    private double perc_used_quota;
    
    public MetaDataStatistic() {}
    
    public MetaDataStatistic (DBObject db_obj){
        
        this.quota = (Double) (db_obj.get("quota"));
        this.num_files = (Long)db_obj.get("number_of_files");
        this.used_quota = (Double) db_obj.get("used_quota");
        this.perc_used_quota = (Double) db_obj.get("perc_used_quota");
    }

    public double getUsed_quota() {
        return used_quota;
    }

    public Long getNum_files() {
        return (Long) num_files;
    }

    public double getQuota() {
        return quota;
    }

    public double getPerc_used_quota() {
        return perc_used_quota;
    }
     
    // test object attributes values
    @Override
    public String toString(){
        
        return ("num_files: "+this.num_files+
                " perc_used_quota: "+this.perc_used_quota+
                " quota: "+this.quota+
                " used_quota: "+this.used_quota);
    }
    
    
}

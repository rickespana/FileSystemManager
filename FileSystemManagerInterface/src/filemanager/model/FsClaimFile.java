/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Date;

/**
 *
 * @author rick_espana
 */
public class FsClaimFile implements Serializable {
    
    private String TYPE = "file";
    private String file_name;
    private String content_type;
    private String mongo_id;
    private long file_length;
    private Date upload_date;
    private String file_md5;

    //helpers
    private String tmp_file_path;
    
    public FsClaimFile(){}
    
    public FsClaimFile(String _file_name){
        this.file_name = _file_name;
        //TODO: rest of implementation
        
  
    }
    
    /*** getters and setters ******/
    
    public String getFile_path() {
        return tmp_file_path;
    }

    public void setFile_path(String file_path) {
        this.tmp_file_path = file_path;
    }
  
    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getMongo_id() {
        return mongo_id;
    }

    public void setMongo_id(String mongo_id) {
        this.mongo_id = mongo_id;
    }

    public float getFile_length() {
        return file_length;
    }

    public void setFile_length(long file_length) {
        this.file_length = file_length;
    }

    public Date getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Date upload_date) {
        this.upload_date = upload_date;
    }

    public String getFile_md5() {
        return file_md5;
    }

    public void setFile_md5(String file_md5) {
        this.file_md5 = file_md5;
    }
    
    @Override
    public String toString(){
        return this.file_name;
    }
       
    
}

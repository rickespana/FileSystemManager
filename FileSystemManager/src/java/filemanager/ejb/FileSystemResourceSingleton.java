/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager.ejb;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.mongodb.*;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.PreUpdate;

/**
 *
 * @author rick_espana
 */

@Startup
@Singleton
public class FileSystemResourceSingleton {
        
    private final String DBNAME = "filesystemdb";
    public MongoClient mongoDBaccess;
    
    @PreDestroy
    private void preDestroyMessage(){
        System.out.println("Destroying Resource Singleton");
    }
    
    @PostConstruct
    private void initMongoDBPool(){
        try {
            // It could change if you have a cluster enviroment
            
            mongoDBaccess = new MongoClient();
            MongoOptions c_options = mongoDBaccess.getMongoOptions();
            String mongoOptions =
                                String.format("%n connectionsPerHost: %s%n "
                                              + "X ThreadsAllowed: %s%n "
                                              + "writeConcern: %s%n",
                                              c_options.getConnectionsPerHost(),
                                              c_options.getThreadsAllowedToBlockForConnectionMultiplier(),
                                              c_options.getWriteConcern());                               
            
            // Add log entry with MongoClient characteristics
            Logger.getLogger(FileSystemResourceSingleton.class.getName())
                    .log(Level.INFO, "MongoClient created: {0}",mongoOptions);
        } catch (UnknownHostException ex) {
            Logger.getLogger(FileSystemResourceSingleton.class.getName())
                                                .log(Level.SEVERE, null, ex);
        }
        
    }
    
    public DB getDBInstance(){
        return mongoDBaccess.getDB(DBNAME);
    }
}

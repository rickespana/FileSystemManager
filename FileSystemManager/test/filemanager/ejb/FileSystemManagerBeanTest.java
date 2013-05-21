

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager.ejb;

import ejb.FileSystemManager;
import filemanager.model.ClaimFS;
import filemanager.model.FsClaimFile;
import filemanager.model.MetaDataStatistic;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import javax.ejb.embeddable.EJBContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Hashtable;


/**
 *
 * @author rick_espana
 */
public class FileSystemManagerBeanTest {
    
    public FileSystemManagerBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createUserFSMetaData method, of class FileSystemManagerBean.
     */
    @Test
    public void testCreateUserFSMetaData() throws Exception {
        System.out.println("createUserFSMetaData");
        String _userId = "14";
        float _quota = 200.0F;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        FileSystemManager instance = (FileSystemManager)container.getContext()
                                        .lookup("java:global/classes/FileSystemManagerBean");

//        boolean expResult = true;
//        System.out.println("Creating FSMetadata....");
//        boolean result = instance.createUserFSMetaData(_userId, _quota);
//       
//        _quota = 100.5F;
//        System.out.println("Update Quota....");
//        instance.setUserFSMetadataQuota(_userId, _quota);
//        
//        System.out.println("Adding FSClaim....");
//        instance.createFSforClaim(_userId, "212");
//        
//        MetaDataStatistic user_stats = instance.getFSMetadataStatistics(_userId);
//        System.out.println("Statistics");
//        System.out.println(user_stats.toString());
//       
        ArrayList ar;
//         System.out.println("Byte Ocupation: "+instance.getClaimFSByteOccupation());
//         System.out.println("FS Number of Files: "+instance.getClaimFSNumFiles());
//         System.out.println("ClaimFSId: "+instance.getClaimFSId());
//         System.out.println("Working Path: "+instance.claimFSgetWorkingPath());
//         System.out.println("Root Contents: "+instance.claimFSgetRootContents());
//      
//          System.out.println("Changing Directory");
//          //TODO: unexistent index?
//          instance.claimFSChangeDirectory(1);
//          System.out.println("Working Path: "+instance.claimFSgetWorkingPath());
//          System.out.println("Json Display: "+instance.claimFSgetActualDirContents());
//         
//          System.out.println("Creating new folder  Carpeta in "+instance.claimFSgetWorkingPath());
//          instance.claimFScreateDir("Carpeta");
//          System.out.println("Working Path after Carpeta Creation: "+instance.claimFSgetWorkingPath());
//          instance.claimFScreateDir("Carpeta 2");
//          System.out.println("Working Path after Carpeta 2 Creation: "+instance.claimFSgetWorkingPath());
//          instance.claimFSgoBackDir();
//          System.out.println("Working Path: "+instance.claimFSgetWorkingPath());
//
//          System.out.println("Renaming Carpeta in");
//          instance.claimFSrenameDirectory(0,"NombreNuevo");
//          System.out.println("Json Display: "+instance.claimFSgetActualDirContents());
//     
//          ar = Collections.list(instance.getClaimFSTree().preorderEnumeration());
//          System.out.println("Tree Preorder");
//          System.out.println(Arrays.toString(ar.toArray()));
//          
//          System.out.println("Deleting Diretory NombreNuevo");
//          instance.claimFSdeleteDir(0);
//          System.out.println("DirContents after delete for "+instance.claimFSgetWorkingPath()+": "+instance.claimFSgetActualDirContents());
//
//          instance.claimFSgoBackToRoot();
//          System.out.println("Working Path after go Back to Root "+instance.claimFSgetWorkingPath());
//          System.out.println("DirContents: "+instance.claimFSgetActualDirContents());
//         
//          System.out.println("Saving modified Claim to DB");
//          instance.saveClaimFStoDB();
                  
          System.out.println("Obtain it again");
          instance.loadClaimFS("212");
          
          ar = Collections.list(instance.getClaimFSTree().preorderEnumeration());
          System.out.println("Tree Preorder");
          System.out.println(Arrays.toString(ar.toArray()));
          System.out.println("Actual Dir Contents: "+instance.claimFSgetActualDirContents());
          
          FsClaimFile fsc = new FsClaimFile("Word");
          fsc.setFile_path("C:/Users/respana/Desktop/20130125_MP_FormatoQA_T4_CompraComercio_V1.0.docx");
          fsc.setFile_extension("doc");
          
          FsClaimFile fsc2 = new FsClaimFile("Imagen");
          fsc2.setFile_path("C:/Users/respana/Desktop/Chrysanthemum.jpg");
          fsc2.setFile_extension("jpg");
          
          System.out.println("Create file for claim");
          instance.UploadFileforClaim(fsc,fsc2);
          System.out.println("Actual Dir Contents: "+instance.claimFSgetActualDirContents());
          instance.saveClaimFStoDB();
        
          
    }
}
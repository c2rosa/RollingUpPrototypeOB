import java.io.File;

/**
 * Created by rosa.charles on 2/17/16.
 */
public class launchToDeleteFiles {

    public launchToDeleteFiles() {

    }

    public static void main(String[] arg) throws Exception {
        launchToDeleteFiles myLaunchToDeleteFiles = new launchToDeleteFiles();

        myLaunchToDeleteFiles.deleteContentsOfDirectories();
    }

    private void deleteContentsOfDirectories() {

        String baseDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";

        for (int id=2088; id<2609; id++){
            System.out.println(id);
            String runId = id+"";

            String dir_preprocessorOutput_local = baseDirectory+runId+"\\in\\Output\\";
            File df_preprocessorOutput_local = new File(dir_preprocessorOutput_local);
            if (df_preprocessorOutput_local.exists()) {
                this.deleteContentOfDirectory(df_preprocessorOutput_local);
            }

            String dir_coreModelOutput_local = baseDirectory+runId+"\\in\\Output\\OptimOutput\\";
            File df_coreModelOutput_local = new File(dir_coreModelOutput_local);
            if (df_coreModelOutput_local.exists()) {
                this.deleteContentOfDirectory(df_coreModelOutput_local);
            }

            dir_preprocessorOutput_local = baseDirectory+runId+"\\in\\Output - 100 cube capacity\\";
            df_preprocessorOutput_local = new File(dir_preprocessorOutput_local);
            if (df_preprocessorOutput_local.exists()) {
                this.deleteContentOfDirectory(df_preprocessorOutput_local);
            }

            dir_coreModelOutput_local = baseDirectory+runId+"\\in\\Output - 100 cube capacity\\OptimOutput\\";
            df_coreModelOutput_local = new File(dir_coreModelOutput_local);
            if (df_coreModelOutput_local.exists()) {
                this.deleteContentOfDirectory(df_coreModelOutput_local);
            }


        }

    }

    private void deleteContentOfDirectory(File directory) {

        if(!directory.isDirectory()) {
            return;
        }

        // Only do this if this is a directory
        String[] files = directory.list();
        if (files.length > 0) {
            for(int i=0; i<files.length; i++) {
                String myFileName_short = files[i];
                String myFileName = directory.toString()+"\\"+myFileName_short;
                File myFile = new File(myFileName);
                if(!myFile.exists()) {
                    // Don't delete if it doesn't exist
                    continue;
                }
                if(!myFile.canWrite()) {
                    // Don't delete if can't write
                    continue;
                }
                if(myFile.isDirectory()) {
                    // Don't delete subdirectories
                    continue;
                }

                boolean success = myFile.delete();

                if(success) {
                    System.out.println("Deleted myFileName=" + myFileName);
                } else {
                    System.out.println("Did not Delete myFileName=" + myFileName);
                }
            }
        }

    }
}

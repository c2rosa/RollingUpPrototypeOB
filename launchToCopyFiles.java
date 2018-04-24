import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosa.charles on 2/17/16.
 */
public class launchToCopyFiles {

    public launchToCopyFiles() {

    }

    public static void main(String[] arg) throws Exception {
        launchToCopyFiles myLaunchToCopyFiles = new launchToCopyFiles();

        String myMode = arg[0];
        int runID_lb = new Integer(arg[1]);
        int runID_ub = new Integer(arg[2]);

        myLaunchToCopyFiles.copyContentsOfRemoteDirectoriesToLocalDirectories(myMode, runID_lb, runID_ub);
    }

    private void copyContentsOfRemoteDirectoriesToLocalDirectories(String myMode, int runID_lb, int runID_ub) throws Exception {

        String copyFromBaseDirectory = "\\\\nfs\\appdataprod\\PROD\\OperationsResearch\\LHOptimizer\\";

        String copyToBaseDirectory = null;
        if(myMode.equals("OB")) {
            copyToBaseDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\OB Model Phase II\\";
        } else if(myMode.equals("FAC")) {
            copyToBaseDirectory = "C:\\Old Drive\\rosa.charles\\workspace_git\\FAC\\LHOProdExtracts\\";
        }

        for (int id=runID_lb ; id<=runID_ub; id++){

            System.out.println(id);
            String runId = id+"";

            String copyFromDirectory = copyFromBaseDirectory+runId+"\\in\\";
            File df_copyFromDirectory = new File(copyFromDirectory);
            if (!df_copyFromDirectory.exists()) {
                continue;
            }

            String myShiftCode = this.getShiftCode("lho_model_run.csv", copyFromDirectory);

            if(myShiftCode == null) {
                continue;
            }

            boolean shouldWeContinue = false;
            if(myMode.equals("OB") && myShiftCode.equals("O")) {
                shouldWeContinue = true;
            } else if(myMode.equals("FAC") && myShiftCode.equals("F")) {
                shouldWeContinue = true;
            }

            if(!shouldWeContinue) {
                continue;
            }

            String copyToDirectory = copyToBaseDirectory+runId+"\\in\\";
            File df_copyToDirectory = new File(copyToDirectory);
            if (!df_copyToDirectory.exists()) {
                boolean isDirectoryCreated = df_copyToDirectory.mkdirs();
                if(!isDirectoryCreated) {
                    // this is odd
                    int temp_a = 0;
                }
            }

            this.copyDirectory(df_copyFromDirectory, df_copyToDirectory);



        }

    }

    public String getShiftCode(String file_lho_model_run, String dir_in) throws Exception {

        File df = new File(dir_in);
        if (!df.exists()) {
            return null;
        }

        BufferedReader br = null;
        String dbRecord;

        try {
            File f = new File(dir_in+file_lho_model_run);
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(bis));
            dbRecord = br.readLine();

            while ( (dbRecord = br.readLine()) != null) {

                String[] val = dbRecord.split(",");
                String ShiftCode = val[3];

                if(ShiftCode != null) {
                    return ShiftCode;
                } else {
                    int temp_a = 0;
                }


            }
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("IOException error trying to close the file: " +
                            ioe.getMessage());
                }
            } // end if
        } // end finally

        return null;


    }

    public void copyDirectory(File sourceLocation , File targetLocation) throws IOException {

        if (sourceLocation.isDirectory() && targetLocation.isDirectory()) {

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            boolean areTheseBothFiles = false;
            if(!sourceLocation.isDirectory() && !targetLocation.isDirectory()) {
                areTheseBothFiles = true;
            }

            if(!areTheseBothFiles) {
                return;
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
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


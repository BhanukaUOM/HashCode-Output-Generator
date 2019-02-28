import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.zip.*;

public class main {
    static StringBuilder s = new StringBuilder();
    public static void main(String[] args) {
        file file = new file();
        file.deletewithzip();
        file.checkdir();
        generate();
    }

    static void generate() {
		out out = new out("big");
		code(out);
		out.close();
    }

    static void code(out out) {
		out.print("a ");
        out.println(22);
    }
}

class file{
    String dir = "";
    void createFile(String type, String fName, String value){
        String fileName = fName;
        fileName += ".out";

        File file = new File(dir + type + "/" + fileName);
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(value);
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    void checkdir() {
        String PATH = dir + "files_in";

        File directory = new File(PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }

        PATH = dir + "files_out";
        directory = new File(PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    void delete(){
        String PATH = dir + "files_in";
        File directory = new File(PATH);
        deleteutil(directory);

        PATH = dir + "files_out";
        directory = new File(PATH);
        deleteutil(directory);
    }

    void deletewithzip(){

        String PATH = dir + "files_in";
        File directory = new File(PATH);
        deleteutil(directory);

        PATH = dir + "files_out";
        directory = new File(PATH);
        deleteutil(directory);

        PATH = dir + "testcase.zip";
        directory = new File(PATH);
        deleteutil(directory);
    }

    void deleteutil(File file){

        if(file.isDirectory()){

            //directory is empty, then delete it
            if(file.list().length==0){

                file.delete();
                //System.out.println("Directory is deleted : " + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    deleteutil(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    //System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            //System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    void zip(){
        try {
            FileOutputStream fos = new FileOutputStream("testcase.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            String file1Name = dir + "files_in";
            String file2Name = dir + "files_out";

            addToZipFile(file1Name, zos);
            addToZipFile(file2Name, zos);

            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

        System.out.println("Writing '" + fileName + "' to zip file");

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}

class out{
    String fName;
    StringBuilder sb = new StringBuilder();
    out(String fName){
        this.fName = fName;
    }

    void println(Object... objects){
        for (int i = 0; i < objects.length; i++)
            sb.append(objects[i]);
        sb.toString().trim();
        sb.append("\n");
    }

    void print(Object... objects){
        for (int i = 0; i < objects.length; i++)
            sb.append(objects[i]);
    }

    void close(){
        file file = new file();
        file.createFile("files_out", fName, sb.toString().trim());
    }
}
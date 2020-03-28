package src.main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class FileWriter {

    public BufferedWriter writeInFile(String name){
        FileOutputStream file = null;
        try {
            String s = "D:\\PT2019\\pt2019_30422_mateiu_patricia\\New folder\\" + name + ".txt";
            file = new FileOutputStream(s);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter out = new OutputStreamWriter(file);
        BufferedWriter bw = new BufferedWriter(out);
       return bw;
    }
}

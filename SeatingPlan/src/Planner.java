import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Planner
{
  public static int[][] firstColumn = {
    { 2, 1, 1, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2 }, 
    new int[20], 
    new int[20], 
    new int[20], 
    new int[20], 
    new int[20], 
    new int[20], 
    new int[20] };
  public static int[][] lastColumn = {
    { 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11 }, 
    { 10, 10, 11, 11, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 8, 8 }, 
    { 8, 9, 10, 11, 11, 10, 10, 10, 9, 9, 9, 9, 9, 8, 9, 9, 9, 9, 9, 9 }, 
    { 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11 }, 
    { 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12 }, 
    { 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11 }, 
    { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 }, 
    { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 } };
  public static int[] rowLength = { 20, 4, 12 };
  private static ArrayList<boolean[][]> auditorium = new ArrayList<>();
  
  public static void planSeat(File csvFile)
  {
    initAuditorium();
    
    LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
    try
    {
      readFile(csvFile, data);
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    LinkedHashMap<String, Teacher> teachers = new LinkedHashMap<>();
    String name;
    for (Map.Entry<String, Integer> V : data.entrySet())
    {
      name = (String)V.getKey();
      int numStudents = ((Integer)V.getValue()).intValue();
      teachers.put(name, new Teacher(name, numStudents));
    }
    assignSeating(teachers);
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%-15s%-15s%-15s%-15s\n\n", "Teacher", "Floor", "Block", "Rows"));
    for (Teacher teacher : teachers.values()) {
      sb.append(teacher);
    }
    Frame.teacherText = sb.toString();
    Frame.teacherCollection = teachers.values();
  }
  
  private static void assignSeating(HashMap<String, Teacher> teachers)
  {
    int currentBlock = 0;
    int currentRow = 0;
    int currentColumn = 0;
    boolean[][] block = auditorium.get(0);
    int numStudents;
    for (Entry<String, Teacher> teacher: teachers.entrySet())
    {
      Teacher t = teacher.getValue();
      numStudents = t.getNumStudents();
      if (currentRow > block.length - 3) {
        if (block[currentRow].length - currentColumn + (block.length - 1 - currentRow) * block[currentRow].length < numStudents)
        {
          currentRow = 0;
          currentColumn = 0;
          currentBlock++;
          block = auditorium.get(currentBlock);
        }
      }
      t.setBlock(auditorium.indexOf(block));
      t.addRow(currentRow);
      for(int i=0;i<numStudents;i++){
    	  boolean successfullySeated = false;
          if (block[currentRow][currentColumn])
          {
            successfullySeated = true;
            t.addColumn(currentColumn);
          }
          currentColumn++;
          if (!successfullySeated) {
            i--;
          }
          if (currentColumn > lastColumn[currentBlock][currentRow])
          {
            currentColumn = 0;
            currentRow++;
            if (i != numStudents - 1) {
              t.addRow(currentRow);
            }
          }
          if (currentRow == block.length)
          {
            currentRow = 0;
            currentColumn = 0;
            currentBlock++;
            block = auditorium.get(currentBlock);
          }
        }
      }
     
  }
  
  private static void initAuditorium()
  {
    boolean[][] lstage = setTrue(new boolean[20][12]);
    lstage[0][0] = false;lstage[0][1] = false;lstage[1][0] = false;lstage[2][0] = false;
    for (int row = 5; row < lstage.length; row++) {
      if (row < 8) {
        lstage[row][0] = false;
      } else {
        lstage[row][0] = lstage[row][1] = false;
      }
    }
    lstage[13][2] = false;
    auditorium.add(lstage);
    
    boolean[][] mstage = setTrue(new boolean[20][14]);
    for (int row = 0; row < 6; row++) {
      if (row < 2) {
        mstage[row][11] = (mstage[row][12] = mstage[row][13] = false);
      } else if (row < 4) {
        mstage[row][12] = (mstage[row][13] = false);
      } else {
        mstage[row][13] = false;
      }
    }
    for (int row = 18; row < 20; row++) {
      for (int column = 9; column < 14; column++) {
        mstage[row][column] = false;
      }
    }
    auditorium.add(mstage);
    
    boolean[][] rstage = setTrue(new boolean[20][12]);
    rstage[0][9] = (rstage[0][10] = rstage[0][11] = false);rstage[1][10] = (rstage[1][11] = false);rstage[2][11] = false;
    for (int row = 5; row < rstage.length; row++) {
      if (row < 8) {
        rstage[row][11] = false;
      } else {
        rstage[row][10] = (rstage[row][11] = false);
      }
    }
    rstage[13][9] = false;
    auditorium.add(rstage);
    
    auditorium.add(setTrue(new boolean[4][12]));
    auditorium.add(setTrue(new boolean[4][13]));
    auditorium.add(setTrue(new boolean[4][12]));
    auditorium.add(setTrue(new boolean[12][15]));
    auditorium.add(setTrue(new boolean[12][15]));
  }
  
  private static boolean[][] setTrue(boolean[][] ar)
  {
    for (int row = 0; row < ar.length; row++) {
      for (int column = 0; column < ar[row].length; column++) {
        ar[row][column] = true;
      }
    }
    return ar;
  }
  
  private static void readFile(File file, HashMap<String, Integer> data)
    throws NumberFormatException, IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line = "";
    String csvSplit = ",";
    while ((line = br.readLine()) != null)
    {
      String[] words = line.split(csvSplit);
      if (words.length > 2)
      {
        String name = words[0] + "," + words[1];
        data.put(name.substring(1, name.length() - 1), Integer.valueOf(words[2]));
      }
      else
      {
        data.put(words[0], Integer.valueOf(words[1]));
      }
    }
    br.close();
  }
}

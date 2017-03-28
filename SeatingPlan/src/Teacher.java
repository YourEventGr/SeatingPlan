import java.awt.Polygon;
import java.util.LinkedList;

public class Teacher
{
  private String name;
  private int numStudents;
  private int block;
  private int beginColumn;
  private int endColumn;
  private int beginRow;
  private int endRow;
  private Polygon poly;
  private int nameX;
  private int nameY;
  private LinkedList<Integer> rows;
  private LinkedList<Integer> columns;
  
  public Teacher(String name, int numStudents)
  {
    this.name = name;
    this.numStudents = numStudents;
    this.rows = new LinkedList<>();
    this.columns = new LinkedList<>();
  }
  
  public Teacher()
  {
    this("", 0);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getFloor()
  {
    if (this.block < 3) {
      return "Stage";
    }
    return "Balcony";
  }
  
  public String getBlock()
  {
    switch (this.block)
    {
    case 0: 
    case 3: 
      return "Left";
    case 1: 
    case 4: 
      return "Middle";
    case 2: 
    case 5: 
      return "Right";
    case 6: 
      return "Back Left";
    case 7: 
      return "Back Right";
    }
    return "null";
  }
  
  public String getRows()
  {
    String seatingRows = "";
    this.beginRow = ((Integer)this.rows.get(0)).intValue();
    this.endRow = ((Integer)this.rows.get(this.rows.size() - 1)).intValue();
    this.beginColumn = ((Integer)this.columns.get(0)).intValue();
    this.endColumn = ((Integer)this.columns.get(this.columns.size() - 1)).intValue();
    seatingRows = seatingRows + getCharacterRow(this.beginRow);
    seatingRows = seatingRows + getActualColumn(this.beginColumn);
    seatingRows = seatingRows + " ~ ";
    seatingRows = seatingRows + getCharacterRow(this.endRow);
    seatingRows = seatingRows + getActualColumn(this.endColumn);
    return seatingRows;
  }
  
  private char getCharacterRow(int row)
  {
    char c;
    if (this.block < 3)
    {
      if (row < 6) {
        c = (char)(67 + row);
      } else {
        c = (char)(74 + (row - 6));
      }
    }
    else
    {
      if (row < 6) {
        c = (char)(65 + row);
      } else {
        c = (char)(74 + (row - 8));
      }
    }
    return c;
  }
  
  private int getActualColumn(int column)
  {
    switch (this.block)
    {
    case 0: 
      return 23 - 2 * column;
    case 1: 
      return column + 1;
    case 2: 
      return 2 * (column + 1);
    case 3: 
      return 23 - 2 * column;
    case 4: 
      return column + 1;
    case 5: 
      return 2 * (column + 1);
    case 6: 
      return 29 - 2 * column;
    case 7: 
      return 2 * (column + 1);
    }
    return 0;
  }
  
  public void makePolygon()
  {
    this.beginRow = ((Integer)this.rows.get(0)).intValue();
    this.endRow = ((Integer)this.rows.get(this.rows.size() - 1)).intValue();
    this.beginColumn = ((Integer)this.columns.get(0)).intValue();
    this.endColumn = ((Integer)this.columns.get(this.columns.size() - 1)).intValue();
    int[] xPoly = null;
    int[] yPoly = null;
    
    int xIndent = (this.block % 3 + 1) * Panel.marginX;
    int yIndent = (this.block / 3 + 1) * Panel.marginY;
    if (this.block <= 5) {
      xIndent += this.block % 3 * 13 * Panel.lengthX;
    } else {
      xIndent += this.block % 2 * 15 * Panel.lengthX;
    }
    if ((this.block >= 3) && (this.block <= 5)) {
      yIndent += Planner.rowLength[0] * Panel.lengthY;
    } else if ((this.block >= 6) && (this.block <= 7)) {
      yIndent += Planner.rowLength[0] * Panel.lengthY + Planner.rowLength[1] * Panel.lengthY;
    }
    LinkedList<Integer> x = new LinkedList<>();
    x.add(Integer.valueOf(this.beginColumn * Panel.lengthX));
    for (int i = this.beginRow; i <= this.endRow - 1; i++)
    {
      x.add(Integer.valueOf(Planner.lastColumn[this.block][i] * Panel.lengthX));
      x.add(Integer.valueOf(Planner.lastColumn[this.block][i] * Panel.lengthX));
    }
    if (this.endColumn == Planner.lastColumn[this.block][this.endRow])
    {
      x.add(Integer.valueOf(this.endColumn * Panel.lengthX));
      x.add(Integer.valueOf(this.endColumn * Panel.lengthX));
    }
    else
    {
      x.add(Integer.valueOf((this.endColumn + 1) * Panel.lengthX));
      x.add(Integer.valueOf((this.endColumn + 1) * Panel.lengthX));
    }
    for (int i = this.endRow; i >= this.beginRow + 1; i--)
    {
      x.add(Integer.valueOf(Planner.firstColumn[this.block][i] * Panel.lengthX));
      x.add(Integer.valueOf(Planner.firstColumn[this.block][i] * Panel.lengthX));
    }
    x.add(Integer.valueOf(this.beginColumn * Panel.lengthX));
    
    LinkedList<Integer> y = new LinkedList<>();
    for (int i = this.beginRow; i <= this.endRow + 1; i++)
    {
      y.add(Integer.valueOf(i * Panel.lengthY));
      y.add(Integer.valueOf(i * Panel.lengthY));
    }
    for (int i = this.endRow; i >= this.beginRow + 1; i--)
    {
      y.add(Integer.valueOf(i * Panel.lengthY));
      y.add(Integer.valueOf(i * Panel.lengthY));
    }
    xPoly = new int[x.size()];
    for (int i = 0; i < xPoly.length; i++) {
      xPoly[i] = ((Integer)x.get(i)).intValue();
    }
    yPoly = new int[y.size()];
    for (int i = 0; i < yPoly.length; i++) {
      yPoly[i] = ((Integer)y.get(i)).intValue();
    }
    for (int i = 0; i < xPoly.length; i++)
    {
      xPoly[i] += xIndent;
      yPoly[i] += yIndent;
    }
    this.poly = new Polygon(xPoly, yPoly, xPoly.length);
    makeCoordinate(xIndent, xPoly, yPoly);
    updatePanelHeight(yPoly[(yPoly.length / 2)]);
  }
  
  private void updatePanelHeight(int maxY)
  {
    if (maxY > Panel.height) {
      Panel.height = maxY + 30;
    }
  }
  
  public int getNameX()
  {
    return this.nameX;
  }
  
  public int getNameY()
  {
    return this.nameY;
  }
  
  public void makeCoordinate(int xIndent, int[] xPoly, int[] yPoly)
  {
    double textIndentIndex = 0.0D;
    if (this.block % 3 == 0) {
      textIndentIndex = 0.45D;
    } else if (this.block % 3 == 1) {
      textIndentIndex = 0.47D;
    } else {
      textIndentIndex = 0.3D;
    }
    this.nameX = ((int)(xIndent + textIndentIndex * (Planner.lastColumn[this.block][((this.endRow - this.beginRow) / 2)] - Planner.firstColumn[this.block][((this.endRow - this.beginRow) / 2)]) * Panel.lengthX));
    this.nameY = ((int)(yPoly[0] + this.rows.size() / 1.8D * Panel.lengthY));
  }
  
  public int getBeginColumn()
  {
    return this.beginColumn;
  }
  
  public int getEndColumn()
  {
    return this.endColumn;
  }
  
  public int getNumStudents()
  {
    return this.numStudents;
  }
  
  public Polygon getPoly()
  {
    return this.poly;
  }
  
  public void setBlock(int block)
  {
    this.block = block;
  }
  
  public void addRow(int row)
  {
    this.rows.add(Integer.valueOf(row));
  }
  
  public void addColumn(int column)
  {
    this.columns.add(Integer.valueOf(column));
  }
  
  public String toString()
  {
    return String.format("%-15s%-15s%-15s%-15s", new Object[] { getName(), getFloor(), getBlock(), getRows() }) + "\n";
  }
}

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Collection;
import javax.swing.JPanel;

public class Panel
  extends JPanel
{
  private static final long serialVersionUID = -561548754897173174L;
  public static int lengthX = 30;
  public static int lengthY = 40;
  public static int marginX = 50;
  public static int marginY = 100;
  public static int width = 0;
  public static int height = 0;
  private static int headerFontSize = 28;
  private static int teacherNameFontSize = 18;
  private static int seatNumberGuidelineFontSize=12;
  private Collection<Teacher> teachers;
  
  public Panel(Collection<Teacher> teachers)
  {
    this.teachers = teachers;
    width = 4 * marginX + 36 * lengthX;
    
    double zoomFactor = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1920.0D;
    lengthX = (int)(lengthX * zoomFactor);
    lengthY = (int)(lengthY * zoomFactor);
    marginX = (int)(marginX * zoomFactor);
    marginY = (int)(marginY * zoomFactor);
    width = (int)(width * zoomFactor);
    headerFontSize = (int)(headerFontSize * zoomFactor);
    teacherNameFontSize = (int)(teacherNameFontSize * zoomFactor);
    seatNumberGuidelineFontSize=(int)(seatNumberGuidelineFontSize * zoomFactor);
  }
  
  public void paintComponent(Graphics g)
  {
	  //Draw Stage & Balcony sign
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.setStroke(new BasicStroke(3.0F));
    g2.setFont(new Font("TimesRoman", 1, headerFontSize));
    g2.drawString("Stage", 2 * marginX + 18 * lengthX, marginY / 2);
    g2.drawString("Balcony", 2 * marginX + 18 * lengthX, 3 * marginY / 2 + 20 * lengthY);
    g2.setFont(new Font("TimesRoman", 1, teacherNameFontSize));
    
    //Draw seat numbers
    int tY = marginY + g2.getFontMetrics().getHeight() - 5;
    for (char c = 'C'; c <= 'W'; c = (char)(c + '\001')) {
      if (c != 'I')
      {
        g2.drawString(String.valueOf(c), marginX - 25, tY);
        g2.drawString(String.valueOf(c), 2 * marginX + 12 * lengthX - 10, tY);
        g2.drawString(String.valueOf(c), 3 * marginX + 26 * lengthX - 25, tY);
        tY += lengthY;
      }
    }
    
    tY = 2 * marginY + 20 * lengthY + g2.getFontMetrics().getHeight() - 5;
    for (char c = 'A'; c <= 'D'; c = (char)(c + '\001')) {
      if (c != 'I')
      {
        g2.drawString(String.valueOf(c), marginX - 20, tY);
        g2.drawString(String.valueOf(c), 2 * marginX + 12 * lengthX - 10, tY);
        g2.drawString(String.valueOf(c), 3 * marginX + 26 * lengthX - 20, tY);
        tY += lengthY;
      }
    }
    
    if (this.teachers == null) {
      return;
    }
    for (Teacher t : this.teachers)
    {
      t.makePolygon();
      g2.drawPolygon(t.getPoly());
      g2.drawString(t.getName()+" : "+t.getRows(), t.getNameX()-40, t.getNameY()+10);
    }
    
    //Draw column guidelines
    g2.setFont(new Font("TimesNewRoman",Font.BOLD,seatNumberGuidelineFontSize));
    for(int i=23,curX=marginX;i>=1;i-=2)
    {
    	g2.drawString(String.valueOf(i), curX, marginY-10);
    	g2.drawString(String.valueOf(i), curX, 2 * marginY + 20 * lengthY -10);
    	curX+=(lengthX-1);
    }
    for(int i=101,curX=2*marginX+13*lengthX;i<=114;i++)
    {
    	g2.drawString(String.valueOf(i), curX, marginY-10);
    	g2.drawString(String.valueOf(i), curX, 2 * marginY + 20 * lengthY -10);
    	curX+=(lengthX-2);
    }
    for(int i=2,curX=3 * marginX + 26 * lengthX;i<=24;i+=2)
    {
    	g2.drawString(String.valueOf(i), curX, marginY-10);
    	g2.drawString(String.valueOf(i), curX, 2 * marginY + 20 * lengthY -10);
    	curX+=(lengthX-1);
    }
    
  }
  
  public Dimension getPreferredSize()
  {
    return new Dimension(width, height);
  }
}

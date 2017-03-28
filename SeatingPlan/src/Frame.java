import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class Frame
{
  private static JFrame mainFrame;
  private static JTabbedPane jtb;
  private static JButton selectButton;
  private static JButton saveButton;
  public static Collection<Teacher> teacherCollection;
  public static String teacherText;
  private static JTextArea textArea;
  private static Panel layoutPanel;
  
  public static void main(String[] args)
  {
    mainFrame = new JFrame("Planner");
    mainFrame.setDefaultCloseOperation(3);
    mainFrame.setResizable(false);
    
    jtb = new JTabbedPane();
    
    selectButton = new JButton("Select file");
    
    selectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        JFileChooser fileChooser = new JFileChooser();
        int returnOption = fileChooser.showOpenDialog(null);
        if (returnOption == 0)
        {
          File csvFile = fileChooser.getSelectedFile();
          Planner.planSeat(csvFile);
          
          Frame.jtb.addTab("Layout", Frame.makeLayoutScrollPanel());
          Frame.jtb.addTab("Text", Frame.makeTextScrollPanel(26));
          Frame.mainFrame.add(Frame.jtb);
          Frame.mainFrame.pack();
        }
      }
    });
    saveButton = new JButton("Save");
    saveButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        JFileChooser dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(1);
        dirChooser.setAcceptAllFileFilterUsed(false);
        int returnOption = dirChooser.showOpenDialog(null);
        if (returnOption == 0)
        {
          File dir = dirChooser.getSelectedFile();
          try
          {
            ImageIO.write(Frame.getScreenShot(Frame.layoutPanel), "png", new File(dir.getAbsolutePath() + "\\assemblyLayout.png"));
            ImageIO.write(Frame.getScreenShot(Frame.textArea), "png", new File(dir.getAbsolutePath() + "\\assemblySeating.png"));
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        }
      }
    });
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(selectButton);
    buttonPanel.add(saveButton);
    mainFrame.add(buttonPanel, "North");
    mainFrame.pack();
    mainFrame.setVisible(true);
  }
  
  private static JScrollPane makeLayoutScrollPanel()
  {
    layoutPanel = new Panel(teacherCollection);
    JScrollPane scroll = new JScrollPane(layoutPanel, 20, 31);
    scroll.setBounds(50, 30, 300, 50);
    return scroll;
  }
  
  private static JScrollPane makeTextScrollPanel(int fontSize)
  {
    textArea = new JTextArea(30, 60);
    textArea.setLineWrap(true);
    textArea.setText(teacherText);
    textArea.setFont(new Font("Consolas", 1, fontSize));
    textArea.setEditable(false);
    JScrollPane scroll = new JScrollPane(textArea, 20, 31);
    return scroll;
  }
  
  private static BufferedImage getScreenShot(Component component)
  {
    BufferedImage image = new BufferedImage(
      component.getWidth(), 
      component.getHeight(), 
      1);
    
    component.paint(image.getGraphics());
    return image;
  }
}

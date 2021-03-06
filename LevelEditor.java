package gw;
import java.io.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.Font;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;

import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JButton;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

import java.util.ArrayList;
class LevelEditor extends JPanel {
  ArrayList<Sfera> balle;
  Nave _ships[] = new Nave[2];
  Sad _set = new Sad();
  Sfera s;
  protected transient Image bg = new ImageIcon("gw/sfondo.jpg").getImage();

  LevelEditor()
  {
    balle = new ArrayList<Sfera>();
    _ships[0] = new Nave(_set._W, _set._H);
    _ships[0].setColor(new Color(72,160,220));


    double dist;
    do
    {
      _ships[1] = new Nave(_set._W, _set._H,"gw/20x20spshp.png");
      _ships[1].setColor(new Color(255, 89, 230));
      dist = Math.sqrt(Math.pow(_ships[0].getx() - _ships[1].getx(),2)+Math.pow(_ships[0].gety() - _ships[1].gety(),2));
    } while (dist < 2*_set._imgEdge+100f);



    addMouseListener(new MouseAdapter(){
      Sfera obiettivo;
      public void mouseClicked(MouseEvent e)
      {

        if (e.getClickCount() == 1)
        {


        } else if (e.getClickCount() == 2)
        {
          obiettivo = addSfera(e.getX(),e.getY());
          if (obiettivo != null)
          {
            JSpinner massa = new JSpinner(new SpinnerNumberModel(150.0,100,700,1));
            JSpinner raggio = new JSpinner(new SpinnerNumberModel(150.0,30,150,1));

            Object[] inputFields = {"Inserisci la massa", massa,
                "Inserisci il raggio",raggio};
            int option = JOptionPane.showConfirmDialog(
             null,
             inputFields,
             "Nuovo Pianeta",
             JOptionPane.OK_CANCEL_OPTION,
             JOptionPane.INFORMATION_MESSAGE
          );
          if (option ==JOptionPane.OK_OPTION){
            obiettivo.setM((double)massa.getValue());
            obiettivo.setR((double)raggio.getValue());
          }else{
            balle.remove(balle.size()-1);
            obiettivo = null;
            repaint();
            return;
          }

          repaint();

          }
        }
      }

    });
    addMouseMotionListener(new MouseMotionAdapter(){
      Nave target = null;
      double posX;
      double posY;
      public void mouseDragged(MouseEvent e)
      {
        if (target == null){
          target = onShip(e);
          if (target !=null)
          {
            posX=target.getx();
            posY = target.gety();
          }
        }else{
          target.setx(e.getX()-target.getR()/2);
          target.sety(e.getY()-target.getR()/2);
        }
        repaint();
        addMouseListener(new MouseAdapter()
        {
          public void mouseReleased(MouseEvent e)
          {
            if (onPlanet(e)!=null && target != null)
            {
              target.setx(posX);
              target.sety(posY);
              repaint();
            }
            target = null;
          }
        });
      }
    });
  }






  public Sfera addSfera(int x, int y)
  {
    s = new Sfera();
    s.setx((double)x-s.getR()/2);
    s.sety((double)y-s.getR()/2);

    Sfera b[] = new Sfera[balle.size()];
    b= balle.toArray(b);
    if (s.isValid(_ships,b,balle.size())){
      balle.add(s);
      repaint();
      return s;
    }
    repaint();
    return null;
  }
  public Sfera onPlanet(MouseEvent e)
  {
    for (Sfera s : balle)
    {
      if (Math.sqrt(Math.pow(e.getX()-s.getx()-s.getR()/2,2)+Math.pow(e.getY()-s.gety()-s.getR()/2,2)) <= s.getR()/2)
      {
        return s;
      }
    }
    return null;
  }
  public Nave onShip(MouseEvent e)
  {
    for (Nave s : _ships)
    {
      if ((e.getX() >= s.getx() && e.getX() <=s.getx()+s.getR())&&(e.getY() > s.gety() && e.getY() <= s.gety()+s.getR()))
      {
        return s;
      }
    }
    return null;
  }
  @Override
  protected void paintComponent(Graphics g)
  {

      super.paintComponent(g);
      //navetta.paintIcon(this,g);
      Graphics2D g2d = (Graphics2D)g;


      g2d.drawImage(bg,0,0,null);

      _ships[0].paintComponent(g);
      _ships[1].paintComponent(g);

      for (Sfera balla : balle)
      {
        balla.paintComponent(g);
      }


  }
  public Dimension getPreferredSize()
  {
    return new Dimension(_set._W,_set._H);
  }
  public void saveToFile()
  {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "gw", "gw");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showSaveDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      try{
        File chosen =chooser.getSelectedFile();
        FileOutputStream file = new FileOutputStream(chosen.getName()+".gw");
        ObjectOutputStream out = new ObjectOutputStream(file);
        //out.writeObject(_ships);
        Sfera b[] = new Sfera[balle.size()];
        b= balle.toArray(b);
        toSerialize nuova = new toSerialize();
        nuova.navi = _ships;
        nuova.sfere = b;
        out.writeObject(nuova);
        out.close();
        file.close();
      }catch(IOException e){
        e.printStackTrace();
      }
    }
  }
  public void readFromFile()
  {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "gw", "gw");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      try{
        File chosen = chooser.getSelectedFile();

        FileInputStream file = new FileInputStream(chosen.getName());
        ObjectInputStream in = new ObjectInputStream(file);
        toSerialize  eh = (toSerialize)in.readObject();
        in.close();
        file.close();
        _ships = eh.navi;
        balle.clear();
        for (Sfera i : eh.sfere)
        {
          balle.add(i);
        }

        repaint();
      }catch(IOException e){
        e.printStackTrace();
      }catch (ClassNotFoundException c) {
         c.printStackTrace();
         return;
      }
  }

}

}

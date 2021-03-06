package gw;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import java.io.*;


import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.ArrayList;
import javax.swing.Timer;






class Pannello extends JPanel implements ActionListener,java.io.Serializable
{
  // private int W_RES = 800;
  // private int H_RES = 600;
  private Timer time = new Timer(5,this);

  private int _counter = 0;
  private int _focus = 0;
  private Nave _ships[];
  private Proiettile pew;
  //private int _planetnum = 5;
  //private boolean _inbetween  = true;
  protected  Sad _set = new Sad();
  // _set._planetnum = 5;
  // _set._inbetween = false;
  private Sfera ball[];
  // private int _hheigth = 400;
  // private int _hwidth = 610;
  private int _x = 50;
  private int _y = 50;
  private int _raggio = 30;
  // private static int h=1200;
  // private static int w=800;

  private Pair[][] _ForceMatrix = new Pair[_set._W][_set._H];
    private int _conteggio=0;

  //roba per le frecce
  public int gap = 13;
  private Color clr;
  private float frc;
  private float frc_red;
  private float frc_blue;

  protected Settings s ;

  private transient ArrayList<Trajectory> _tr ;
  private transient Trajectory _current ;
  protected transient Image bg = new ImageIcon("gw/sfondo.jpg").getImage();


  //private JFormattedTextField angles[] ;
  //private JFormattedTextField forces[] ;
  private JSpinner angles[];
  private JSpinner forces[];
  private JButton butts[];
  private JLabel labels[] ;
  private int _points[];

  public Pannello()
  {
    loadGame();
    initUI();
  }

  public void refreshUI()
  {
    _counter=0;
    _focus = _counter%2;
    pew=null;
    removeAll();
    revalidate();
    loadMatrix();
    initUI();
  }

  private JSpinner createSpinner(SpinnerNumberModel spnm,int index,int x, int y)
  {
    JSpinner spin = new JSpinner(spnm);
    spin.setOpaque(false);
    spin.setBorder(BorderFactory.createLineBorder(_ships[index].getColor(),2,true));
    spin.getEditor().setOpaque(false);
    ((JSpinner.NumberEditor)spin.getEditor()).getTextField().setFont(new Font("Verdana", Font.BOLD,14));
    //angles[0].setValue(new Double(0));
    ((JSpinner.NumberEditor)spin.getEditor()).getTextField().setOpaque(false);
    ((JSpinner.NumberEditor)spin.getEditor()).getTextField().setForeground(new Color(250,250,250));
    //one_angle.setPreferredSize(new Dimension(100,20));
    spin.setBounds(x,y,100,20);
    //    spin.setBounds(20,_set._H-100,100,20);

    // Aggiunge un change listener per poter ruotare la nave direttamente
    // variando il valore dello spinner

    return spin;

  }

  public JButton createButton(int index, int x, int y)
  {
    JButton button = new JButton("Shoot");
    button.addActionListener(this);
    button.setBounds(x,y,100,20);
    button.setBorder(BorderFactory.createLineBorder(_ships[index].getColor(),2,true));
    return button;
  }

  public void initUI()
  {
    // angles = new JFormattedTextField[2];
    // forces = new JFormattedTextField[2];
    angles = new JSpinner[2];
    forces = new JSpinner[2];
    butts = new JButton[2];
    labels = new JLabel[2];
    _points = new int[2];
    _points[0] = 0;
    _points[1] = 0;
    // angles[0] = new JFormattedTextField();
    // angles[1] = new JFormattedTextField();
    // forces[0] = new JFormattedTextField();
    // forces[1] = new JFormattedTextField();

    // angles[0] = new JSpinner(new SpinnerNumberModel(0.0,-180,180,1));
    // angles[1] = new JSpinner(new SpinnerNumberModel(0.0,-180,180,1));
    // forces[0] = new JSpinner(new SpinnerNumberModel(1.0,0.0,5.0,0.01));
    // forces[1] = new JSpinner(new SpinnerNumberModel(1.0,0.0,5.0,0.01));
    forces[0] = createSpinner(new SpinnerNumberModel(1.0,0.0,5.0,0.01),0,20,_set._H-80);
    forces[1] = createSpinner(new SpinnerNumberModel(1.0,0.0,5.0,0.01),1,_set._W-120,_set._H-80);

    setLayout(null);
    //
    // angles[0].setOpaque(false);
    // angles[0].setBorder(BorderFactory.createLineBorder(_ships[0].getColor(),2,true));
    // angles[0].getEditor().setOpaque(false);
    // ((JSpinner.NumberEditor)angles[0].getEditor()).getTextField().setFont(new Font("Verdana", Font.BOLD,14));
    // //angles[0].setValue(new Double(0));
    // ((JSpinner.NumberEditor)angles[0].getEditor()).getTextField().setOpaque(false);
    // ((JSpinner.NumberEditor)angles[0].getEditor()).getTextField().setForeground(new Color(250,250,250));
    // //one_angle.setPreferredSize(new Dimension(100,20));
    // angles[0].setBounds(20,_set._H-100,100,20);
    // // Aggiunge un change listener per poter ruotare la nave direttamente
    // // variando il valore dello spinner
    // angles[0].addChangeListener(new ChangeListener(){
    //   public void stateChanged(ChangeEvent e)
    //   {
    //     _ships[0].rotate((double)angles[0].getValue());
    //     repaint(_ships[0].getRect());
    //   }
    // });
    angles[0] = createSpinner(new SpinnerNumberModel(0.0,-180,180,1),0,20,_set._H-100);
    angles[0].addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e)
      {
        _ships[0].rotate((double)angles[0].getValue());
        repaint(_ships[0].getRect());
      }
    });
    add(angles[0]);

    // forces[0].setOpaque(false);
    // forces[0].setBorder(BorderFactory.createLineBorder(_ships[0].getColor(),2,true));
    // forces[0].getEditor().setOpaque(false);
    // ((JSpinner.NumberEditor)forces[0].getEditor()).getTextField().setOpaque(false);
    // ((JSpinner.NumberEditor)forces[0].getEditor()).getTextField().setFont(new Font("Verdana", Font.BOLD,14));
    // //forces[0].setValue(new Double(0));
    // ((JSpinner.NumberEditor)forces[0].getEditor()).getTextField().setForeground(new Color(250,250,250));
    // forces[0].setBounds(20,_set._H-80,100,20);
    add(forces[0]);


    // angles[1].setOpaque(false);
    // angles[1].setBorder(BorderFactory.createLineBorder(new Color(255, 89, 230),2,true));
    // angles[1].getEditor().setOpaque(false);
    // ((JSpinner.NumberEditor)angles[1].getEditor()).getTextField().setFont(new Font("Verdana", Font.BOLD,14));
    // //angles[0].setValue(new Double(0));
    // ((JSpinner.NumberEditor)angles[1].getEditor()).getTextField().setOpaque(false);
    // ((JSpinner.NumberEditor)angles[1].getEditor()).getTextField().setForeground(new Color(250,250,250));
    // angles[1].setBounds(_set._W-120,_set._H-100,100,20);
    // // Aggiunge un change listener per poter ruotare la nave direttamente variando il valore dello spinner
    // angles[1].addChangeListener(new ChangeListener(){
    //   public void stateChanged(ChangeEvent e)
    //   {
    //     _ships[1].rotate((double)angles[1].getValue());
    //     repaint(_ships[1].getRect());
    //   }
    // });
    angles[1] = createSpinner(new SpinnerNumberModel(0.0,-180,180,1),1,_set._W-120,_set._H-100);
    angles[1].addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e)
      {
        _ships[1].rotate((double)angles[1].getValue());
        repaint(_ships[1].getRect());
      }
    });
    add(angles[1]);

    // forces[1].setOpaque(false);
    // forces[1].setBorder(BorderFactory.createLineBorder(new Color(255, 89, 230),2,true));
    // forces[1].getEditor().setOpaque(false);
    // ((JSpinner.NumberEditor)forces[1].getEditor()).getTextField().setOpaque(false);
    // ((JSpinner.NumberEditor)forces[1].getEditor()).getTextField().setFont(new Font("Verdana", Font.BOLD,14));
    // //forces[0].setValue(new Double(0));
    // ((JSpinner.NumberEditor)forces[1].getEditor()).getTextField().setForeground(new Color(250, 250, 250));
    // forces[1].setBounds(_set._W-120,_set._H-80,100,20);
    add(forces[1]);

    // butts[0] = new JButton("Shoot");
    // butts[0].addActionListener(this);
    // butts[0].setBounds(20,_set._H-60,100,20);
    // butts[0].setBorder(BorderFactory.createLineBorder(new Color(72, 160, 220),2,true));
    butts[0] = createButton(0, 20, _set._H-60);
    add(butts[0]);

    // butts[1] = new JButton("Shoot");
    // butts[1].addActionListener(this);
    // butts[1].setBounds(_set._W-120,_set._H-60,100,20);
    // butts[1].setBorder(BorderFactory.createLineBorder(new Color(255, 89, 230),2,true));
    butts[1] = createButton(1, _set._W-120,_set._H-60);
    butts[1].setEnabled(false);
    add(butts[1]);

    labels[0] = new JLabel(Integer.toString(_points[0]));
    labels[0].setFont(new Font("Verdana", Font.BOLD,40));
    labels[0].setForeground(_ships[0].getColor());
    labels[0].setBounds(_set._W-150,20,100,100);
    add(labels[0]);
    labels[1] = new JLabel(Integer.toString(_points[1]));
    labels[1].setFont(new Font("Verdana", Font.BOLD,40));
    labels[1].setForeground(_ships[1].getColor());
    labels[1].setBounds(_set._W-50,20,100,100);
    add(labels[1]);


  }

  public void loadGame()
  {
    _tr = new ArrayList<Trajectory>();
    ball = new Sfera[_set._planetnum];
    double dist;
    _ships = new Nave[2];

    _ships[0] = new Nave(_set._W, _set._H);
    _ships[0].setColor(new Color(72,160,220));
    do
    {
      _ships[1] = new Nave(_set._W, _set._H,"gw/20x20spshp.png");
      _ships[1].setColor(new Color(255, 89, 230));
      dist = Math.sqrt(Math.pow(_ships[0].getx() - _ships[1].getx(),2)+Math.pow(_ships[0].gety() - _ships[1].gety(),2));
    } while (dist < 2*_set._imgEdge+100f);

    // for (Nave s : _ships)
    // {
    //   s.Scale(_set._imgEdge);
    // }
    loadPlanets();

    loadMatrix();
  }
  private void loadMatrix()
  {
    for(int i=0; i<_set._W; i++) {
        	for(int j=0; j<_set._H; j++) {
    		try {
    			_ForceMatrix[i][j] = Forze(i,j,ball);
    			}
    		catch(Exception e) {
    			_ForceMatrix[i][j] = new Pair(-1,-1);
    		}
      }
    }
  }
  private void loadPlanets()
  {
    int i = 0;
    if (_set._inbetween)
    {
      do
      {
        double x = (_ships[1].getx()>_ships[0].getx())? _ships[0].getx()+10+(_ships[1].getx()+10 - (_ships[0].getx()+10))/2:_ships[1].getx()+10+(_ships[0].getx()+10 - (_ships[1].getx()+10))/2;
        double y = (_ships[1].gety()>_ships[0].gety())? _ships[0].gety()+10+(_ships[1].gety()+10 - (_ships[0].gety()+10))/2:_ships[1].gety()+10+(_ships[0].gety()+10 - (_ships[1].gety()+10))/2;

        ball[i] = new Sfera(x,y);
      }while(!ball[i].isValid(_ships,ball,i));
      i++;

    }
    for (i = i; i < _set._planetnum; i++)
    {
      do
      {
        ball[i] = new Sfera(_set._W,_set._H);
      } while(!ball[i].isValid(_ships,ball,i));
    }
  }
  public Dimension getPreferredSize()
  {
    return new Dimension(_set._W,_set._H);
  }
  // private void freccia(int x, int y)
  // {
  //   if (x != _x || y!= _y)
  //   {
  //     int hord,vertd;
  //     _x = x;
  //     _y=y;
  //     _ships[_focus].getMouse(x,y);
  //     repaint();
  //   }
  // }
  public void actionPerformed(ActionEvent evento)
  {
    if (evento.getSource() instanceof JButton) {
      double angle = (double)angles[_focus].getValue();
      double pewX = _ships[_focus].getx() + _ships[_focus].getR()/2 + ((Math.sin(Math.toRadians(angle)))*_ships[_focus].getR());
      double pewY = _ships[_focus].gety() + _ships[_focus].getR()/2 - ((Math.cos(Math.toRadians(angle)))*_ships[_focus].getR());
      pew = new Proiettile(pewX,pewY);
      _current = new Trajectory(_focus);
      _current.push(new Pair(pewX,pewY));

      pew.Shoot(angle,(double)forces[_focus].getValue());
      _ships[_focus].rotate(angle);
      _counter++;
      butts[_focus].setEnabled(false);
      _focus=_counter%2;
      butts[_focus].setEnabled(true);
      //TODO la riga qui sopra va spostata e copiata per ogni volta in cui il proiettile sparisce
      //per ora è qui per evitare di bloccare il gioco in caso il proiettile non colpisse nulla
    }
    if (pew != null)
    //	if(true)
    {
      if (pew.Hit(_ships[0]))
      {
        _current.push(new Pair(pew.getx(),pew.gety()));
        repaint();
        _tr.add(_current);
        _ships[1].increase();
        _points[1]++;
        labels[1].setText(Integer.toString(_points[1]));

        pew = null;
        _tr = new ArrayList<Trajectory>();
        _current= null;
        loadGame();
      }else if (pew.Hit(_ships[1]))
      {
        _current.push(new Pair(pew.getx(),pew.gety()));
        repaint();
        _tr.add(_current);
        _ships[0].increase();
        _points[0]++;
        labels[0].setText(Integer.toString(_points[0]));
        pew = null;
        _tr = new ArrayList<Trajectory>();
        _current= null;
        loadGame();

      }else if ( pew.Hit(ball))
      {
        double minx,miny,maxx,maxy;
        //repaint(pew.getx(),pew.gety(),pew.getPX(),pew.getPY());
        if (pew.getx() < pew.getPX())
        {
          minx = pew.getx();
          maxx = pew.getPX();
        } else
        {
          minx = pew.getPX();
          maxx = pew.getx();
        }
        if (pew.gety() < pew.getPY())
        {
          miny = pew.gety();
          maxy = pew.getPY();
        } else
        {
          miny = pew.getPY();
          maxy = pew.gety();
        }
        repaint((int)minx,(int)miny,(int)maxx,(int)maxy);
        _tr.add(_current);
        pew = null;
      }else {
        //pew.Forze(ball);
        //pew.Update();
        Pair forces = Forze(pew.getx(),pew.gety(),ball);
        if (Math.abs(forces.getx()) < 1e-4 && Math.abs(forces.gety()) < 1e-4)
        {
          _tr.add(_current);
          pew = null;
        }else
        {
          pew.Update(forces);
          _current.push(new Pair(pew.getx(),pew.gety()));
        }
        repaint();
        //repaint();
      }
    }
  }

  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    //navetta.paintIcon(this,g);
    Graphics2D g2d = (Graphics2D)g;
    double vertd, hord;
    vertd = (_y - _ships[_focus].gety())/3;
    hord = (_x - _ships[_focus].getx() )/3;

    g2d.drawImage(bg,0,0,null);

    if(_set._frecce)

    {

      for(int i=gap; i<_set._W; i+=2*gap) {
        for(int j=gap; j<_set._H; j+=2*gap) {
          Shape arrow = createArrowShape(new Pair(i,j),_ForceMatrix[i][j]);
          frc = risultante(_ForceMatrix[i][j]);
          frc_blue=(float)(1-(2*Math.atan(8*frc)/Math.PI));
          frc_red=(float)(2*Math.atan(8*frc)/Math.PI);
          clr = new Color(frc_red,0f,frc_blue);
          g2d.setColor(clr);
          g2d.draw(arrow);
        }
      }
    }

    _ships[0].paintComponent(g);
    _ships[1].paintComponent(g);
    for (Trajectory raj : _tr)
    {
      raj.paintComponent(g);
    }
    if (pew != null)
    {
      // Line2D punto;
      // for (Sfera b : ball)
      // {
      //   punto = new Line2D.Double(b.getx()+(b.getR()/2),b.gety()+(b.getR()/2),pew.getx(),pew.gety());
      //   g2d.draw(punto);
      // }
      pew.paintComponent(g);
      _current.paintComponent(g);
    }



    for (Sfera balla : ball)
    {
      balla.paintComponent(g);
    }
    _conteggio++;

    time.start();
  }

  public Pair Forze(double x, double y, Sfera[] palle)
  {
    double distx;
    double disty;
    double dist;
    double f;
    double fx = 0;
    double fy = 0;
    for (Sfera i : palle)
    {
      distx = x - (i.getx()+(i.getR()/2));
      disty = y - (i.gety()+(i.getR()/2));
      dist = Math.sqrt(distx*distx + disty*disty);
      f = -100*i.getM()/Math.pow(dist,2);
      fx += f * (double)(distx/dist);
      fy += f * (double)(disty/dist);
    }
    double ax = (fx*1/50);
    double ay = (fy*1/50);
    return new Pair(ax,ay);
  }

  public float risultante(Pair p)
  {
    double x,y, risultante;
    x= p.getx();
    y= p.gety();

    risultante = Math.sqrt(x*x + y*y);

    return (float)risultante;

  }
  // public Settings getSettings()
  // {
  //   s  = new Settings(_planetnum);
  //   return s;
  // }

  public Sad getSettings()
  {
    return _set;
  }

  public void setSettings(Sad set) // best name
  {
    //_planetnum = s.getNumber();
    _set = set;
    if (_set._modified)
    {
      pew = null;
      refreshUI();
      loadGame();
      repaint();
    }
    _set._modified = false;
  }

  public static Shape createArrowShape(Pair fromPt, Pair toPt) {

    Polygon arrowPolygon = new Polygon();
    arrowPolygon.addPoint(-6,0);
    arrowPolygon.addPoint(6,0);
    arrowPolygon.addPoint(3,1);
    arrowPolygon.addPoint(6,0);
    arrowPolygon.addPoint(3,-1);
    arrowPolygon.addPoint(6,0);
    arrowPolygon.addPoint(-6,0);


     //Pair MidPoint = midpoint(fromPt, toPt);
     //Point midPoint = new Point((int)MidPoint.getx(),(int)MidPoint.gety());


    double rotate = Math.atan2(toPt.gety(), toPt.getx());

    AffineTransform transform = new AffineTransform();
    transform.translate(fromPt.getx(), fromPt.gety());
    double ptDistance = Math.pow((Math.pow(toPt.gety() - fromPt.gety(),2))+(Math.pow(toPt.getx() - fromPt.getx(),2)),0.5);
    double scale = 2*Math.atan(ptDistance);
    transform.scale(scale, scale);
    transform.rotate(rotate);


    return transform.createTransformedShape(arrowPolygon);
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
        toSerialize nuova = new toSerialize();
        nuova.navi = _ships;
        nuova.sfere = ball;
        out.writeObject(nuova);
        out.close();
        file.close();
        out.writeObject(this);
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
        ball = eh.sfere;
        refreshUI();
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

import java.awt.Button;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrame {

    private JFrame frmSelectAreaIn;
    private JPanel selectedAreaPanel;
    private JPanel configuration;
    private String imagePath=" ";
    BufferedImage origImage = null;
    BufferedImage[] parqueos = null;
    public BufferedImage selectImage = null;
    Directory dir = new Directory();

    BufferedImage rotate =null;
    int cont=0;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame window = new MainFrame();
                    window.frmSelectAreaIn.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     * @throws IOException 
     */
    public MainFrame() throws IOException {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     * @throws IOException 
     */
    private void initialize() throws IOException {
        
        escogerimagen();
       
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        frmSelectAreaIn = new JFrame();
        frmSelectAreaIn.setTitle("Seleccionar Parqueo");
        frmSelectAreaIn.setBounds(0, 0,width-20,height-40);
        frmSelectAreaIn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmSelectAreaIn.getContentPane().setLayout(null);
        frmSelectAreaIn.setLocationRelativeTo(null);
        
        // Scalar Imagen
        
        // Panel del Parqueo Seleccionado
        selectedAreaPanel = new JPanel();
        selectedAreaPanel.setBounds(origImage.getWidth()+50, 50, 200, 200);
        frmSelectAreaIn.getContentPane().add(selectedAreaPanel);
        
        // Panel para Configurar
        configuration = new JPanel();
        configuration.setBounds(origImage.getWidth()+50, 500, 250, 250);
        frmSelectAreaIn.getContentPane().add(configuration);

        // Imagen de los Parqueaderos Frame
        JPanel mainPanel = new ImagePanel(imagePath, this);
        mainPanel.setBounds(11, 11,origImage.getWidth(),origImage.getHeight());
        frmSelectAreaIn.getContentPane().add(mainPanel);
        
        JLabel lblSelectedArea = new JLabel("Parqueo Seleccionado");
        lblSelectedArea.setBounds(origImage.getWidth()+50, 11, 221, 14);
        frmSelectAreaIn.getContentPane().add(lblSelectedArea);
        
        // Frame de Configuracion
        JLabel lblConfiguracion = new JLabel("Configuración");
        lblConfiguracion.setBounds(origImage.getWidth()+50,300, 221, 14);
        frmSelectAreaIn.getContentPane().add(lblConfiguracion);
       
        TextField txtGrados= new TextField("Grados");
        txtGrados.setBounds(origImage.getWidth()+160,360, 100, 30);
        frmSelectAreaIn.getContentPane().add(txtGrados);
        
        Button btnRotar= new Button("Rotar");
        btnRotar.setBounds(origImage.getWidth()+50, 350, 100, 50);
        frmSelectAreaIn.getContentPane().add(btnRotar);
        btnRotar.addActionListener( new ActionListener()
{
    @Override
    public void actionPerformed(ActionEvent e)
    {     
        rotate=rotateImageByDegrees(selectImage,Double.valueOf(txtGrados.getText()));
        Graphics g = configuration.getGraphics();
        g.clearRect(0,0, 250, 250);
        //g.drawImage(selectImage, 0, 0, null);
        g.drawImage(rotate,0, 0, null);
    }
});
        
        Button btnGuardar= new Button("Guardar");
        btnGuardar.setBounds(origImage.getWidth()+280, 350, 100, 50);
        frmSelectAreaIn.getContentPane().add(btnGuardar);
        btnGuardar.addActionListener( new ActionListener()
{
    @Override
    public void actionPerformed(ActionEvent e)
    {     
     File outputfile = new File("C:\\Users\\jessi\\Documents\\NetBeansProjects\\Parqueaderos\\Images\\"+cont+".png");
        try {
            ImageIO.write(rotate, "png", outputfile);
            cont++;
            JOptionPane.showMessageDialog(null, "El parqueo ha sido guardado",  "Parqueo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            System.out.println("Error de escritura");
        }
    }
});
        
        Button btnMostrar= new Button("Mostrar");
        btnMostrar.setBounds(origImage.getWidth()+390, 350, 100, 50);
        frmSelectAreaIn.getContentPane().add(btnMostrar);
        btnMostrar.addActionListener( new ActionListener()
{
    @Override
    public void actionPerformed(ActionEvent e)
    {     
        parqueos=dir.Parqueos();
        
        Parqueos frame = new Parqueos();
        frame.setTitle("Parqueos Guardados");
        frame.setBounds(0, 0,width-200,height-200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter(){
 
  //Este es el evento que se ejecuta cuando un JFrame se carga
  public void windowOpened(WindowEvent e){
      JPanel parqueosguardados = new JPanel();
      parqueosguardados.setBounds(20, 20, frame.getWidth()-40, frame.getHeight()-40);
      frame.getContentPane().add(parqueosguardados);
      int x=0 ,y=0;
      for (int i = 0; i < parqueos.length ;i++) {
        Graphics g = parqueosguardados.getGraphics();
        g.drawImage(parqueos[i],x,0, null);
        g.drawString(String.valueOf(i),x+25,200);
        x=x+100;
        
      }
   //Realizas lo que quieras
  }
});        
        
    }
});
        
        
    }
    
    
    public void escogerimagen() throws IOException{
    
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images","gif", "png", "bmp","jpg","jpeg");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
         imagePath= chooser.getSelectedFile().getAbsolutePath();  
        }
         origImage = ImageIO.read(chooser.getSelectedFile());
    }
    

    // function to update selected region of the image
    public void updateSelectedRegion(BufferedImage bufferedImage) {
        Graphics g = selectedAreaPanel.getGraphics();
        //selectImage=bufferedImage;
        g.clearRect(0, 0, 300, 300);
        g.drawImage(bufferedImage, 0, 0, null);
        
        Graphics g1 = configuration.getGraphics();
        g1.clearRect(0,0, 250, 250);
        
        
    }
    
    public static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
    }
    
            public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {

            double rads = Math.toRadians(angle);
            double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
            int w = img.getWidth();
            int h = img.getHeight();
            int newWidth = (int) Math.floor(w * cos + h * sin);
            int newHeight = (int) Math.floor(h * cos + w * sin);

            BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = rotated.createGraphics();
            AffineTransform at = new AffineTransform();
            at.translate((newWidth - w) / 2, (newHeight - h) / 2);

            int x = w / 2;
            int y = h / 2;

            at.rotate(rads, x, y);
            g2d.setTransform(at);
            g2d.drawImage(img, 0, 0, null);
       
            g2d.dispose();

            return rotated;
        }
}
    


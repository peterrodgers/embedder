package euler.library;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import euler.construction.ConstructedConcreteDiagram;


public class SelectDiagramFrame extends JFrame {
    
	private JLabel photographLabel;
    private JPanel buttonBar = new JPanel();
    private JToolBar imageBar = new JToolBar();    
    private String imagedir = "images/";    
    private MissingIcon placeholderIcon = new MissingIcon();
    private String selectedImage = null;
    private EulerDiagramWindow parentWindow = null;
    private String searchText = null;
    JPanel searchPanel = new JPanel();
    JButton search = new JButton("search");
    JTextField searchField;
    
    /**
     * List of all the descriptions of the image files. These correspond one to
     * one with the image file names
     */
    private String[] imageCaptions = { "a", "a b","a b c","ab", "a ab", "a b ab","a b ac",
    		"a b c ab","ab ac","a ab ac","a b ab ac","a b c ab ac","a bc","a ab bc",
    		"a b ac bc","ab bc ac","a ab ac bc","a b ab ac bc","a b c ab bc ac","abc",
    		"a abc","a b abc"," a b c abc","ab abc","a ab abc", "a b ab abc","a b ac abc",
    		"ab ac abc","a b c ab abc","a ab ac abc","a b ab ac abc","a b c ab ac abc",
    		"a bc abc","a ab bc abc","a b ac bc abc","ab ac bc abc","a ab ac bc abc",
    		"a b ab ac bc abc","a b c ab ac bc abc"};
    
    /**
     * List of all the image files to load.
     */
    private String[] imageFileNames = { "1.jpg", "2.jpg","3.jpg", "4.jpg","5.jpg","6.jpg",
    		"7.jpg","8.jpg","9.jpg","10.jpg","11.jpg","12.jpg","13.jpg",
    		"14.jpg", "15.jpg","16.jpg", "17.jpg","18.jpg","19.jpg","20.jpg",
    		"21.jpg","22.jpg","23.jpg","24.jpg","25.jpg","26.jpg",
    		"27.jpg", "28.jpg","29.jpg", "30.jpg","31.jpg","32.jpg","33.jpg",
    		"34.jpg","35.jpg","36.jpg","37.jpg","38.jpg","39.jpg",};
    
    /**
     * Main entry point to the demo. Loads the Swing elements on the "Event
     * Dispatch Thread".
     *
     * @param args
     */
   /* public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	SelectDiagramFrame app = new SelectDiagramFrame();
                app.setVisible(true);
            }
        });
    }*/
   
    /**
     * Default constructor for the demo.
     */
    public SelectDiagramFrame(EulerDiagramWindow edw) {
    	this.parentWindow = edw;
        this.initComponents();
    }   
    public String getSelectedDiagram(){return selectedImage;}
    public void initComponents(){
    	
    	   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           setTitle("3 Set Euler Diagram Library");
         
           JButton select = new JButton("select");
           buttonBar.add(select, BorderLayout.WEST);          
           searchField = new JTextField(searchText,20); 
           searchPanel.add(search, BorderLayout.WEST);
           searchPanel.add(searchField,BorderLayout.CENTER);                      
           buttonBar.add(searchPanel, BorderLayout.CENTER); 
           
           add(buttonBar,BorderLayout.NORTH);
           
           photographLabel = new JLabel();
           photographLabel.setSize(new Dimension((int)(this.getSize().getHeight()*0.8), (int)(this.getSize().getWidth()*0.8)));
           photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
           photographLabel.setHorizontalTextPosition(JLabel.CENTER);
           photographLabel.setHorizontalAlignment(JLabel.CENTER);
           photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
           
         //  add(photographLabel, BorderLayout.CENTER);
           	select.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent event) {    			
   				if(selectedImage!=null){
   					updatePareantFrame(selectedImage);	   		       
   				}
   		        else{
   		        	System.out.println("diagram not found in library");
   		        }
   				//dispose();
   			}});
           	search.addActionListener(new ActionListener() {
       			public void actionPerformed(ActionEvent event) {    			
       				selectedImage = searchField.getText();
       				updatePareantFrame(selectedImage);	
       				setTitle(selectedImage);
       			}});
       
           JScrollPane scrollPane = new JScrollPane(imageBar);
           scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
           scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);     
           add(scrollPane, BorderLayout.CENTER);
           
           setSize(500, 600);           
           setLocationRelativeTo(null);
         
           
           // start the image loading SwingWorker in a background thread
           loadimages.execute();
    }
    private void updatePareantFrame(String selectedDiagram){
    	ConstructedConcreteDiagram ccd = parentWindow.getLibrary().getDiagram(selectedDiagram);
	         if(ccd!=null){
	        	EulerDiagramPanel edp = parentWindow.getEulerDiagramPanel();
	        	edp.setConstructedConcreteDiagram(ccd);
	        	edp.resetDiagram(ccd);
	        	edp.update(edp.getGraphics());
	        }
    	
    }
    /**
     * SwingWorker class that loads the images a background thread and calls publish
     * when a new one is ready to be displayed.
     *
     * We use Void as the first SwingWroker param as we do not need to return
     * anything from doInBackground().
     */
    private SwingWorker<Void, ThumbnailAction> loadimages = new SwingWorker<Void, ThumbnailAction>() {
        
        /**
         * Creates full size and thumbnail versions of the target image files.
         */
        @Override
        protected Void doInBackground() throws Exception {
            for (int i = 0; i < imageCaptions.length; i++) {
                ImageIcon icon;
                icon = createImageIcon(imagedir + imageFileNames[i], imageCaptions[i]);
                
                ThumbnailAction thumbAction;
                if(icon != null){
                    
                    ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 200, 200));                    
                    thumbAction = new ThumbnailAction(icon, thumbnailIcon, imageCaptions[i]);
                    
                }else{
                    // the image failed to load for some reason
                    // so load a placeholder instead
                	System.out.println("error loading file " +imageFileNames[i]);
                    thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon, imageCaptions[i]);
                }
                publish(thumbAction);
            }
            // unfortunately we must return something, and only null is valid to
            // return when the return type is void.
            return null;
        }
        
        /**
         * Process all loaded images.
         */
        @Override
        protected void process(List<ThumbnailAction> chunks) {        		        
        	imageBar.setLayout(new GridLayout(0,2));
            for (ThumbnailAction thumbAction : chunks) {
                JButton thumbButton = new JButton(thumbAction);
                imageBar.add(thumbButton, imageBar.getComponentCount());               
            }
        }
    };
    
    /**
     * Creates an ImageIcon if the path is valid.
     * @param String - resource path
     * @param String - description of the file
     */
    protected ImageIcon createImageIcon(String path,
            String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Select Diagram...Couldn't find file: " + path);
            return null;
        }
    }
    
    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
    /**
     * Action class that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction{
        
        /**
         *The icon if the full image we want to display.
         */
        private Icon displayPhoto;
        
        /**
         * @param Icon - The full size photo to show in the button.
         * @param Icon - The thumbnail to show in the button.
         * @param String - The descriptioon of the icon.
         */
        public ThumbnailAction(Icon photo, Icon thumb, String desc){
            //displayPhoto = photo;
            
            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);
            
            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }
        
        /**
         * Shows the full image in the main area and sets the application title.
         */
        public void actionPerformed(ActionEvent e) {
         //   photographLabel.setIcon(displayPhoto);
        	selectedImage = getValue(SHORT_DESCRIPTION).toString();
        	setTitle(selectedImage);
         }
    }
}
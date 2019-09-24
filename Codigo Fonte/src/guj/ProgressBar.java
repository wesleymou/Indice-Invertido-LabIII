package guj;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBar extends JFrame { 

	private static final long serialVersionUID = -4668062439664829448L;
	// create a frame 
    private JFrame f;
    private JProgressBar b; 
  
    public void createProgressBar(String tittle) { 
    	
        f = new JFrame(tittle);  
        JPanel p = new JPanel(); 
        b = new JProgressBar(); 
  
        b.setValue(0); 
        b.setStringPainted(true); 

        p.setLayout(new GridLayout(1, 1));
   
        p.add(b); 
        f.add(p); 
   
        f.setSize(500, 100);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.setVisible(true);
    } 
    
    public void fill(int i) {
    	b.setValue(i);
    }
    
    public void closeProgressBar() {
//    	f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
		f.setVisible(false);
		f.dispose();
	}
} 
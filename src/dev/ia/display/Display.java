package dev.ia.display;

import javax.swing.JFrame;

import dev.ia.display.Drawmap;

public class Display{
	private JFrame frame;
	
	private String title;
	
	private int width, height;
	
	private Drawmap previousMap = null;
	
	public Display(String title, int width, int height){
		this.title = title;
		this.height = height;
		this.width = width;
		
		create();
	}
	
	public void create(){
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	public void add(Drawmap comp){
		
		if(previousMap != null) {
			frame.remove(previousMap);
		}
		
		frame.add(comp);
		frame.validate();
        frame.repaint();
        previousMap = comp;
	}
	
}

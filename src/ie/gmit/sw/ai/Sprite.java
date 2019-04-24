package ie.gmit.sw.ai;

import javax.imageio.*;
import java.awt.image.*;

public class Sprite{
	private String name; //The name of this sprite
	private BufferedImage[] frames; //The set of image frames to animate
 	private int index = 0; //Initial starting index in array
 	
 	
	public Sprite(String name,int num, String... images) throws Exception{
		this.name = name;
		this.index = 0; //Initialise the starting index to zero
		this.frames = new BufferedImage[images.length]; //Initialise the image frames
		
		for (int i = 0; i < images.length; i++){
			frames[i] = ImageIO.read(new java.io.File(images[i])); //Read in each image as a BufferedImage
		}
	}
	
	public Sprite(String string, String string2) {
		// TODO Auto-generated constructor stub
	}

	public BufferedImage getNext(){ //Returns the next image frame
		int idx = index;
		if (index < frames.length - 1){
			index++;
		}else{
			index = 0; //Circle back to the start of the array
		}
		
		return frames[idx]; 
	}
	
	public void setImageIndex(int idx){
		this.index = idx;
	}
	
	public String getName(){
		return this.name;
	}
}

//Aya Yahya

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.stream.IntStream;


public class Normal {

	  static Timer timer = new Timer();     // Timer to esure the time of For loop nd SIMD
	  static int seconds = 0;
	    
	public static void main(String[] args) throws IOException {
	
		long startTime = System.nanoTime();   // Fire the timer
		
		vectorImage();                        //call the function that run the program as SIMD
	
		long endTime = System.nanoTime();     // stop the timer

		long duration = (endTime - startTime); // calculate the time of the program 
		System.out.println("Timer with SIMD running : " + duration/1000000 + " micro second");  // print the duration time
		 
	     startTime = System.nanoTime();     // reset the timer again to run it with For loops
	     imageForLoop();					 //call the function that run the program with for loops
	
		endTime = System.nanoTime();          // stop the timer

		 duration = (endTime - startTime); 		// calculate the time of the program 
		System.out.print("Timer with for loops running : " + (duration/1000000) + " micro second");  // print the duration time
		
	}
	
	
	// function that use SIMD operation
	
	public static void vectorImage() throws IOException {
		
		FileWriter myWriter = new FileWriter("imageSIMD.ppm");    // create a ppm image
		int row = 400;
		int column = 400;
	
		myWriter.append("P3\n" + row + " " + column + "\n255\n");    // append to the begging of the file 

		Vector<Integer> index = new Vector<Integer>(400 *400);     //initialize vector of size 400* 400 that will contain the index of each element
	
		int[] indexing = IntStream.range(0, 400*400).toArray();     // array of size 400*400 have values from 0 to 400*400 
		
		Integer[] array = Arrays.stream( indexing ).boxed().toArray( Integer[]::new );   // convert the array type needed to convert the array to vector
	
		Collections.addAll(index, array );      // add all elements  on the array to the vector

		index.forEach((element) -> {               // vector foreach function take every element in the array and apply the same sequence of operation on it  "SIMD"
			
	      int modula = element%400;               // calculate the column of the element
		  int i = element / 400;					// calculate the row of the element
			
		  float x = (float) i / (float) 400;		//calculate x 
		  float y = (float) modula / (float) 400;	//calculate y
		  float z = (float) 0.5;					//calculate z
		  int red =  (int) (x * 256);				//calculate the red value of the pixel
		  int green = (int) (y * 256);				//calculate the green value of the pixel
		  int blue = (int) (z * 256);				//calculate the blue value of the pixel
	
		  String pixel = red + " " + green + " "+ blue +"  ";    // initialize the pixel
		  
		  // write the pixel to the .ppm file
		  try {
			myWriter.append(pixel);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		});
	
		 myWriter.close();     //close the file

	}
	
	
	// function that run the program sequential with for loops
	public static void imageForLoop() throws IOException {
		
		FileWriter myWriter = new FileWriter("image.ppm");   // create a ppm image
		int row = 400;
		int column = 400;
		String out = "";                    // String that will save the pixels 

		out = out + "P3\n" + row + " " + column + "\n255\n";    // append to the begging of the file 

	
		
		for ( int i = 0 ; i < row; i++) { 					// for loop that go through the image rows
			for ( int j  = 0 ;j < column ; j++) {           // for loop that go through the image columns
				float x = (float) i / (float) row ;			//calculate x
				float y = (float) j /(float) column;		//calculate y
				float z = (float) 0.5;						//calculate x
				
				int r = (int) (256 * x);					//calculate the red value of the pixel
				int g = (int) (256 * y);					//calculate the green value of the pixel
				int b = (int) (256 * z);					//calculate the blue value of the pixel
				out = out + ""+ r +" "+g+" "+b+"  \n";		 // initialize the pixel and add it to other pixels
			}                           
		}
		
		myWriter.write(out);								// write the ppm image file
		 myWriter.close();									//close the ppm file
	}
	
	//timer configuration function
	
		 public static void MyTimer() {

		        TimerTask task;

		        task = new TimerTask() {
		            @Override
		            public void run() { 
		                while (seconds < 100) {
		                    System.out.println("Seconds = " + seconds);
		                    seconds++;
		                }
		            }
		        };
		         timer.schedule(task, 0, 1000);

		    }

		
}



/* Sample output 
Timer with SIMD running : 325 micro second
Timer with for loops running : 106359 micro second*/
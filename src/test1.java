import java.util.Scanner;


public class test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		while(true){
			
			Scanner s = new Scanner(System.in);
			String line = s.nextLine();
			if(line.matches("^\\d{1,8}+([.](\\d{0,2}))?$")){
				System.out.println("true");
			}else{
				System.out.println("false");
				
			}
			//Enumeration 
		}
	}

}

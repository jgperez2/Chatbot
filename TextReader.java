import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;



public class TextReader {

	public static File[] getFiles(Scanner scnr, String directory) {
		
		File[] files = new File(directory).listFiles();
		File[] userFiles;
        int index = 1;
        
        System.out.println("\nFiles:");
        for(File file : files) {

        	String fileT = file.toString();
        	int bckSlsh = fileT.lastIndexOf('\\');
        	String path = fileT.substring(bckSlsh + 1, fileT.length());
        	System.out.println(index + ": " + path);
        	index++;
        }
        
        System.out.println("\nSelect file number(s) (" + 1 + "-" + files.length + "):");
        String userSel = scnr.next(); 
        
        userSel.replace(',', ' ');
        String[] userSelection = userSel.split(" ");
        int[] userInts = new int[userSelection.length];
        index = 0;
        
        
        try{
        	for (String user: userSelection){
        		System.out.println("Run #" + index + "A");
        		userInts[index] = Integer.parseInt(user.trim());
        		System.out.println("Run #" + index + "B");
        		index++;
        	}
        	
        }catch(Exception e){
        	System.out.println("XXXXXXX_FAIL_XXXXXXX");
        }
        
        //fuck
        index = 0;
        for(File file : files) {
        	System.out.println(index + ": " + file);
        	index++;
        }
        //fuck
        
        userFiles = new File[userInts.length];
        index = 0;
		for (int ints: userInts) {
			
			System.out.print(ints + ", ");
			
//			userFiles[index] = files[ints-1];
//			index++;
		}
        
        
		return userFiles;
	}
	
	public static String separate(String words) {
		
		words = words.replaceAll(".", " . ");
        words = words.replace(",", " , ");
        words = words.replace("?", " ? ");
        words = words.replace("!", " ! ");
        words = words.replace("@", " @ ");
        words = words.replace("#", " # ");
        words = words.replace("$", " $ ");
        words = words.replace("%", " % ");
        words = words.replace("^", " ^ ");
        words = words.replace("&", " & ");
        words = words.replace("*", " * ");
        words = words.replace("(", " ( ");
        words = words.replace(")", " ) ");
        words = words.replace("-", " - ");
        words = words.replace("_", " _ ");
        words = words.replace("\\", " \\ ");
        words = words.replace("\"", " \" ");
        words = words.replace('/', ' ');
		
		return words;
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
	        Map<String, Word> myMap = new HashMap<String, Word>();
//	        myMap.put(, "b");
//	        myMap.put("c", "d");
	        
	        
	        
	        Scanner userInput = new Scanner(System.in);

	        System.out.print("\nSelect Directory:  ");
//	        String directory = userInput.next(); 
	        String directory = ".\\@PostMalone"; 
	        
	        File[] usrFiles = getFiles(userInput, directory);
	        
	        
	        String wholeFile = "";
	        String line = null;
	        
	        for(File file: usrFiles) {
		        try {
		            // FileReader reads text files in the default encoding.
		            FileReader fileReader = 
		                new FileReader(file);
	
		            // Always wrap FileReader in BufferedReader.
		            BufferedReader bufferedReader = 
		                new BufferedReader(fileReader);
	
		            while((line = bufferedReader.readLine()) != null) {
		                wholeFile += line + "\n";
	
		            }   
	
		            // Always close files.
		            bufferedReader.close();         
		        }
		        catch(FileNotFoundException ex) {
		            System.out.println(
		                "Unable to open file '" + 
		                file + "'");                
		        }
		        catch(IOException ex) {
		            System.out.println(
		                "Error reading file '" 
		                + file + "'");                  
		            // Or we could just do this: 
		            // ex.printStackTrace();
		        }
		       
		        wholeFile = separate(wholeFile);
		        
	        }
	        

	        System.out.println(wholeFile);
	        
	        
	        Scanner scnr= new Scanner(wholeFile);
	        
	        Word poop = new Word(scnr.next().toLowerCase());
	        myMap.put(poop.getString(), poop);
	        int cnt = 1;
	        int Dcnt = 1;	        
	        
	        while(scnr.hasNext()) {
	        	String n = scnr.next().toLowerCase();
	        	if (myMap.containsKey(n)) {
	        		myMap.get(n).updateCount();
	        		cnt++;
	        	}else {
	        		myMap.put(n, new Word(n));
	        		Dcnt++;
	        	}
	        }
	        
	        Object[] bone = myMap.values().toArray();
	        ArrayList<Integer> intList = new ArrayList<Integer>();
	        for (int j = 0; j < bone.length; j++) {
	        	intList.add(Integer.parseInt(bone[j].toString()));
	        }
	       
	        
	        Collections.sort(intList);
	        
	        System.out.println("\n\n\n" + intList.size() + "\n\n\n\n");
	        
	        for (int i = 0; i < intList.size(); i++) {
	        	System.out.println(intList.get(i));
	        }
	        
	        PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
	        
	        for (String name: myMap.keySet()) {
	        	writer.println(myMap.get(name).toStringg());
	        }
	        
	        System.out.println("Distinct count: " + Dcnt + "\nTotal Count: " + cnt);
	        
	        writer.close();
	        
//	        for (String name: myMap.keySet()) {
//	        	System.out.println(myMap.get(name).toStringg());
//	        }
//	        System.out.println("Distinct count: " + Dcnt + "\nTotal Count: " + cnt);
	}

}

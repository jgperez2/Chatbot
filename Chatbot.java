import java.util.*;
import java.io.*;

public class Chatbot{
	
    private static String filename = "./@PostMalone/Oct17_17_811pm.txt";
    
    private static ArrayList<Integer> readCorpus(){
        ArrayList<Integer> corpus = new ArrayList<Integer>();
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                if(sc.hasNextInt()){
                    int i = sc.nextInt();
                    corpus.add(i);
                }                
                else{
                    sc.next();
                }
            }
            sc.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return corpus;
    }
    
    public static int count(int w, ArrayList<Integer> corpus){
    	int count = 0;
    	for (int i = 0; i < corpus.size(); i++){
    		if (corpus.get(i) == w){
    			count++;
    		}
    	}
    	return count;
    }    
    
    public static double getProbability(int w, ArrayList<Integer> corpus){
    	int count = count(w, corpus);
    	return count/(double)corpus.size();
    }
    
    public static HashMap<Integer, Double>	setBounds(ArrayList<Integer> corpus, HashMap<Integer, Double> hmapIndex){
    	// Stores the current probability upper bound of the last index added to the hashmap
    	double curProb = 0.0;
    	
    	//sets each index, with a non-zero probability, with it's upper bound in probability
    	// (you can get the lower bound by looking at the previous index with a non-zero probability)
    	for (int i = 0; i < 4700; i++){
    		double prob = getProbability(i, corpus);
    		
    		if((prob - .000001) > 0){
    			curProb += prob;			
    			hmapIndex.put(i, curProb);
    		}
    	}
    	return hmapIndex;
    }
    
    public static int findWord(HashMap<Integer, Double> hmap, double roll, ArrayList<Integer> keys) {
    	
    	int token = 0;
    	if (keys == null) {
	    	int guess = (int) Math.round(roll * hmap.size()) - 1;
	    	int posneg = 1;
	    	int bound = hmap.size();
	    	
	    	if(hmap.get(guess) > roll) {
	    		bound = 0;
	    		posneg = -1;
	    	}
	    	
	    	for (int i = guess; true ; i += posneg) {
	    		if(i == 0 && hmap.get(i) >= roll) {
	    			token = i;
	    			break;
	    		}
	    		else if (hmap.get(i-1) < roll && hmap.get(i) >= roll) {
	    			token = i;
	    			break;
	    		}
	    	}
    	}
    	else {
    		
    		for (int i = 0; i < keys.size(); i++) {
    			if(i == 0) {
	    			if(hmap.get((keys.get(i))) >= roll) {    				
	    				token = keys.get(i);
	    				break;
	    			}
    			}
		    	else if (hmap.get(keys.get(i-1)) < roll && hmap.get(keys.get(i)) >= roll) {
		    			token = keys.get(i);
		    			break;
		    	}
	    	}
    	}
    	
    	return token;
    }
    
    public static ArrayList<Integer> findPairs(int word, ArrayList<Integer> corpus){
    	
    	ArrayList<Integer> pairs = new ArrayList<>();
    	
		for (int i = 0; i < corpus.size(); i ++) {
			
			if (corpus.get(i) == word && (i + 1) < corpus.size()) {
				pairs.add(corpus.get(i+1)); 
			}
			
		}
		
		return pairs;
    }
    
    static private ArrayList<Integer> findTrio(int h1, int h2, ArrayList<Integer> corpus) {
		ArrayList<Integer> trios = new ArrayList<>();

		for (int i = 0; i < corpus.size() - 2; i ++) {
			if (corpus.get(i) == h1 && corpus.get(i+1) == h2) {
				trios.add(corpus.get(i+2)); 
			}
		}
		
		return trios;
    }
    
    static public void main(String[] args){
    	HashMap<Integer, Double> hmapIndex = new HashMap<Integer, Double>();
        HashMap<Integer, Double> hmapPairs = new HashMap<Integer, Double>();
        HashMap<Integer, Double> hmapTrios = new HashMap<Integer, Double>();
        
    	ArrayList<Integer> corpus = readCorpus();
		int flag = Integer.valueOf(args[0]);
        
        if(flag == 100){
			int w = Integer.valueOf(args[1]);
            int count = 0;
            count = count(w, corpus);
            
            System.out.println(count);
            System.out.println(String.format("%.7f",getProbability(w, corpus)));
        }
        else if(flag == 200){
        	hmapIndex = setBounds(corpus, hmapIndex);
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int token = findWord(hmapIndex, n1/(double)n2, null);
            System.out.println(token);
            if (token != 0) {
            	System.out.println(String.format("%.7f",hmapIndex.get(token-1)));
            }
            else {
            	System.out.println(String.format("%.7f", 0.0));
            }
			System.out.println(String.format("%.7f",hmapIndex.get(token)));
        }
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            int count = 0;
            ArrayList<Integer> words_after_h = new ArrayList<Integer>();
            words_after_h = findPairs(h, corpus);
            count  = count(w, words_after_h);
            //output 
            System.out.println(count);
            System.out.println(words_after_h.size());
            System.out.println(String.format("%.7f",count/(double)words_after_h.size()));
        }
        else if(flag == 400){   	
        	int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            ArrayList<Integer> words_after_h = new ArrayList<Integer>();
            words_after_h = findPairs(h, corpus);
            
            hmapPairs = setBounds(words_after_h, hmapPairs);
            
            ArrayList<Integer> keys = new ArrayList<Integer>();
    		for (Integer k: hmapPairs.keySet()) {
    			keys.add(k);
    		}
            Collections.sort(keys);
            
            int token = findWord(hmapPairs, n1/(double)n2, keys);
            System.out.println(token);
            
            if (token != 0) {
            	System.out.println(String.format("%.7f", hmapPairs.get(keys.get(keys.indexOf(token) - 1))));
            }
            else {
            	System.out.println(String.format("%.7f", 0.0));
            }
			System.out.println(String.format("%.7f", hmapPairs.get(token)));
            
        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            int count = 0;
            ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
            
            words_after_h1h2 = findTrio(h1, h2, corpus);
            count = count(w, words_after_h1h2);
            
            //output 
            System.out.println(count);
            System.out.println(words_after_h1h2.size());
            if(words_after_h1h2.size() == 0)
                System.out.println("undefined");
            else
                System.out.println(String.format("%.7f",count/(double)words_after_h1h2.size()));
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            ArrayList<Integer> words_after = new ArrayList<Integer>();
            
            words_after = findTrio(h1, h2, corpus);
            if(words_after.size() == 0) {
            	System.out.println("undefined");
            }
            else{
            	hmapTrios = setBounds(words_after, hmapTrios);

            	ArrayList<Integer> keys = new ArrayList<Integer>();
            	for (Integer k: hmapTrios.keySet()) {
            		keys.add(k);
            		
            	}
            	Collections.sort(keys);
            	
            	int token = findWord(hmapTrios, n1/(double)n2, keys);

            	System.out.println(token);
            	
            	if (token == 0 || keys.indexOf(token) == 0) {
            		System.out.println(String.format("%.7f", 0.0));
            	}
            	else {
            		System.out.println(String.format("%.7f", hmapTrios.get(keys.get(keys.indexOf(token) - 1))));
            	}
            	System.out.println(String.format("%.7f", hmapTrios.get(token)));
            }
            
        }
        else if(flag == 700){
        	hmapIndex = setBounds(corpus, hmapIndex);
        	int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1=0,h2=0;

            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);

            if(t == 0){
                double r = rng.nextDouble();
                
                h1 = findWord(hmapIndex, r, null);
                
                System.out.println(h1);
                if(h1 == 9 || h1 == 10 || h1 == 12){
                    return;
                }

                r = rng.nextDouble();
                
                ArrayList<Integer> words_after_h = new ArrayList<Integer>();
                words_after_h = findPairs(h1, corpus);
                
                hmapPairs = setBounds(words_after_h, hmapPairs);
                
                ArrayList<Integer> keys = new ArrayList<Integer>();
        		for (Integer k: hmapPairs.keySet()) {
        			keys.add(k);
        		}
                Collections.sort(keys);
                
                h2 = findWord(hmapPairs, r, keys);
                
                System.out.println(h2);
            }
            else if(t == 1){
                h1 = Integer.valueOf(args[3]);
                double r = rng.nextDouble();

                ArrayList<Integer> words_after_h = new ArrayList<Integer>();
                words_after_h = findPairs(h1, corpus);
                
                hmapPairs = setBounds(words_after_h, hmapPairs);
                
                ArrayList<Integer> keys = new ArrayList<Integer>();
        		for (Integer k: hmapPairs.keySet()) {
        			keys.add(k);
        		}
                Collections.sort(keys);
                
                h2 = findWord(hmapPairs, r, keys);
                
                System.out.println(h2);
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while(h2 != 9 && h2 != 10 && h2 != 12){
                double r = rng.nextDouble();
                int w  = 0;

                ArrayList<Integer> words_after = new ArrayList<Integer>();
                words_after = findTrio(h1, h2, corpus);
                
                if(words_after.size() == 0) {
                	System.out.println("Undefined");
                	hmapPairs.clear();
                	ArrayList<Integer> words_after_h = new ArrayList<Integer>();
                    words_after_h = findPairs(h2, corpus);
                    
                    hmapPairs = setBounds(words_after_h, hmapPairs);
                    
                    ArrayList<Integer> keys = new ArrayList<Integer>();
            		for (Integer k: hmapPairs.keySet()) {
            			keys.add(k);
            		}
                    Collections.sort(keys);
                    
                    w = findWord(hmapPairs, r, keys);
                }
                else{
                	hmapTrios = setBounds(words_after, hmapTrios);

                	ArrayList<Integer> keys = new ArrayList<Integer>();
                	for (Integer k: hmapTrios.keySet()) {
                		keys.add(k);
                		
                	}
                	Collections.sort(keys);
                	
                	w = findWord(hmapTrios, r, keys);
                	keys.clear();
                	hmapTrios.clear();
                }
                
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }

        return;
    }
}
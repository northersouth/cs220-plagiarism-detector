package plagdetect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.*;
import java.util.Map;

public class PlagiarismDetector implements IPlagiarismDetector {
	
	private int N_CONST;
	private HashMap<String, HashSet<String>> processedFiles = new HashMap();
	
	public PlagiarismDetector(int n) {
		N_CONST = n;

	}
	
	@Override
	public int getN() {
		return N_CONST;
	}

	@Override
	public Collection<String> getFilenames() {
		return processedFiles.keySet();
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		return processedFiles.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		return processedFiles.get(filename).size();
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		
		Set<String> temp1 = processedFiles.keySet();
		Set<String> temp2 = processedFiles.keySet();
		
		Iterator<String> check1 = temp1.iterator();
		Iterator<String> check2 = temp2.iterator();
		
		Map<String, Map<String, Integer>> theResults = new HashMap<String, Map<String, Integer>>();
		
		while(check1.hasNext()) {
			Map<String, Integer> meta = new HashMap<String, Integer>();
			String outer = (String) check1.next();
			while(check2.hasNext()) {
				String inner = (String) check2.next();
				if(outer.equals(inner)) {
					meta.put(inner,  -1);
				} else {
					meta.put(inner, this.getNumNGramsInCommon(outer, inner));
				}
			}
			theResults.put(outer, meta);
			check2 = temp2.iterator();
		}
		
		
		return theResults;
	}

	@Override
	public void readFile(File file) throws IOException {
		
		Scanner tempScan = new Scanner(file);
		List<String> words = new ArrayList<String>();
		//put all words and punctuation into a list 
		while(tempScan.hasNext()) {
			words.add(tempScan.next());
		}
		
		tempScan.close();
		
		
		HashSet<String> tempSet = new HashSet<String>();
		
		for(int i = N_CONST-1; i < words.size(); i++) {
			int countDown = N_CONST-1;
			String tempStr = "";
			
			while(countDown >= 0) {
				tempStr = tempStr.concat(words.get(i-countDown)) + " ";
				countDown--;
			}
			tempSet.add(tempStr.substring(0,tempStr.length()-1));
			
			//periods are always the end of an n-gram
			if(words.get(i).equals(".") || words.get(i).equals("?") || words.get(i).equals("!")) {
				i+=N_CONST-1;
			}
		}
		
		processedFiles.put(file.getName(), tempSet);
		
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		int counter = 0;
		
		Collection<String> temp1 = this.getNgramsInFile(file1);
		Collection<String> temp2 = this.getNgramsInFile(file2);
		
		Iterator<String> check1 = temp1.iterator();
		
		while(check1.hasNext()) {
			if(temp2.contains(check1.next())) {
				counter++;
			}
		}
		
		return counter;
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		Map<String, Map<String, Integer>> thePairs = this.getResults();
		
		Collection<String> sus = new ArrayList<String>();
		
		Set<String> keys1 = thePairs.keySet();
		Iterator<String> outer = keys1.iterator();
		
		while(outer.hasNext()) {
			String temp1 = (String) outer.next();
			Set<String> keys2 = thePairs.get(temp1).keySet();
			Iterator<String> inner = keys2.iterator();
			while(inner.hasNext()) {
				String temp2 = (String) inner.next();
				if(thePairs.get(temp1).get(temp2) >= minNgrams) {
					sus.add(temp1 + " " + temp2 + " " + thePairs.get(temp1).get(temp2));
				}
			}
			inner = keys2.iterator();
		}
		
		return sus;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File f : dir.listFiles()) {
			readFile(f);
		}
	}
}

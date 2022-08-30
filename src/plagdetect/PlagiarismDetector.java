package plagdetect;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.*;
import java.util.Map;

public class PlagiarismDetector implements IPlagiarismDetector {
	
	private int N_CONST;
	private HashMap<String, HashSet<String>> processedFiles;
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method
		
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		// TODO Auto-generated method stub
		return null;
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

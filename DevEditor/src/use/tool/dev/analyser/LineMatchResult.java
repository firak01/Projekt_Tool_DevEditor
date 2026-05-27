package use.tool.dev.analyser;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LineMatchResult {
	private File objFile=null;
	private Path objPath=null;
	private List<LineMatch> listLineMatchOuter = new ArrayList<LineMatch>();
	
	public LineMatchResult() {		
	}
	
	public LineMatchResult(File objFile) {
		this.objFile = objFile;
	}
	
	public LineMatchResult(Path objPath) {
		this.objPath = objPath;
	}
	
	
	
	//#############################
	public void setFile(File objFile) {
		this.objFile = objFile;
	}
	public File getFile() {
		return this.objFile;
	}
	
	public void setPath(Path objPath) {
		this.objPath = objPath;
	}
	public Path getPath() {
		return this.objPath;
	}
	
	public void setLineMatch(List<LineMatch> listLineMatchOuter) {
		this.listLineMatchOuter = listLineMatchOuter;
	}
	public List<LineMatch> getLineMatch(){
		return this.listLineMatchOuter;
	}
	
}

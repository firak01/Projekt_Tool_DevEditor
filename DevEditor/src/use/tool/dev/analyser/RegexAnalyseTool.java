package use.tool.dev.analyser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractList.ArrayListUtilZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;

public class RegexAnalyseTool {

    // Regex: fängt vec...get(Ziffer) ein
    private static final Pattern P = Pattern.compile("(vec\\w*\\.get\\([0-9]\\))");

    // Replacement: korrekt geklammert
    private static final String REPLACEMENT = "((Object)$1).toString()";

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException, ExceptionZZZ {
        if (args.length < 1) {
            System.out.println("Verwendung:");
            System.out.println("  java RegexReplaceTool <pfad-zum-ordner>");
            System.out.println("  java RegexReplaceTool <Regex-Ausdruck>");
            return;
        }else {
        	System.out.println("Starting using file/directory:");        	
        }

        Path inputPath = Paths.get(args[0]);
        System.out.println(inputPath.toString());
        
        String sRegex = args[1];
        
       
        //final List<LineMatch> listLineMatchOuter = new ArrayList<LineMatch>();
        final List<LineMatchResult> listLineMatchResult = new ArrayList<LineMatchResult>();
        if (Files.isDirectory(inputPath)) {
            // Alle .txt-Dateien im Verzeichnisbaum durchlaufen
            Files.walkFileTree(inputPath, new SimpleFileVisitor<Path>() {
            
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    if (path.toString().endsWith(".txt")) {
                    	List<LineMatch>listLineMatch = processFile(path, sRegex);
                    	if (listLineMatch != null) {
                        //      listLineMatchOuter.addAll(listLineMatch);
                    		LineMatchResult objLineMatchResult = new LineMatchResult(path);
                    		objLineMatchResult.setLineMatch(listLineMatch);
                    		listLineMatchResult.add(objLineMatchResult);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else if (Files.isRegularFile(inputPath) && inputPath.toString().endsWith(".txt")) {
            // Nur eine konkrete Datei verarbeiten
        	 List<LineMatch>listLineMatch=null;
        	 listLineMatch = processFile(inputPath, sRegex);
        	 if (listLineMatch != null) {
                 //      listLineMatchOuter.addAll(listLineMatch);
             		LineMatchResult objLineMatchResult = new LineMatchResult(inputPath);
             		objLineMatchResult.setLineMatch(listLineMatch);
             		listLineMatchResult.add(objLineMatchResult);
             }
        } else {
            System.out.println("Ungültiger Pfad: " + inputPath);
        }
        
        
        //#### Ergebnis
        boolean bRegardPoStrict = true;
        for(LineMatchResult objResult : listLineMatchResult) {
        	System.out.println(objResult.getPath().getFileName());
        	        	
        	if(bRegardPoStrict) {
	        	String sPO = StringZZZ.mid(objResult.getPath().getFileName().toString(),3,"-");        	
	        	
	        	List<LineMatch>listaResult = objResult.getLineMatch();
	        	for(LineMatch lineResult : listaResult) {
	        		String sLineContent = lineResult.getLineContent();
	        		if(!StringZZZ.contains(sLineContent,"-"+sPO+"-")) {
	        			System.out.println(lineResult.getLineNumber() + "\t" + sLineContent);
	        		}
	        	}
	        	System.out.println("\n");
        	}else {
        		//Vertiefung als Unterschied 
        		String sPO = StringZZZ.left(objResult.getPath().getFileName().toString(),9);
            	
        		List<LineMatch>listaResult = objResult.getLineMatch();
	        	for(LineMatch lineResult : listaResult) {
	        		String sLineContent = lineResult.getLineContent();
	        		if(!StringZZZ.contains(sLineContent,sPO)) {
	        			System.out.println(lineResult.getLineNumber() + "\t" + sLineContent);
	        		}
	        	}
	        	System.out.println("\n");
        	}
        }
        
    }

    private static List<LineMatch> processFile(Path file, String sRegEx) {
    	
    		List <LineMatch> listaLineMatch = RegexAnalyseTool.searchFile(file, sRegEx);
    		return listaLineMatch;
    }
    
    public static List<LineMatch> searchFile(Path file, String sRegEx) {
        List<LineMatch> result = new ArrayList<LineMatch>();

        try {
            System.out.println("Reading File:"+file.toString());
            Pattern pattern = Pattern.compile(sRegEx);

            List<String> lines = Files.readAllLines(file, UTF8);

            int lineNumber = 1;

            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    result.add(new LineMatch(lineNumber, line));
                }

                lineNumber++;
            }

        } catch (IOException e) {
            System.err.println("Fehler bei Datei " + file + ": " + e.getMessage());
        }

        return result;
    }
}

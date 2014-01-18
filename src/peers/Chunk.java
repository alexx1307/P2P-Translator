package peers;

public class Chunk {
	private String line;
	private int number;
	
	public Chunk(String line, int number){
		this.line=line;
		this.number=number;
	}
	
	public String getLine(){
		return line;
	}
	
	public int getNumber(){
		return number;
	}
}

public class Word{
	String word;
	Integer count;

	public Word(String word) {
		this.word = word;
		this.count = 1;
	}
	
	public String getString() {
		return this.word;
	}
	
	public Integer getCount() {
		return this.count;
	}
	
	public int compareTo(Word other) {
		if (other.getString().equals(this.word)) {
			return 1;
		}
		return 0;
	}
	
	public void updateCount() {
		this.count++;
	}
	
	public String toStringg() {
		return word + ": " + count;
	}
	
	public String toString() {
		return "" + count;
	}
}

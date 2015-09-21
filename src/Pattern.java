import java.io.Serializable;
/**
 * Used for making pattern objects that are made up of 
 * a string.
 * @author Nicholas Grant
 *
 */
public class Pattern implements Serializable {
	/**
	 * Represents the pattern
	 */
	private String pattern;
	/**
	 * Pattern constructor
	 * @param p		String of pattern
	 */
	public Pattern( String p ) {
		pattern = p;
	}
	/**
	 * Gets the string pattern
	 * @return		String pattern
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * Overrides the inherent hashCode() method for use in HashMap
	 */
	@Override
	public int hashCode() {
		return pattern.hashCode();
	}
	/**
	 * Overrides the inherent equals() method for use in HashMap
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Pattern)) {
			return false;
		}
		Pattern p = (Pattern) obj;
		return pattern.equals(p.getPattern());
	}
	

}

/*  Condition class

 Constructing Intelligent Agents with Java
 (C) Joseph P.Bigus and Jennifer Bigus 1997

 */

public class Condition {
	int index;
	String symbol;

	Condition(String Symbol) {
		symbol = Symbol;
		if (Symbol.equals("="))
			index = 1;
		else if (Symbol.equals(">"))
			index = 2;
		else if (Symbol.equals("<"))
			index = 3;
		else if (Symbol.equals("!="))
			index = 4;
		else
			index = -1;
	}

	String asString() {
		String temp = new String();
		switch (index) {
		case 1:
			temp = "=";
			break;
		case 2:
			temp = ">";
			break;
		case 3:
			temp = "<";
			break;
		case 4:
			temp = "!=";
			break;
		}
		return temp;
	}
}

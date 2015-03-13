/*  Clause class

  Constructing Intelligent Agents with Java
   (C) Joseph P.Bigus and Jennifer Bigus 1997
    

 */

import java.util.Vector;

public class Clause {
	Vector<Rule> ruleRefs;
	RuleVariable lhs;
	String rhs;
	Condition cond;
	Boolean consequent; // true or false
	Boolean truth; // states = null(unknown), true or false

	Clause(RuleVariable Lhs, Condition Cond, String Rhs) {
		lhs = Lhs;
		cond = Cond;
		rhs = Rhs;
		lhs.addClauseRef(this);
		ruleRefs = new Vector<Rule>();
		truth = null;
		consequent = new Boolean(false);
	}

	void addRuleRef(Rule ref) {
		ruleRefs.addElement(ref);
	}

	Boolean check() {
		if (consequent.booleanValue() == true)
			return null;
		if (lhs.value == null) {
			return truth = null; // can't check if variable value is undefined
		} else {

			switch (cond.index) {
			case 1:
				truth = new Boolean(lhs.value.equals(rhs));
				// RuleBase.appendText("\nTesting Clause " + lhs.name + " = " +
				// rhs + " " + truth);
				break;
			case 2:
				truth = new Boolean(lhs.value.compareTo(rhs) > 0);
				// RuleBase.appendText("\nTesting Clause " + lhs.name + " > " +
				// rhs + " " + truth);
				break;
			case 3:
				truth = new Boolean(lhs.value.compareTo(rhs) < 0);
				// RuleBase.appendText("\nTesting Clause " + lhs.name + " < " +
				// rhs + " " + truth);
				break;
			case 4:
				truth = new Boolean(lhs.value.compareTo(rhs) != 0);
				// RuleBase.appendText("\nTesting Clause " + lhs.name + " != " +
				// rhs + " " + truth);
				break;
			}

			return truth;
		}
	}

	void isConsequent() {
		consequent = new Boolean(true);
	}

	Rule getRule() {
		if (consequent.booleanValue() == true)
			return ruleRefs.firstElement();
		else
			return null;
	}

};
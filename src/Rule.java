import java.awt.TextArea;
import java.util.Enumeration;
import java.util.Vector;

public class Rule {
	RuleBase rb;
	String name;
	Clause antecedents[]; // allow up to 4 antecedents for now
	Clause consequent; // only 1 consequent clause allowed
	Boolean truth; // states = (null=unknown, true, or false)
	boolean fired = false;

	Rule(RuleBase Rb, String Name, Clause lhs, Clause rhs) {
		rb = Rb;
		name = Name;
		antecedents = new Clause[1];
		antecedents[0] = lhs;
		lhs.addRuleRef(this);
		consequent = rhs;
		rhs.addRuleRef(this);
		rhs.isConsequent();
		rb.ruleList.addElement(this); // add self to rule list
		truth = null;
	}

	Rule(RuleBase Rb, String Name, Clause lhs1, Clause lhs2, Clause rhs) {
		rb = Rb;
		name = Name;
		antecedents = new Clause[2];
		antecedents[0] = lhs1;
		lhs1.addRuleRef(this);
		antecedents[1] = lhs2;
		lhs2.addRuleRef(this);
		consequent = rhs;
		rhs.addRuleRef(this);
		rhs.isConsequent();
		rb.ruleList.addElement(this); // add self to rule list
		truth = null;
	}

	Rule(RuleBase Rb, String Name, Clause lhs1, Clause lhs2, Clause lhs3,
			Clause rhs) {
		rb = Rb;
		name = Name;
		antecedents = new Clause[3];
		antecedents[0] = lhs1;
		lhs1.addRuleRef(this);
		antecedents[1] = lhs2;
		lhs2.addRuleRef(this);
		antecedents[2] = lhs3;
		lhs3.addRuleRef(this);
		consequent = rhs;
		rhs.addRuleRef(this);
		rhs.isConsequent();
		rb.ruleList.addElement(this); // add self to rule list
		truth = null;
	}

	Rule(RuleBase Rb, String Name, Clause lhs1, Clause lhs2, Clause lhs3,
			Clause lhs4, Clause rhs) {
		rb = Rb;
		name = Name;
		antecedents = new Clause[4];
		antecedents[0] = lhs1;
		lhs1.addRuleRef(this);
		antecedents[1] = lhs2;
		lhs2.addRuleRef(this);
		antecedents[2] = lhs3;
		lhs3.addRuleRef(this);
		antecedents[3] = lhs4;
		lhs4.addRuleRef(this);
		consequent = rhs;
		rhs.addRuleRef(this);
		rhs.isConsequent();
		rb.ruleList.addElement(this); // add self to rule list
		truth = null;
	}

	long numAntecedents() {
		return antecedents.length;
	}

	Boolean check() { // if antecedent is true and rule has not fired
		RuleBase.appendText("\nTesting rule " + name);
		for (int i = 0; i < antecedents.length; i++) {
			if (antecedents[i].truth == null)
				return null;
			if (antecedents[i].truth.booleanValue() == true) {
				continue;
			} else {
				return truth = new Boolean(false); // don’t fire this rule
			}
		} // endfor
		return truth = new Boolean(true); // could fire this rule
	}

	// display the rule in text format
	@SuppressWarnings("deprecation")
	void display(TextArea textArea) {
		textArea.appendText(name + ": IF ");
		for (int i = 0; i < antecedents.length; i++) {
			Clause nextClause = antecedents[i];
			textArea.appendText(nextClause.lhs.name
					+ nextClause.cond.asString() + nextClause.rhs + " ");
			if ((i + 1) < antecedents.length)
				textArea.appendText("\n     AND ");
		}
		textArea.appendText("\n     THEN");
		textArea.appendText(consequent.lhs.name + consequent.cond.asString()
				+ consequent.rhs + "\n");
	}

	// determine if a rule is true or false
	// by recursively trying to prove its antecedent clauses are true
	// if any are false, the rule is false
	Boolean backChain() {
		RuleBase.appendText("\nEvaluating rule " + name);
		for (int i = 0; i < antecedents.length; i++) { // test each clause
			if (antecedents[i].truth == null)
				rb.backwardChain(antecedents[i].lhs.name);
			if (antecedents[i].truth == null) { // couldn’t prove t or f
				antecedents[i].lhs.askUser(); // so ask user for help
				truth = antecedents[i].check(); // redundant?
			}
			if (antecedents[i].truth.booleanValue() == true) {
				continue; // test the next antecedent (if any)
			} else {
				return truth = new Boolean(false); // exit, one is false
			}
		}
		return truth = new Boolean(true); // all antecedents are true
	}

	void fire() {
		RuleBase.appendText("\nFiring rule " + name);
		truth = new Boolean(true);
		fired = true;
		// set the variable value and update clauses
		consequent.lhs.setValue(consequent.rhs);
		// now retest any rules whose clauses just changed
		checkRules(consequent.lhs.clauseRefs);
	}

	public Rule selectRule(Vector ruleSet) {
		Enumeration enumi = ruleSet.elements();
		long numClauses;
		Rule nextRule;

		Rule bestRule = (Rule) enumi.nextElement();
		long max = bestRule.numAntecedents();
		while (enumi.hasMoreElements()) {
			nextRule = (Rule) enumi.nextElement();
			if ((numClauses = nextRule.numAntecedents()) > max) {
				max = numClauses;
				bestRule = nextRule;
			}
		}
		return bestRule;
	}

	public static void checkRules(Vector clauseRefs) {
		Enumeration enumi = clauseRefs.elements();
		while (enumi.hasMoreElements()) {
			Clause temp = (Clause) enumi.nextElement();
			Enumeration enum2 = temp.ruleRefs.elements();
			while (enum2.hasMoreElements()) {
				((Rule) enum2.nextElement()).check(); // retest the rule
			}
		}
	}
}
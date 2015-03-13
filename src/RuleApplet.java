/*  RuleApplet class

 Constructing Intelligent Agents with Java
 (C) Joseph P.Bigus and Jennifer Bigus 1997

 Modified: 8/14/98 jpb convert to 1.1 Event model

 Copyright (c) 1998 John Wiley & Sons, Inc.  All rights reserved.  
 Reproduction or translation of this work beyond that permitted in 
 Section 117 of the 1976 United States Copyright Act without the 
 express written permission of the copyright owner is unlawful.  
 Requests for further information should be addressed to Permissions 
 Department, John Wiley & Sons, Inc. The purchaser may make back-up 
 copies for his/her own use only and not for distribution or resale.
 The Publisher assumes no responsibility for errors, omissions, or 
 damages, caused by the use of these programs or from the use of the 
 information contained herein.

 */

import java.applet.Applet;
import java.awt.CheckboxGroup;
import java.awt.Frame;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

public class RuleApplet extends Applet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    // user selected a rule base
	void choice1_Clicked() {
		String rbName = choice1.getSelectedItem();
		if (rbName.equals("Acheteur"))
			currentRuleBase = Acheteur;
		if (rbName.equals("Vendeur"))
			currentRuleBase = Vendeur;

		currentRuleBase.reset(); // reset the rule base
		choice2.removeAll(); // jpb -- bug fix
		choice3.removeAll();
		Enumeration<RuleVariable> vars = currentRuleBase.variableList
				.elements();
		while (vars.hasMoreElements()) {
			choice2.addItem(((RuleVariable) vars.nextElement()).name);
		}
		currentRuleBase.displayVariables(textArea3);

	}

	// user selected a variable
	void choice2_Clicked(java.awt.event.ItemEvent event) {
		String varName = choice2.getSelectedItem();
		choice3.removeAll();

		RuleVariable rvar = (RuleVariable) currentRuleBase.variableList
				.get(varName);
		Enumeration<String> labels = rvar.labels.elements();
		while (labels.hasMoreElements()) {
			choice3.addItem(((String) labels.nextElement()));
		}
	}

	// user selected a value for a variable
	void choice3_Clicked(java.awt.event.ItemEvent event) {
		String varName = choice2.getSelectedItem();
		String varValue = choice3.getSelectedItem();

		RuleVariable rvar = (RuleVariable) currentRuleBase.variableList
				.get(varName);
		rvar.setValue(varValue);
		textArea3.append("\n" + rvar.name + " set to " + varValue);

	}

	// user pressed Find button -- do an infernece cycle
	void button1_Clicked(java.awt.event.ActionEvent event) {
		String goal = textField1.getText();

		textArea2.append("\n --- Starting Inferencing Cycle --- \n");
		currentRuleBase.displayVariables(textArea2);
		if (radioButton1.getState() == true)
			currentRuleBase.forwardChain();
		if (radioButton2.getState() == true)
			currentRuleBase.backwardChain(goal);
		currentRuleBase.displayVariables(textArea2);
		textArea2.append("\n --- Ending Inferencing Cycle --- \n");
	}

	// user pressed Demo button -- do inference with pre-set values
	void button2_Clicked(java.awt.event.ActionEvent event) {
		String rbName = choice1.getSelectedItem();
		if (rbName.equals("Acheteur")) {
			if (radioButton1.getState() == true)
				demoVehiclesFC(Acheteur);
			if (radioButton2.getState() == true)
				demoVehiclesBC(Acheteur);
		} else  {
			if (radioButton1.getState() == true)
				demoBugsFC(Vendeur);
			if (radioButton2.getState() == true)
				demoBugsBC(Vendeur);
		}

	}

	void button3_Clicked(java.awt.event.ActionEvent event) {

		// {{CONNECTION
		// Clear the text for TextArea
		textArea1.setText("");
		textArea2.setText("");
		textArea3.setText("");
		// }}

		currentRuleBase.reset();
		currentRuleBase.displayRules(textArea1);
		currentRuleBase.displayVariables(textArea3);
	}

	// display dialog to get user value for a variable
	@SuppressWarnings("deprecation")
	static public String waitForAnswer(String prompt, String labels) {

		// position dialog over parent dialog

		Frame frame = new Frame("Ask User");
		dlg = new RuleVarDialog(frame, true);
		dlg.label1.setText("   " + prompt + " (" + labels + ")");
		dlg.setLocation(200, 200);
		dlg.show();
		String ans = dlg.getText();
		return ans;

	}

	public void init() {
		super.init();

		// {{INIT_CONTROLS
		setLayout(null);
		setSize(624, 527);
		button1.setLabel("Find Goal");
		addNotify();
		resize(624, 527);
		button1 = new java.awt.Button("Find Goal");
		button1.setBounds(36, 468, 108, 30);
		button2.setLabel("Run Demo");
		add(button1);
		button2 = new java.awt.Button("Run Demo");
		button2.setBounds(228, 468, 108, 30);
		button3.setLabel("Reset");
		add(button2);
		button3 = new java.awt.Button("Reset");
		button3.setBounds(444, 468, 108, 30);
		add(button3);
		textArea1 = new java.awt.TextArea();
		textArea1.setBounds(12, 48, 312, 144);
		add(textArea1);
		textArea2 = new java.awt.TextArea();
		textArea2.setBounds(12, 216, 600, 168);
		label2.setText("Trace Log");
		add(textArea2);
		label2 = new java.awt.Label("Trace Log");
		label2.setBounds(24, 192, 168, 24);
		label1.setText("Rule Base");
		add(label2);
		label1 = new java.awt.Label("Rule Base");
		label1.setBounds(24, 12, 96, 24);
		add(label1);
		choice1 = new java.awt.Choice();
		add(choice1);
		choice1.setBounds(132, 12, 192, 21);
		radioButton1.setCheckboxGroup(Group1);
		radioButton1.setLabel("Forward Chain");
		Group1 = new CheckboxGroup();
		radioButton1 = new java.awt.Checkbox("Forward Chain", Group1, false);
		radioButton1.setBounds(36, 396, 156, 21);
		add(radioButton1);
		choice3 = new java.awt.Choice();
		add(choice3);
		choice3.setBounds(480, 36, 135, 21);
		label5.setText("Value");
		label5 = new java.awt.Label("Value");
		label5.setBounds(480, 12, 95, 24);
		add(label5);
		choice2 = new java.awt.Choice();
		add(choice2);
		choice2.setBounds(336, 36, 137, 21);
		textArea3 = new java.awt.TextArea();
		textArea3.setBounds(336, 72, 276, 122);
		label4.setText("Variable");
		add(textArea3);
		label4 = new java.awt.Label("Variable");
		label4.setBounds(336, 12, 109, 24);
		radioButton2.setCheckboxGroup(Group1);
		radioButton2.setLabel("Backward Chain");
		add(label4);
		radioButton2 = new java.awt.Checkbox("Backward Chain", Group1, false);
		radioButton2.setBounds(36, 420, 156, 24);
		add(radioButton2);
		textField1 = new java.awt.TextField();
		textField1.setBounds(324, 420, 142, 27);
		label3.setText("Goal");
		add(textField1);
		label3 = new java.awt.Label("Goal");
		label3.setBounds(324, 384, 80, 30);
		add(label3);
		// }}

		frame = new Frame("Ask User");
		frame.setSize(50, 50);
		frame.setLocation(100, 100);
		choice1.addItem("Acheteur");
		choice1.addItem("Vendeur");

        Acheteur = new RuleBase("Acheteur Rule Base");
        Acheteur.setDisplay(textArea2);
		initAcheteurRuleBase(Acheteur);
		currentRuleBase = Acheteur;

        Vendeur = new RuleBase("Vendeur Rule Base");
        Vendeur.setDisplay(textArea2);
		initVendeurRuleBase(Vendeur);

		// initialize textAreas and list controls
		currentRuleBase.displayRules(textArea1);
		currentRuleBase.displayVariables(textArea3);
		radioButton1.setState(true);
		choice1_Clicked(); // fill variable list

		// {{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		button1.addActionListener(lSymAction);
		button2.addActionListener(lSymAction);
		button3.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		choice1.addItemListener(lSymItem);
		choice2.addItemListener(lSymItem);
		choice3.addItemListener(lSymItem);
		// }}
	}

	// {{DECLARE_CONTROLS
	java.awt.Button button1 = new java.awt.Button();
	java.awt.Button button2 = new java.awt.Button();
	java.awt.Button button3 = new java.awt.Button();
	java.awt.TextArea textArea1 = new java.awt.TextArea();
	java.awt.TextArea textArea2 = new java.awt.TextArea();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Choice choice1 = new java.awt.Choice();
	java.awt.Checkbox radioButton1 = new java.awt.Checkbox();
	java.awt.CheckboxGroup Group1 = new java.awt.CheckboxGroup();
	java.awt.Choice choice3 = new java.awt.Choice();
	java.awt.Label label5 = new java.awt.Label();
	java.awt.Choice choice2 = new java.awt.Choice();
	java.awt.TextArea textArea3 = new java.awt.TextArea();
	java.awt.Label label4 = new java.awt.Label();
	java.awt.Checkbox radioButton2 = new java.awt.Checkbox();
	java.awt.TextField textField1 = new java.awt.TextField();
	java.awt.Label label3 = new java.awt.Label();
	// }}

	static Frame frame;
	static RuleVarDialog dlg;
	static RuleBase Vendeur;
	static RuleBase Acheteur;
	static RuleBase currentRuleBase;

	// initialize the Vendeur rule base
	public void initVendeurRuleBase(RuleBase rb) {
		rb.goalClauseStack = new Stack(); // goals and subgoals

		rb.variableList = new Hashtable();
		RuleVariable CategorieVoiture = new RuleVariable("CatégorieVoiture");
        CategorieVoiture.setLabels("Essence Diesel");
        CategorieVoiture.setPromptText("Quelle est la catégorie de la voiture?");
		rb.variableList.put(CategorieVoiture.name, CategorieVoiture);

		RuleVariable MarqueVoiture = new RuleVariable("MarqueVoiture");
        MarqueVoiture.setLabels("Renault Mercedes BMW Peugeot");
        MarqueVoiture.setPromptText("Quelle est la marque de la voiture?");
		rb.variableList.put(MarqueVoiture.name, MarqueVoiture);

		RuleVariable Cylindree = new RuleVariable("Cylindrée");
        Cylindree.setLabels("1.0L 1.2L 1.4L 1.6L 1.8L 2.0L");
        Cylindree.setPromptText("Quelle est la cylindrée de la voiture?");
		rb.variableList.put(Cylindree.name, Cylindree);

		RuleVariable couleur = new RuleVariable("couleur");
        couleur.setLabels("Noire Blanche Rouge Gris_métalisé");
        couleur.setPromptText("Quelle est la couleur de la voiture?");
		rb.variableList.put(couleur.name, couleur);

		RuleVariable nombrePlaces = new RuleVariable("nombrePlaces");
        nombrePlaces.setLabels("2 5");
        nombrePlaces.setPromptText("Quel est le nombre de places de la voiture?");
		rb.variableList.put(nombrePlaces.name, nombrePlaces);

		RuleVariable nombrePortes = new RuleVariable("nombrePortes");
        nombrePortes.setLabels("3 5");
        nombrePortes.setPromptText("Quel est le nombre de portes de la voiture?");
		rb.variableList.put(nombrePortes.name, nombrePortes);

		RuleVariable Accident = new RuleVariable("Accidentée");
        Accident.setLabels("oui non");
        Accident.setPromptText("La voiture est-elle accidentée?");
		rb.variableList.put(Accident.name, Accident);

        RuleVariable Prix = new RuleVariable("Prix");
        Prix.setLabels("<1000K >1000Ket<2000K >2000K");
        Prix.setPromptText("Quel est le prix de la voiture?");
        rb.variableList.put(Prix.name, Prix);

		// Note: at this point all variables values are NULL

		Condition cEquals = new Condition("=");
		Condition cNotEquals = new Condition("!=");

		// define rules
		rb.ruleList = new Vector();
		Rule Vend1 = new Rule(rb, "Achete", new Clause(CategorieVoiture, cEquals,
				"Essence"), new Clause(Cylindree, cEquals, "1.4L"),
				new Clause(MarqueVoiture, cEquals, "Renault"), new Clause(Prix, cEquals, ">1000Ket<2000K"));

		/*Rule Vend2 = new Rule(rb, "tick", new Clause(MarqueVoiture, cEquals,
				"arachnid"), new Clause(MarqueVoiture, cEquals, "short"),
				new Clause(MarqueVoiture, cEquals, "Tick"));*/

	}


	// initialize the Acheteur rule base
	public void initAcheteurRuleBase(RuleBase rb) {
		rb.goalClauseStack = new Stack(); // goals and subgoals

        rb.variableList = new Hashtable();
        RuleVariable CategorieVoiture = new RuleVariable("CatégorieVoiture");
        CategorieVoiture.setLabels("Essence Diesel");
        CategorieVoiture.setPromptText("Quelle est la catégorie de la voiture?");
        rb.variableList.put(CategorieVoiture.name, CategorieVoiture);

        RuleVariable MarqueVoiture = new RuleVariable("MarqueVoiture");
        MarqueVoiture.setLabels("Renault Mercedes BMW Peugeot");
        MarqueVoiture.setPromptText("Quelle est la marque de la voiture?");
        rb.variableList.put(MarqueVoiture.name, MarqueVoiture);

        RuleVariable Cylindree = new RuleVariable("Cylindrée");
        Cylindree.setLabels("1.0L 1.2L 1.4L 1.6L 1.8L 2.0L");
        Cylindree.setPromptText("Quelle est la cylindrée de la voiture?");
        rb.variableList.put(Cylindree.name, Cylindree);

        RuleVariable couleur = new RuleVariable("couleur");
        couleur.setLabels("Noire Blanche Rouge Gris_métalisé");
        couleur.setPromptText("Quelle est la couleur de la voiture?");
        rb.variableList.put(couleur.name, couleur);

        RuleVariable nombrePlaces = new RuleVariable("nombrePlaces");
        nombrePlaces.setLabels("2 5");
        nombrePlaces.setPromptText("Quel est le nombre de places de la voiture?");
        rb.variableList.put(nombrePlaces.name, nombrePlaces);

        RuleVariable nombrePortes = new RuleVariable("nombrePortes");
        nombrePortes.setLabels("3 5");
        nombrePortes.setPromptText("Quel est le nombre de portes de la voiture?");
        rb.variableList.put(nombrePortes.name, nombrePortes);

        RuleVariable Accident = new RuleVariable("Accidentée");
        Accident.setLabels("oui non");
        Accident.setPromptText("La voiture est-elle accidentée?");
        rb.variableList.put(Accident.name, Accident);

        RuleVariable Prix = new RuleVariable("Prix");
        Prix.setLabels("<1000K >1000Ket<2000K >2000K");
        Prix.setPromptText("Quel est le prix de la voiture?");
        rb.variableList.put(Prix.name, Prix);

        RuleVariable ach = new RuleVariable(" Acheter");
        ach.setLabels("oui non");
        ach.setPromptText("Voulez-vous acheter la voiture?");
        rb.variableList.put(ach.name, ach);

		// Note: at this point all variables values are NULL

		Condition cEquals = new Condition("=");
		Condition cNotEquals = new Condition("!=");
		Condition cLessThan = new Condition("<");

		// define rules
        rb.ruleList = new Vector();
        Rule Vend1 = new Rule(rb, "Achete", new Clause(CategorieVoiture, cEquals,
                "Essence"), new Clause(Cylindree, cEquals, "1.4L"),
                new Clause(MarqueVoiture, cEquals, "Renault"), new Clause(ach, cEquals, "oui"));
        Rule NonVend = new Rule(rb, "AchatRefuse", new Clause(CategorieVoiture, cEquals,
                "Diesel"), new Clause(Cylindree, cEquals, "2.0L"),
                new Clause(MarqueVoiture, cEquals, "BMW"), new Clause(ach, cEquals, "Non"));

    }

	public void demoVehiclesFC(RuleBase rb) {

		textArea2.append("\n --- Starting Demo ForwardChain ---\n ");
		// should be a Mini-Van
		((RuleVariable) rb.variableList.get("vehicle")).setValue(null);
		((RuleVariable) rb.variableList.get("vehicleType")).setValue(null);
		((RuleVariable) rb.variableList.get("size")).setValue("medium");
		((RuleVariable) rb.variableList.get("num_wheels")).setValue("4");
		((RuleVariable) rb.variableList.get("num_doors")).setValue("3");
		((RuleVariable) rb.variableList.get("motor")).setValue("yes");
		rb.displayVariables(textArea2);
		rb.forwardChain(); // chain until quiescence...
		textArea2.append("\n --- Stopping Demo ForwardChain! ---\n");

		rb.displayVariables(textArea2);
	}

	public void demoVehiclesBC(RuleBase rb) {
		textArea2.append("\n --- Starting Demo BackwardChain ---\n ");
		// should be a minivan
		((RuleVariable) rb.variableList.get("vehicle")).setValue(null);
		((RuleVariable) rb.variableList.get("vehicleType")).setValue(null);
		((RuleVariable) rb.variableList.get("size")).setValue("medium");
		((RuleVariable) rb.variableList.get("num_wheels")).setValue("4");
		((RuleVariable) rb.variableList.get("num_doors")).setValue("3");
		((RuleVariable) rb.variableList.get("motor")).setValue("yes");
		rb.displayVariables(textArea2);
		rb.backwardChain("vehicle"); // chain until quiescence...
		textArea2.append("\n --- Stopping Demo BackwardChain! ---\n ");
		rb.displayVariables(textArea2);
	}

	public void demoBugsBC(RuleBase rb) {
		textArea2.append("\n --- Starting Demo BackwardChain ---\n ");
		// should be a insect, ladybug
		((RuleVariable) rb.variableList.get("wings")).setValue("2");
		((RuleVariable) rb.variableList.get("legs")).setValue("6");
		((RuleVariable) rb.variableList.get("shape")).setValue("round");
		((RuleVariable) rb.variableList.get("antennae")).setValue("2");
		((RuleVariable) rb.variableList.get("color"))
				.setValue("orange_and_black");
		((RuleVariable) rb.variableList.get("leg_length")).setValue("long");
		((RuleVariable) rb.variableList.get("size")).setValue("small");
		rb.displayVariables(textArea2);
		rb.backwardChain("species"); // chain until quiescence...
		textArea2.append("\n --- Stopping Demo BackwardChain! ---\n");
		rb.displayVariables(textArea2);
	}

	public void demoBugsFC(RuleBase rb) {

		textArea2.append("\n --- Starting Demo ForwardChain ---\n");
		// should be a insect, ladybug
		((RuleVariable) rb.variableList.get("bugClass")).setValue(null);
		((RuleVariable) rb.variableList.get("insectType")).setValue(null);
		((RuleVariable) rb.variableList.get("wings")).setValue("2");
		((RuleVariable) rb.variableList.get("legs")).setValue("6");
		((RuleVariable) rb.variableList.get("shape")).setValue("round");
		((RuleVariable) rb.variableList.get("antennae")).setValue("2");
		((RuleVariable) rb.variableList.get("color"))
				.setValue("orange_and_black");
		((RuleVariable) rb.variableList.get("leg_length")).setValue("long");
		((RuleVariable) rb.variableList.get("size")).setValue("small");
		rb.displayVariables(textArea2);
		rb.forwardChain(); // chain until quiescence...
		textArea2.append("\n --- Stopping Demo ForwardChain! ---\n");

		// now display the results
		rb.displayVariables(textArea2);
	}

	public void demoPlantsBC(RuleBase rb) {
		textArea2.append("\n --- Starting Demo BackwardChain ---\n ");
		// should be a pine tree
		((RuleVariable) rb.variableList.get("stem")).setValue("woody");
		((RuleVariable) rb.variableList.get("stemPosition"))
				.setValue("upright");
		((RuleVariable) rb.variableList.get("one_main_trunk")).setValue("yes");
		((RuleVariable) rb.variableList.get("broad_and_flat_leaves"))
				.setValue("no");
		((RuleVariable) rb.variableList.get("leaf_shape"))
				.setValue("needlelike");
		((RuleVariable) rb.variableList.get("needle_pattern"))
				.setValue("random");
		rb.displayVariables(textArea2);

		rb.backwardChain("family"); // chain until quiescence...
		textArea2.append("\n --- Stopping Demo BackwardChain! ---\n");
		rb.displayVariables(textArea2);
	}

	public void demoPlantsFC(RuleBase rb) {

		textArea2.append("\n --- Starting Demo ForwardChain --- \n ");
		// should be a pine tree
		((RuleVariable) rb.variableList.get("stem")).setValue("woody");
		((RuleVariable) rb.variableList.get("stemPosition"))
				.setValue("upright");
		((RuleVariable) rb.variableList.get("one_main_trunk")).setValue("yes");
		((RuleVariable) rb.variableList.get("broad_and_flat_leaves"))
				.setValue("no");
		((RuleVariable) rb.variableList.get("leaf_shape"))
				.setValue("needlelike");
		((RuleVariable) rb.variableList.get("needle_pattern"))
				.setValue("random");
		rb.displayVariables(textArea2);

		rb.forwardChain(); // chain until quiescence...
		textArea2.append("\n --- Stopping Demo ForwardChain! --- \n");

		rb.displayVariables(textArea2);
	}

	// -------------------------------------------------------------
	// This file has been migrated from the 1.0 to 1.1 event model.
	// This method is not used with the new 1.1 event model. You can
	// move any code you need to keep, then remove this method.
	// -------------------------------------------------------------
	//
	//
	//
	// public boolean handleEvent(Event event) {
	// if (event.target == button1 && event.id == Event.ACTION_EVENT) {
	// button1_Clicked(event);
	// return true;
	// }if (event.target == button2 && event.id == Event.ACTION_EVENT) {
	// button2_Clicked(event);
	// return true;
	// }if (event.target == button3 && event.id == Event.ACTION_EVENT) {
	// button3_Clicked(event);
	// return true;
	// }
	// if (event.target == dlg && event.id == Event.ACTION_EVENT) {
	// return dlg.handleEvent(event);
	// }
	// if (event.target == choice1 && event.id == Event.ACTION_EVENT) {
	// choice1_Clicked();
	// return true;
	// }
	// if (event.target == choice2 && event.id == Event.ACTION_EVENT) {
	// choice2_Clicked(event);
	// return true;
	// }
	// if (event.target == choice3 && event.id == Event.ACTION_EVENT) {
	// choice3_Clicked(event);
	// return true;
	// }
	// return super.handleEvent(event);
	// }
	// -------------------------------------------------------------

	class SymAction implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			Object object = event.getSource();
			if (object == button1)
				button1_Clicked(event);
			else if (object == button2)
				button2_Clicked(event);
			else if (object == button3)
				button3_Clicked(event);
		}
	}

	class SymItem implements java.awt.event.ItemListener {
		public void itemStateChanged(java.awt.event.ItemEvent event) {
			Object object = event.getSource();
			if (object == choice1)
				choice1_Clicked();
			else if (object == choice2)
				choice2_Clicked(event);
			else if (object == choice3)
				choice3_Clicked(event);
		}
	}
}

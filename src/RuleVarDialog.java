/*  RuleVarDialog class

 Constructing Intelligent Agents with Java
   (C) Joseph P.Bigus and Jennifer Bigus 1997

*/

import java.awt.*;

public class RuleVarDialog extends Dialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	void button1_Clicked(java.awt.event.ActionEvent event) {
         answer = textField1.getText().trim() ;   // set
         dispose();
    }


	public RuleVarDialog(Frame parent, boolean modal) {

	    super(parent, modal);

     	//{{INIT_CONTROLS
		panel1.setLayout(null);
    panel1.setSize(360, 220) ;
    add(panel1);
		setSize(getInsets().left + getInsets().right + 352,getInsets().top + getInsets().bottom + 214);
		label1 = new java.awt.Label("");
		label1.setBounds(getInsets().left + 0,getInsets().top + 12,407,61);
		panel1.add(label1);
		textField1 = new java.awt.TextField();
		textField1.setText("");
		textField1.setBounds(getInsets().left + 192,getInsets().top + 84,97,39);
		panel1.add(textField1);
		button1 = new java.awt.Button("Set");
		button1.setBounds(getInsets().left + 24,getInsets().top + 144,124,41);
		panel1.add(button1);
    setTitle("Rule Applet -- Ask User");
		//}}

    	//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		button1.addActionListener(lSymAction);
		this.addWindowListener(new windowEvents());
		//}}
	}

	public RuleVarDialog(Frame parent, String title, boolean modal) {
	    this(parent, modal);
	    setTitle(title);
	}


 //-------------------------------------------------------------
 // This file has been migrated from the 1.0 to 1.1 event model.
 // This method is not used with the new 1.1 event model. You can 
 // move any code you need to keep, then remove this method.
 //-------------------------------------------------------------
 // 
 // 	public boolean handleEvent(Event event) {
 // 
 //         // bug workaround --- 1004 is button press ???
 // 	    if ( event.id == 1004 & event.target == button1 ) {
 // 	       button1_Clicked(event);  // set
 // 	       hide() ;
 //            dispose() ;
 // 	       return true ;
 //  	 	}
 //     	 return super.handleEvent(event);
 // 	}
 //-------------------------------------------------------------

 	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == button1)
				button1_Clicked(event);

	  }
  }

	class windowEvents extends java.awt.event.WindowAdapter {
        public void windowClosing(java.awt.event.WindowEvent event) {
               answer = "" ;
               dispose();
        }
    }



    public String getText() { return answer; }

	//{{DECLARE_CONTROLS
  java.awt.Panel panel1 = new Panel();
	java.awt.Label label1 = new Label();
	java.awt.TextField textField1;
	java.awt.Button button1;
	//}}
	String answer = new String("") ;
}

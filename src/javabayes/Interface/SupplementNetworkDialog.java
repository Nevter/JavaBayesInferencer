package javabayes.Interface;

import javabayes.InferenceGraphs.*;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;


class SupplementNetworkDialog extends Dialog {
    // The InferenceGraph object that contains the network.
    InferenceGraph ig;

    // Components used in the dialog
    Panel returnp, propp;
    Button apply_button, dismiss_button;
    Choice antecedent, operator, consequent;
    Vector nodeNames;

    // Constants used to construct the dialog.
    private final static int TOP_INSET = 13;
    private final static int LEFT_INSET = 18;
    private final static int RIGHT_INSET = 18;
    private final static int BOTTOM_INSET = 0;

    // Labels for the various elements of the dialog.
    private final static String dialog_title = "Supplement Network";
    private final static String apply_label = "Apply";
    private final static String dismiss_label = "Dismiss";
    private final static String classical_implication_label = "-> : classical";
    private final static String defeasible_implication_label = "~> : defeasible";


    /**
     * Default constructor for an SupplementNetworkDialog object.
     */
    public SupplementNetworkDialog(Frame parent, InferenceGraph i_g){
        super(parent, dialog_title, true);
        this.ig = i_g;

        nodeNames = get_ig_variable_names();

        //Propositional Statements panel
        propp = new Panel();
        propp.setLayout(new BorderLayout());

        antecedent = new Choice();
        // Add all variables as options for antecedent
        for (Enumeration names = nodeNames.elements(); names.hasMoreElements(); )
            antecedent.add((String)names.nextElement());
        operator = new Choice();
        operator.add(classical_implication_label);
        operator.add(defeasible_implication_label);

        consequent = new Choice();
        // Add all variables as options for consequent
        for (Enumeration names = nodeNames.elements(); names.hasMoreElements(); )
            consequent.add((String)names.nextElement());
        propp.add("West", antecedent);
        propp.add("Center", operator);
        propp.add("East", consequent);

        // Return buttons panel
        returnp = new Panel();
        returnp.setLayout(new FlowLayout(FlowLayout.CENTER));
        apply_button = new Button(apply_label);
        dismiss_button = new Button(dismiss_label);
        returnp.add(apply_button);
        returnp.add(dismiss_button);

        setLayout(new BorderLayout());
        add("Center", propp);
        add("South", returnp);

        // Pack the whole window
        pack();

    }

    /**
     * Customized show method.
     */
    public void show() {
        Rectangle bounds = getParent().bounds();
        Rectangle abounds = bounds();

        move(bounds.x + (bounds.width - abounds.width)/ 2,
                bounds.y + (bounds.height - abounds.height)/2);

        super.show();
    }


    /**
     * Customized insets method.
     */
    public Insets insets() {
        Insets ins = super.insets();
        return(new Insets(ins.top + TOP_INSET, ins.left + LEFT_INSET,
                ins.bottom + BOTTOM_INSET, ins.right + RIGHT_INSET));
    }

    /**
     * Handle the possible destruction of the window.
     */
    public boolean handleEvent(Event evt) {
        if (evt.id == Event.WINDOW_DESTROY)
            dispose();
        return(super.handleEvent(evt));
    }

    /**
     * Handle the possible events.
     */
    public boolean action(Event evt, Object arg) {

        if (evt.target == dismiss_button){
            dispose();
        }
        else if (evt.target == apply_button){
            update_network();
        }
        else return super.action(evt, arg);

        return true;
    }

    /**
     * Update the contents of the network when the dialog exits
     */
    private void update_network(){
        String antecedentName = antecedent.getSelectedItem();
        String consequentName = consequent.getSelectedItem();
        String operatorName = operator.getSelectedItem();

        InferenceGraphNode antecedentNode = null;
        InferenceGraphNode consequentNode = null;

        // Find the correct InferenceGraphNode objects

        for (Enumeration node = ig.get_nodes().elements(); node.hasMoreElements(); ) {
            InferenceGraphNode currentNode = (InferenceGraphNode)node.nextElement();
            if (currentNode.get_name().equals(antecedentName))
                antecedentNode = currentNode;
            if (currentNode.get_name().equals(consequentName))
                consequentNode = currentNode;
        }

        if (antecedentNode == null || consequentNode == null ) {
            //error
            System.out.println("Node could not be found");
            return;
        }

        /*
        int relationship = ig.get_relationship(antecedentNode, consequentNode);

        if (relationship == InferenceGraph.RELATION_SELF){
            //print error to console
            System.out.println("The two nodes are the same!");
            return;
        }
        */

        /**
         * TODO:
         * check if  there is already a relationship? - Algorithm dependent
         * draw an arc between the two nodes depending on the operator.
         */
        System.out.println("creating arc");
        ig.create_arc(antecedentNode, consequentNode);

    }

    private Vector get_ig_variable_names(){
        Enumeration nodes = ig.get_nodes().elements();
        Vector nodeNames = new Vector();
        while (nodes.hasMoreElements())
            nodeNames.add(((InferenceGraphNode)nodes.nextElement()).get_name());
        return nodeNames;
    }
}

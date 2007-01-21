package pt.linkare.ant.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTextField;

public class PropertyEditorForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JScrollPane jScrollPane = null;

    private JTextPane validationLog = null;

    private JButton saveBtn = null;

    private JPanel jPanel = null;

    private JLabel propertiesDescLabel = null;

    private JPanel panelProperties = null;

    private JLabel propName = null;

    private JTextField propEditor = null;

    private JLabel propOutput = null;

    /**
         * This is the default constructor
         */
    public PropertyEditorForm() {
	super();
	initialize();
    }

    /**
         * This method initializes this
         * 
         * @return void
         */
    private void initialize() {
	this.setSize(300, 200);
	this.setContentPane(getJContentPane());
	this.setTitle("JFrame");
    }

    /**
         * This method initializes jContentPane
         * 
         * @return javax.swing.JPanel
         */
    private JPanel getJContentPane() {
	if (jContentPane == null) {
	    propertiesDescLabel = new JLabel();
	    propertiesDescLabel.setText("properties description");
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new BorderLayout());
	    jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
	    jContentPane.add(getJPanel(), BorderLayout.SOUTH);
	    jContentPane.add(propertiesDescLabel, BorderLayout.NORTH);
	}
	return jContentPane;
    }

    /**
         * This method initializes jScrollPane
         * 
         * @return javax.swing.JScrollPane
         */
    private JScrollPane getJScrollPane() {
	if (jScrollPane == null) {
	    jScrollPane = new JScrollPane();
	    jScrollPane.setViewportView(getPanelProperties());
	}
	return jScrollPane;
    }

    /**
         * This method initializes validationLog
         * 
         * @return javax.swing.JTextPane
         */
    private JTextPane getValidationLog() {
	if (validationLog == null) {
	    validationLog = new JTextPane();
	}
	return validationLog;
    }

    /**
         * This method initializes saveBtn
         * 
         * @return javax.swing.JButton
         */
    private JButton getSaveBtn() {
	if (saveBtn == null) {
	    saveBtn = new JButton();
	    saveBtn.setText("Save & Exit");
	}
	return saveBtn;
    }

    /**
         * This method initializes jPanel
         * 
         * @return javax.swing.JPanel
         */
    private JPanel getJPanel() {
	if (jPanel == null) {
	    GridBagConstraints gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.fill = GridBagConstraints.BOTH;
	    gridBagConstraints.weighty = 1.0;
	    gridBagConstraints.weightx = 1.0;
	    jPanel = new JPanel();
	    jPanel.setLayout(new GridBagLayout());
	    jPanel.add(getValidationLog(), gridBagConstraints);
	    jPanel.add(getSaveBtn(), new GridBagConstraints());
	}
	return jPanel;
    }

    /**
         * This method initializes panelProperties
         * 
         * @return javax.swing.JPanel
         */
    private JPanel getPanelProperties() {
	if (panelProperties == null) {
	    GridLayout gridLayout = new GridLayout();
	    gridLayout.setRows(1);
	    gridLayout.setHgap(2);
	    gridLayout.setVgap(1);
	    gridLayout.setColumns(3);
	    
	    propName = new JLabel();
	    propName.setText("JLabel");
	    propOutput = new JLabel();
	    propOutput.setText("JLabel");
	    panelProperties = new JPanel();
	    panelProperties.setLayout(gridLayout);
	    panelProperties.add(propName, null);
	    panelProperties.add(getPropEditor(), null);
	    panelProperties.add(propOutput, null);
	}
	return panelProperties;
    }

    /**
         * This method initializes propEditor
         * 
         * @return javax.swing.JTextField
         */
    private JTextField getPropEditor() {
	if (propEditor == null) {
	    propEditor = new JTextField();
	}
	return propEditor;
    }

}

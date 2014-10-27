package org.ripple.power.ui;

import javax.swing.JRadioButton;

import org.ripple.power.config.LSystem;
import org.ripple.power.ui.graphics.LColor;

public class RPRadioButton extends JRadioButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RPRadioButton(){
		setBackground(LSystem.dialogbackground);
		setForeground(LColor.WHITE);
	}
}

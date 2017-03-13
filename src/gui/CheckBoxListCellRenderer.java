/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Melody
 */
public class CheckBoxListCellRenderer implements ListCellRenderer<JCheckBox> {

    @Override
    public Component getListCellRendererComponent(JList<? extends JCheckBox> list, JCheckBox value, 
                                                      int i, boolean isSelected, boolean bln1) {
        value.setSelected(isSelected);
        value.setEnabled(true);
        return value;
    }

}

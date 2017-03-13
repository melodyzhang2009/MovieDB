/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;

/**
 *
 * @author Melody
 */
public class CountryCheckBoxListener implements ItemListener {

    private final Set<String> genresSelected;

    public CountryCheckBoxListener(Set<String> genresSelected) {
        this.genresSelected = genresSelected;
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        String genre = ((JCheckBox) ie.getItem()).getText();
        int selectedOrNot = ie.getStateChange();
        if (selectedOrNot == ItemEvent.SELECTED) {
            genresSelected.add(genre);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Genre {0} is selected", genre);
        } else if (selectedOrNot == ItemEvent.DESELECTED) {
            genresSelected.remove(genre);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Genre {0} is deselected", genre);
        }
    }
}


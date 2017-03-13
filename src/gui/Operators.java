/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Melody
 */
public enum Operators {
    ALL("=, <, >, <=, >="), EQUAL("="), LESS("<"), BIGGER(">"), LESSANDEQUAL("<="), BIGGERANDEQUAL(">=");
    
    String value;
    
    Operators(String string) {
        value = string;
    }
    
    @Override
    public String toString() {
        return value;
    }
}

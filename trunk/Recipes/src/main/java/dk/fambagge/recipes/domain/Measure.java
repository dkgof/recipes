/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.fambagge.recipes.domain;

import java.io.Serializable;

/**
 *
 * @author Gof
 */
public interface Measure extends Serializable {
    public String getSymbol();
    public double getFactor();
    
    public enum Weight implements Measure {
        MILLIGRAM("mg", 0.001),
        GRAM("g", 1),
        OUNCE("oz", 28.35),
        POUND("lb", 453.59),
        KILOGRAM("kg", 1000),
        ;
        
        private final String symbol;
        private final double factor;
        
        Weight(String symbol, double factor) {
            this.symbol = symbol;
            this.factor = factor;
        }
        
        @Override
        public String getSymbol() {
            return symbol;
        }
        
        @Override
        public double getFactor() {
            return factor;
        }
    }
    
    public enum Volume implements Measure {
        DROP("drop", 0.001 / 20.0),
        MILLILITER("ml", 0.001),
        TEASPOON("tsp", 0.004),
        TABLESPOON("tbsp", 0.015),
        CENTILITER("cl", 0.01),
        FLUID_OUNCE("fl oz", 0.029),
        DECILITER("dl", 0.1),
        CUP_US("cup us", 0.23659),
        CUP("cup", 0.250),
        LITER("l", 1),
        ;
        
        private final String symbol;
        private final double factor;
        
        Volume(String symbol, double factor) {
            this.symbol = symbol;
            this.factor = factor;
        }
        
        @Override
        public String getSymbol() {
            return symbol;
        }
        
        @Override
        public double getFactor() {
            return factor;
        }
    }
}

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
    public String toDBString();
    public double convertTo(double amount, Measure targetMeasure);
    
    public enum Weight implements Measure {
        MILLIGRAM("mg", 0.001),
        GRAM("g", 1),
        KILOGRAM("kg", 1000),
        OUNCE("oz", 28.35),
        POUND("lb", 453.59),
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

        @Override
        public String toDBString() {
            return this.name();
        }

        @Override
        public double convertTo(double amount, Measure targetMeasure) {
            if((targetMeasure instanceof Weight)) {
                return (amount * this.getFactor()) / targetMeasure.getFactor();
            } else {
                throw new UnsupportedOperationException("Cannot convert weight meassure to anything but weight");
            }
        }
    }
    
    public enum Volume implements Measure {
        MILLILITER("ml", 0.001),
        CENTILITER("cl", 0.01),
        DECILITER("dl", 0.1),
        LITER("L", 1),
        TEASPOON("tsp", 0.004),
        TABLESPOON("tbsp", 0.015),
        DROP("drop", 0.001 / 20.0),
        FLUID_OUNCE("fl oz", 0.029),
        CUP_US("cup us", 0.23659),
        CUP("cup", 0.250),
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

        @Override
        public String toDBString() {
            return this.name();
        }

        @Override
        public double convertTo(double amount, Measure targetMeasure) {
            if((targetMeasure instanceof Volume)) {
                return (amount * this.getFactor()) / targetMeasure.getFactor();
            } else {
                throw new UnsupportedOperationException("Cannot convert bolume meassure to anything but volume");
            }
        }
    }
}

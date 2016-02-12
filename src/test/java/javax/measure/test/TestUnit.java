/*
 * Units of Measurement API
 * Copyright (c) 2014-2016, Jean-Marie Dautelle, Werner Keil, V2COM.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-363 nor the names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.measure.test;

import java.util.Map;

import javax.measure.Dimension;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.IncommensurableException;
import javax.measure.UnconvertibleException;
import javax.measure.UnitConverter;
import javax.measure.quantity.Dimensionless;
import javax.measure.test.unit.BaseUnit;

/**
 * @author Werner Keil
 */
public abstract class TestUnit<Q extends Quantity<Q>> implements Unit<Q> {

    public static final Unit<Dimensionless> ONE = new BaseUnit<Dimensionless>("one");

    // public static final Unit<Mass> METRIC_MASS = QuantityFactory.getInstance(Mass.class).getMetricUnit();

    protected String symbol; // e.g. "A"
    protected final String name; // e.g. "Angstrom"
    protected double multFactor; // e.g. 1E-10
    double addFactor = 0.0; // used for temperatures
    final Dimension dimension = TestDimension.getInstance();
    		
    protected TestUnit() {
    	name = "";
    }

    public TestUnit(String name, double factor) {
        this.name = name;
        this.multFactor = factor;
    }

    protected TestUnit(String name) {
        this(name, 0);
    }

    public Unit<Q> shift(double offset) {
        return this;
    }

    public Unit<Q> alternate(String symbol) {
        return new BaseUnit(symbol);
    }

    public <T extends Quantity<T>> Unit<T> asType(Class<T> type) throws ClassCastException {
//      Unit<T> metricUnit = QuantityFactory.getInstance(type).getMetricUnit();
//      if ((metricUnit == null) || metricUnit.isCompatible(this))
//          return (Unit<T>) this;
//      throw new ClassCastException("The unit: " + this //$NON-NLS-1$
//              + " is not of parameterized type " + type); //$NON-NLS-1$
        return null;
    }

    public Unit<Q> divide(double divisor) {
        // TODO Auto-generated method stub
        return null;
    }

    public Unit<?> divide(Unit<?> that) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public UnitConverter getConverterTo(Unit<Q> that) throws UnconvertibleException {
		if ((this == that) || this.equals(that))
			return TestConverter.IDENTITY; // Shortcut.
		Unit<Q> thisSystemUnit = this.getSystemUnit();
		Unit<Q> thatSystemUnit = that.getSystemUnit();
		if (!thisSystemUnit.equals(thatSystemUnit))
			try {
				return getConverterToAny(that);
			} catch (IncommensurableException e) {
				throw new UnconvertibleException(e);
			}
		UnitConverter thisToSI = this.getSystemConverter();
		UnitConverter thatToSI = that.getConverterTo(thatSystemUnit);
		return thatToSI.inverse().concatenate(thisToSI);
    }

    public UnitConverter getConverterToAny(Unit<?> that)
		throws IncommensurableException, UnconvertibleException {
		if (!isCompatible(that))
			throw new IncommensurableException(this
					+ " is not compatible with " + that);
		TestUnit thatAbstr = (TestUnit) that; // Since both units are
														// compatible they must
														// be both test
														// units.
		Unit thisSystemUnit = this.getSystemUnit();
		UnitConverter thisToDimension = 
				this.getSystemConverter();
		Unit thatSystemUnit = thatAbstr.getSystemUnit();
		UnitConverter thatToDimension = 
				thatAbstr.getSystemConverter();
		return thatToDimension.inverse().concatenate(thisToDimension);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public String getName() {
        return name;
    }

    public Map<Unit<?>, Integer> getProductUnits() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSymbol() {
        return symbol;
    }

    public Unit<?> inverse() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isCompatible(Unit<?> that) {
        // TODO Auto-generated method stub
        return false;
    }

    public Unit<Q> multiply(double factor) {
        // TODO Auto-generated method stub
        return null;
    }

    public Unit<?> multiply(Unit<?> that) {
        // TODO Auto-generated method stub
        return null;
    }

    public Unit<?> pow(int n) {
        // TODO Auto-generated method stub
        return null;
    }

    public Unit<?> root(int n) {
        // TODO Auto-generated method stub
        return null;
    }

    public abstract Unit<Q> getSystemUnit();
    
	/**
	 * Returns the converter from this unit to its unscaled {@link #toSysemUnit System Unit}
	 * unit.
	 *
	 * @return <code>getConverterTo(this.toSystemUnit())</code>
	 * @see #toSI
	 */
    public UnitConverter getSystemConverter() throws UnsupportedOperationException {
        return TestConverter.IDENTITY;
    }
    
    public Unit<Q> transform(UnitConverter operation) {
        // TODO Auto-generated method stub
        return null;
    }

    public double getMultFactor() {
        return multFactor;
    }
    
    @Override
    public String toString() {
    	return getName();
    }
}
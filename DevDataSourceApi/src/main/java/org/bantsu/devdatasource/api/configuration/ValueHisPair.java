package org.bantsu.devdatasource.api.configuration;

/**
 * A data type that stores the history value of an object
 * @param <T> type of the value
 */
public class ValueHisPair <T>{

    /**
     * the object(usually the dynamic proxy object)
     */

    private Object obj = null;

    /**
     * the former value
     */
    private T formerValue = null;

    /**
     * the current value
     */
    private T currentValue = null;

    public ValueHisPair(Object obj, T formerValue, T currentValue) {
        this.obj = obj;
        this.formerValue = formerValue;
        this.currentValue = currentValue;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public T getFormerValue() {
        return formerValue;
    }

    public void setFormerValue(T formerValue) {
        this.formerValue = formerValue;
    }

    public T getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(T currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public String toString() {
        return "ValueHisPair{" +
                "formerValue=" + formerValue +
                ", currentValue=" + currentValue +
                '}';
    }
}

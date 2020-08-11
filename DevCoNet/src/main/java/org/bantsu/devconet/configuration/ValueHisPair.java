package org.bantsu.devconet.configuration;

public class ValueHisPair <T>{
    private Object obj = null;
    private T formerValue = null;
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

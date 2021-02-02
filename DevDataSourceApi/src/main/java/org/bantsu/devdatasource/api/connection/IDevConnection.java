package org.bantsu.devdatasource.api.connection;

import org.bantsu.devdatasource.api.operator.IDevParaOperator;

import java.lang.reflect.InvocationTargetException;

public interface IDevConnection {
    IDevParaOperator getDevParaOperator() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}

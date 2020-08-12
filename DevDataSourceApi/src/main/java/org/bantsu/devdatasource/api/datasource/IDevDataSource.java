package org.bantsu.devdatasource.api.datasource;

import org.bantsu.devdatasource.api.configuration.ConnectionType;
import org.bantsu.devdatasource.api.connection.IDevConnection;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;

public interface IDevDataSource {
    IDevConnection getConnection(ConnectionType connectionType) throws Exception;
}

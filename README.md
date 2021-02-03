# DevCoNet-Remastered

DevCoNet is a data persistence layer framework for accessing industrial devices. Through DevCoNet, you can easily access and manage data of certain industrial devices like PLC,  intelligent instrument, etc.
`Note that this project is in a very early stage, there is no guarantee that the results will be correct!`

## Intro
Once correctly configured DevCoNet, you can manage industrial data in the form of Java POJOs and access them with built-in getter and setters of POJOs just like operating the realistic  Java instances!
### Features
- Declarative configuration using annotations
- Automatic connection management 
- Concurrent access support for serial ports
- Provides support for transactions

### Modules

|Module Name|Description|
|----|----|
|DevCoNet|The framework|
|DevCoNetTest|Test case|
|DevDataSourceApi|Basic apis for implementing datasources|
|SocketDataSource|A example of datasource implementation|

## Install

They are all maven projects, so you can either import them to your existing projects or package them before use.

- Import the framework

  ```xml
    <dependency>
        <groupId>org.bantsu.devconet</groupId>
        <artifactId>DevCoNet</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
  ```

- Import data sources

  ```xml
    <dependency>
        <groupId>org.bantsu</groupId>
        <artifactId>DevSimDataSource</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
  ```

  If you have implemented a customized data source, you can also import it in the same way.

## How to use

### Define POJOs

  First, define the POJO, which indicates a group of device parameters you would like to interact with.

### Set annotations

@DevPoJo --> To declare this POJO as a collection of device parameters.
|Parameter name|Type|Description|
|----|----|----|
|value|String|an alias of this POJO|

@DevDataSources --> A collection of various device addresses (instances of data sources).
|Parameter name|Type|Description|
|----|----|----|
|DEV_DATA_SOURCE|DevDataSource[]|descriptions device addresses|

@DevDataSource --> A description of the device address.
|Parameter name|Type|Description|
|----|----|----|
|name|String|name for this data source|
|sourcePackageName|String|package name of this data source|
|TCP_CONFIG|TCPConfig|essential TCP parameters|
|SERIAL_PORT_CONFIG|SerialPortConfig|essential SerialPort parameters|

@TCPConfig --> Refer to the source code.

@SerialPortConfig --> Refer to the source code.

### A typical POJO

```java
package org.bantsu.test.devconet.domain;

import org.bantsu.devconet.anno.annos.DevPoJo;
import org.bantsu.devconet.anno.annos.DevSource;
import org.bantsu.devconet.anno.annos.datasourceconfig.DevDataSource;
import org.bantsu.devconet.anno.annos.datasourceconfig.DevDataSources;
import org.bantsu.devconet.anno.annos.datasourceconfig.SerialPortConfig;
import org.bantsu.devconet.anno.annos.datasourceconfig.TCPConfig;
import org.bantsu.devdatasource.api.configuration.ConnectionType;

import java.io.Serializable;

@DevPoJo("devParam")
@DevDataSources(DEV_DATA_SOURCE = {
        @DevDataSource(name = "devSim", sourcePackageName = "org.bantsu.devdatasource.devsim",
        TCP_CONFIG = @TCPConfig(ip = "127.0.0.1", port = 8080), SERIAL_PORT_CONFIG = @SerialPortConfig()),
        @DevDataSource(name = "socketSource", sourcePackageName = "org.bantsu.devdatasource.socketsource",
        TCP_CONFIG = @TCPConfig(ip = "192.168.3.50", port = 8000), SERIAL_PORT_CONFIG = @SerialPortConfig())
})
public class DevParam implements Serializable {

    @DevSource(dataSourceName = "socketSource", slot = "M", offset = 0, bitOffset = 0)
    private Boolean M0_0;

    @DevSource(dataSourceName = "socketSource", slot = "M", offset = 1)
    private Byte MB01;

    @DevSource(dataSourceName = "socketSource", slot = "M", offset = 4)
    private Integer MD04;

    @DevSource(dataSourceName = "socketSource", slot = "M", offset = 8)
    private Float MD08f;


    public Boolean getM0_0() {return M0_0;}

    public void setM0_0(Boolean m0_0) {M0_0 = m0_0;}

    public Byte getMB01() {return MB01;}

    public void setMB01(Byte MB01) {this.MB01 = MB01;}

    public Integer getMD04() {return MD04;}

    public void setMD04(Integer MD04) {this.MD04 = MD04;}

    public Float getMD08f() {return MD08f;}

    public void setMD08f(Float MD08f) { this.MD08f = MD08f;}

}
```

### Build DevManager

```java
//get a devManagerBuilder
IDevManagerBuilder devManagerBuilder =  new DevManagerBuilder();
//use this builder to build a devManager
DevManager devManager = (DevManager) devManagerBuilder.buildDevManager();
```

- `If your customized data source doesn't support concurrent tasks, we provide a ConcurrentDevManager to make things up. But when using this, the dispose() method should be called at the end of it's lifecycle.`

```java
//use this builder to build a ConcurrentDevManager, 'coreSize' is the corePoolSize of ThreadPool.
DevManager devManager = (DevManager) devManagerBuilder.buildConcurrentDevManager(coreSize);
```

### Enhance POJO

```java
//use devManager.getEnhancedDevPara() method to get enhanced POJO instance
DevParam devParam = (DevParam) devManager.getEnhancedDevPara(DevParam.class);
```

### Interact with device parameters using POJO instance

```java
//set
devParam.setMD04(1096);
//get
Boolen result = devParam.getM0_0();
```

### Transaction control

```java
DevTransactionManager transactionManager =
        new DevTransactionManager(devManager){
    //override devTransaction() with your codes
    @Override
    public void devTransaction() {
        int result = devParam.getMD04();
        rusult -= 100;
        devParam.setMD04(rusult);
        //Here comes an error
        int divByZero = 1 / 0;
        devParam.setM0_0(reult==42);
    }
};
//start the transaction
transactionManager.doTransaction();
```

### Implement your own datasources
`datasource` defines the fundamental interacting patterns with devices, should be implemented **before use**.
The impls should be put into the package named `*.operator`, in which `*` refer to the **full package name** of your datasource.

Two implements should be carried out, and must exactly follow the name `OperatorTCP` and `OperatorSerial` when using TCP and Serial respectively. Each of which should contain the constructor with one specific type of parameter, which is the type of DevConnectionTCP and DevConnectionSerial respectively.

A typical impl is as follows:
```java
// `org.bantsu.devdatasource.socketsource` is the full package name of this datasource
package org.bantsu.devdatasource.socketsource.operator;

import org.bantsu.devdatasource.api.connection.impl.DevConnectionTCP;
import org.bantsu.devdatasource.api.operator.IDevParaOperator;
import org.bantsu.devdatasource.socketsource.utils.SocketRequestHandler;
import java.io.IOException;

public class OperatorTCP implements IDevParaOperator {
    private DevConnectionTCP connection = null;
    private SocketRequestHandler srh = null;
    
	// (Must) A constructor with one parameter
    public OperatorTCP(DevConnectionTCP connection) throws IOException {
        this.connection = connection;
        //It is recommended that to maintain a long connection to the device in the Operator and assign it to a it's connetion using `this.connection.setTCPConnection(xxx)`
        if (this.connection.getTCPConnection() == null){
            this.connection.setTCPConnection(new SocketRequestHandler(this.connection.getHost(),this.connection.getPort()));
        }
        this.srh = (SocketRequestHandler)this.connection.getTCPConnection();
    }

    //write your own impls below
    @Override
    public Boolean getBit(String slot, int offset, int bitOffset) throws IOException {
        String cmd = "getb,"+offset+","+bitOffset;
        return Boolean.parseBoolean(this.srh.request(cmd).trim());
    }

    @Override
    public Boolean setBit(String slot, int offset, int bitOffset, boolean value) throws IOException {
        int valueInt = 0;
        valueInt = value?1:0;
        String cmd = "setb,"+offset+","+bitOffset+","+valueInt;
        return Boolean.parseBoolean(this.srh.request(cmd));
    }

    @Override
    public Byte getByte(String slot, int offset) throws IOException {
        return null;
    }

    @Override
    public Boolean setByte(String slot, int offset, byte value) throws IOException {
        return null;
    }

    @Override
    public Integer getDWord(String slot, int offset) throws IOException {
        String cmd = "getd,"+offset;
        return Integer.parseInt(this.srh.request(cmd).trim());
    }

    @Override
    public Boolean setDWord(String slot, int offset, int value) throws IOException {
        String cmd = "setd,"+offset+","+value;
        return Boolean.parseBoolean(this.srh.request(cmd));
    }

    @Override
    public Float getFloat(String slot, int offset) throws IOException {
        String cmd = "getf,"+offset;
        return Float.parseFloat(this.srh.request(cmd).trim());
    }

    @Override
    public Boolean setFloat(String slot, int offset, float value) throws IOException {
        String cmd = "setf,"+offset+","+value;
        return Boolean.parseBoolean(this.srh.request(cmd));
    }
}
```



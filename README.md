# DevCoNet-Remastered
A data persistence layer framework for accessing industrial devices.

## Intro
|Module Name|Description|
|----|----|
|DevCoNet|The framework|
|DevCoNetTest|test case|
|DevDataSourceApi|Basic apis of customized data sources|
|DevSim|Simple industrial device simulator|
|DevSimDataSource|Customized data sources sample|
#### Note that this project is in a very early stage, and documentation will be added later on.

## Install
They are all maven projects, so you can either import them to your exsiting projects or package them before use.

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
#### Define POJOs
  
  First, define the POJO, which indicates a group of device parameters you would like to interact with.

#### Set annotations
@DevPoJo --> To declare this POJO as a collection of device parameters.
|Parameter name|Type|Description|
|----|----|----|
|value|String|an alias of this POJO|

@DevDataSources --> A collection of various device addresses (instances of data sources). 
|Parameter name|Type|Description|
|----|----|----|
|value|String|an alias of this POJO|
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
#### A typical POJO
```java
  @DevPoJo("devParam")
  @DevDataSources(DEV_DATA_SOURCE = {
        @DevDataSource(name = "devSim_1", 
        sourcePackageName = "org.bantsu.devdatasource.devsim",
        TCP_CONFIG = @TCPConfig(ip = "192.168.1.1", port = 502),
        SERIAL_PORT_CONFIG = @SerialPortConfig()),
        @DevDataSource(name = "devSim_2", 
        sourcePackageName = "org.bantsu.devdatasource.devsim",
        TCP_CONFIG = @TCPConfig(ip = "192.168.1.2", port = 502),
        SERIAL_PORT_CONFIG = @SerialPortConfig())
  })
  public class DevParam implements Serializable {
    @DevSource(dataSourceName = "devSim_1", slot = "M", offset = 0, bitOffset = 0)
    private Boolean M0_0;

    @DevSource(dataSourceName = "devSim_2", slot = "V", offset = 4)
    private Integer MD04;


    public Integer getVD04() {
        return VD04;
    }

    public void setVD04(Integer VD04) {
        this.VD04 = VD04;
    }
  }
```
#### Enhance POJO 
```java
//get a devManagerBuilder
IDevManagerBuilder devManagerBuilder =  new DevManagerBuilder();
//use this builder to build a devManager
DevManager devManager = (DevManager) devManagerBuilder.buildDevManager();
//use devManager.getEnhancedDevPara() method get enhanced POJO instance
DevParam devParam = (DevParam) devManager.getEnhancedDevPara(DevParam.class);
```

#### Interact with device parameters using POJO instance
```java
//set
devParam.setVD04(1096);
//get
Boolen result = devParam.getM0_0();
```

#### Transaction control
```java
DevTransactionManager transactionManager = 
        new DevTransactionManager(devManager){
    //override devTransaction() with your codes
    @Override
    public void devTransaction() {
        int result = devParam.getMD04();
        rusult -= 100;
        devParam.setMD04(rusult);
        devParam.setM0_0(reult==42);
    }
};
//start the transaction
transactionManager.doTransaction();
```





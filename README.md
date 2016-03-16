Introduction
------------

In the construction of the framework was implemented the functions of persistence, data synchronization and replication functions will be necessary to allow the use of the application when the device is in offline mode. 

The main objective of the application model consists of developing mobile applications where to submit internet connectivity, the application to perform the requisitions next to the application server for that, in the absence of connectivity, the same conduct the consultations, cadasters, removals and updates the database on the device. 

The development of the framework has suffered a strong influence of the JPA, having as limiting the types of existing data in the SQLite database, if the developer does not want to be limited to the data types present in the SQLite can modify the form of persists the data by performing a new implementation of class responsible for persistence. The original model of application had no concern when the resolution of conflicts, and to the framework of the information returned from the application server always should the information that will prevail. 

Because it is a framework for mobile devices, some functional requirements should be met, because they express the functions or services that software should be able to perform or provide, which in general are processes that use inputs to produce outputs. For this reason were raised the following functional requirements:

• database creation: The framework should be able to create the database for the storage of information of the entities mapped.
• Register: The system should be able to perform a register of the entities mapped in framework;
• Fetch: The system should be able to perform the consultation of the entities mapped in the framework on the basis of the annotations present in class;
• Remove: The system should be able to perform the removal of entities mapped in framework.
• Update: The system should be able to perform the update of the entities mapped in framework.
• communication with the server: In the presence of connectivity the system should be able to perform the communication with the server.
• synchronization of data: synchronization is bilateral, occurring in both directions, both between the application server and the mobile device, as between the mobile device and the application server. The first occurs when the mobile device performs some requisition rest with the server, returning a given that will be stored in the local database from the mobile device. The second synchronization occurs only if any data present in the database of the mobile device suffer some modification for the change is sent to the application server.


Usage
-----------

####Mapping Class
-----

In relation to the method of setting of classes, the notes were based on the JPA persistence API of persistence, such as the Hibernate, Oracle TopLink and Java Data Objects. However, to meet the scenario of mobile computing were performed some simplifications for which were met the need of framework, where certain types of data, for not having compatibility in the SQLite, were discarded.

This simplification occurred before the fact of platform used to present a low processing in relation to an application server.

```java
@Entity
@Table("atividade")
public class Atividade implements PersistDB {

@Id
private Integer id;

@Column(name="codigo", length=14, allowNulls=false, typeColumn=TypeColumn.VARCHAR)
private String codigo ;

@Column (name ="titulo", length =14 , allowNulls = false , TypeColumn.TypeColumn.VARCHAR
private String titulo;

@ManyToOne
@Column(name = "id_evento", typeColumn = TypeColumn.INTEGER)
private Evento evento;

@ManyToOne
@Column(name = "id_tipo_atividade" , typeColumn = TypeColumn.INTEGER)
private TipoAtividade tipoAtividade ;

@Transient
private int idResponsavel;

...
}

```

The image exemplifies the mapping of a class that will be managed by OffDroid. Before exemplify the use of annotations, it should be emphasized that the whole class of domain must accomplish implementation of PersistDB interface. Regarding the use of annotations, the division of these was performed in two groups for a better explanation: annotations applied to persistent class and annotations inherent attributes that make up the class.

The annotations applied to persistent class are @Table, @Entity, @and @OnlyLocalStorage OnlyOnLine. The annotation @Entity informs to the framework that this class will representativeness in the local database of the device. The annotation @Table, which serves as a complement to the previous annotation, whose purpose is to inform the name of the database table that will be created for the storage of information of the entity.

The two other annotations presented already have been previously addressed. The annotations inherent to the attributes that make up the class are @Id, @Column, @ManyToOne and @Transient. The annotation @Id serves to indicate that the annotated attribute will be the primary key of the class, this will have a restriction registered automatically by the framework, making this way that one can have a duplicity of the same primary key. The annotation @Column has as purpose to inform the name that the attribute should be referenced in the database, as well as its maximum size and type of stored data.

This class still presents the annotation @ManyToOne that combined with the @Column, the purpose is to indicate that the current class presents a relationship, i.e. that the current class stores a reference to the class of the relationship. In this way, as informing previously, all the operations carried out in this class also will be held in classes mapped. In terms @Transient, this indicates the framework that the data contained in this attribute should not be persisted nor created a column for information storage in the database of the device.

####Setting Strategies

During the design of the framework were defined three possible strategies for manipulating the classes of domain, having as purpose to inform the framework which modules should be used during the manipulation of the class in question. The first strategy makes it possible to obtain data only in the context of mobile device that submit the annotation @OnlyLocalStorage. In the second, the data will exist only in the context where the device presents connectivity, who will submit the annotation @OnlyOnLine. Already in the third, the data should be part of the two contexts, both from the mobile device and the server application, in which case the classes of domain does not require any specific annotation.

The three possible strategies for the classes of field are:

• Online Only: This strategy should be applied in the class of the field when not judge necessary to perform the persistence of class to the database from the mobile device. In this case, the class will not submit representativeness in the database of the mobile device and this object can only be completed when the device display connectivity. To make use of this condition simply that the developer use annotation @OnlyOnLine;

• Local only: This strategy should be used when the class submit representativeness only in the database of the mobile device. This class should not be synchronized or sent to the application server. In order to make use of this strategy is needed that the developer makes use of a specific annotation @OnlyLocalStorage.

• Local and OnLine: The third strategy envisaged in the development of the framework was the need of data submit representativeness in the database of the mobile device and these same data are synchronized and sent to the application server. By judge that this would be the default condition used by developers who make use of the framework, it is not necessary to perform any annotation for that this strategy can be used.

####Startup

For the programming of the framework is necessary inform what the name of the database to be created within the mobile device, for only then being stored the application information. Should also be informed a list of classes that the framework should do the management of persistence, the operations inherent in them, and, finally, there must be informed on an instance of the class WebService, containing the URL of the service that the application will perform the requisitions and what standard will be used QueryParam or PathParam.

Once initialized the framework is not necessary to perform any other additional configuration to use it, because that the framework will maintain the configuration provided throughout its use, already being possible to use the framework and all its methods.

```java
try {
	List<String> list = new ArrayList<String>();
	list.add ("package + class name");
	list.add ("package + class name");
	OffDroidManager.createOffLineManager(context, "nameBD.db", list, new WebServiceImpl("Base Url", EstrategiaURL), EstrategiaAtualizacaoBD);
} catch ( Exception e ) {
	e.printStackTrace () ;
}
```

Libraries used in this project
------------------------------

* [acra] [acra]
* [mail] [mail]
* [paho-mqttclient] [paho-mqttclient]

License
-------

   The OffDroid framework is open-sourced software licensed under the LGPL license.

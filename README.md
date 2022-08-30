# hibernate-plain

Boilerplate code used to create ORM Hibernate connections with the corresponding base repository classes. 
Uses ```reflections``` library to find all the classes annotated with ```@Entity``` in the classpath. 
Extracted from old projects. 

#### Example of the utility HibernateInitializer class instantiation
```
HibernateInitializer.install(
    "org.project.core.data.pojo",
    DataProperty.of(Environment.DRIVER, "org.postgresql.Driver"),
    DataProperty.of(Environment.URL, "jdbc:postgresql://localhost/database"),
    DataProperty.system(Environment.USER, "user"),
    DataProperty.system(Environment.PASS, "password")
);
```

# hibernate-plain

The boilerplate code extracted from old projects', consists of utility classes to provide a convenient way of working with Hibernate core module in a singleton manner. Uses ```reflections``` to look up POJO classes.

#### Example of the HibernateInitializer instantiation.
```
HibernateInitializer.install(
    "org.project.core.data.pojo",
    DataProperty.of(Environment.DRIVER, "org.postgresql.Driver"),
    DataProperty.of(Environment.URL, "jdbc:postgresql://localhost/database"),
    DataProperty.system(Environment.USER, "user"),
    DataProperty.system(Environment.PASS, "password")
);
```

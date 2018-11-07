dataSource {
    pooled = true
//    driverClassName = "oracle.jdbc.driver.OracleDriver"
//    dialect = org.hibernate.dialect.Oracle10gDialect
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
}

// environment specific settings
environments {
    development {
        dataSource {
//            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            // build database server
            driverClassName = "org.h2.Driver"
            dbCreate = "create-drop"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            username = "sa"
            password = ""
        }
    }
    test {
        dataSource {
            driverClassName = "org.h2.Driver"
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            username = "sa"
            password = ""
        }
    }
    production {
        dataSource {
            driverClassName = "oracle.jdbc.driver.OracleDriver"
            dialect = org.hibernate.dialect.Oracle10gDialect
            // build database server
//            dbCreate = "update"
//            url = "jdbc:oracle:thin:@//ocm.eoc.afrl.af.mil:1521/cwe.build.devebs.afrl.af.mil"
//            username = "ebs_cwe_ctm"
//            password = "Ru_I3aL9yK_PF8vN"

            properties {
                maxActive = 32
                maxIdle = 2
                minIdle = 0
                initialSize = 2
                maxWait = -1
                maxAge = 21600000

                minEvictableIdleTimeMillis = 300000
                timeBetweenEvictionRunsMillis = 300000
                numTestsPerEvictionRun = 5
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                ignoreExceptionOnPreLoad = true
                validationQuery = "select 1 from dual"
                validationQueryTimeout = 10
                validationInterval = 300000
                jdbcInterceptors = "ConnectionState"
            }
        }
    }
}



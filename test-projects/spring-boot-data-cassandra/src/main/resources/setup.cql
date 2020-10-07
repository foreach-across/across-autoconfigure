CREATE KEYSPACE IF NOT EXISTS testKeySpace WITH replication = {'class':'SimpleStrategy','replication_factor':'1'};
USE testKeySpace;
CREATE TABLE customer (id TimeUUID PRIMARY KEY, firstname text, lastname text);
CREATE INDEX customerfistnameindex ON customer (firstname);
CREATE INDEX customersecondnameindex ON customer (lastname);
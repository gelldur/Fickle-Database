Fickle-Database
===============

Database change management for android something like Liquid Database


example

	<changeLog databaseName="fickle_test_database" >
		<changeSet
		    id="1"
		    author="John" >
		    <createTable
		        name="test_database"
		        comment="I create it for tests" >
		        <column
		            name="id"
		            type="integer"
		            nullable="false"
		            primaryKey="true" >
		        </column>
		        <column
		            name="name"
		            type="text"
		            nullable="false">
		        </column>
		    </createTable>
		</changeSet>
	</changeLog>

	=======================================================================

	Available document structure

	<changeLog databaseName="string" >
		<changeSet
		    id="string" 
		    author="string"
		    comment ="string" >
		    <createTable
		        name="string"
		        comment="string" >
		        <column
		            name="string"
		            type="integer|text|real"
		            nullable="true|false"
		            primaryKey="true|false"
		            comment="string" >
		        </column>
		    </createTable>
		    <deleteTable 
		    	name="string"
		    	comment="string" />
		    <sql command="string" comment="string"/>
		    <clearTable
		    	name="string"
		    	comment="string" />
		</changeSet>
	</changeLog>

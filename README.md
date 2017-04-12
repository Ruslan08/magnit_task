# Description
  There is table "test" in any data base, which contain 1 integer type column "field".  
  There is console application that performs the following functions:

1. Basic class is initialized with data to connection to the database and the number N.   
2. After starting, the application inserts into the table "test" N records with values of 1..N. If there are records in the "test" table, they are deleted before insertion.  
3. Then the application requests this data from "test"."field" and generates a valid XML document as follow:
<pre>
&lt;entries>     
    &lt;entry>         
        &lt;field>field value&lt;/field>     
    &lt;/entry>     
      ...     
    &lt;entry>         
        &lt;field>field value&lt;/field>     
    &lt;/entry> 
&lt;/entries> 
</pre>
(There are N nested elements &lt;entry>).  

4. With XSLT processor application converts first XML file as follow: 
<pre>
&lt;entries>    
  &lt;entry field="field value"/>     
  ...     
  &lt;entry field="field value"/> 
&lt;/entries> 
</pre>
(There are N nested elements &lt;entry>) 

5. Application parses new XML file and output arithmetic sum of the values of all attributes "field".   

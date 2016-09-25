This repository consists of an implementation of TPC-H tuning on PostgreSQL.

Detailed Description on my blog

akshatakerur.com

First step is to download the benchmark from http://www.tpc.org/tpc_documents_current_versions/download_programs/tools-download-request.asp?BM=TPC-H&mode=CURRENT-ONLY and extract it to a directory.

Now that the benchmark is downloaded, prepare your Makefile - copy from makefile.suite. Modify file as below beofre running:

DATABASE = DB2
MACHINE = LINUX
CC = GCC
WORKLOAD = TPCH

Compile using make.

Second step is to generate the date using dbgen tool. It uses a scale parameter based on which the data is generated. To generate 10 GB of data just do

$./dbgen -s 10 

This creates .tbl files. Move these files to your current working directory.

Along with the .tbl files, copy the create_table.sql, queries.txt and loadData.txt files to your workspace. 

Finally, execute the java files.

#!/bin/sh
#
#
echo "update customer set cust_passwd=md5('${2}') where cust_nickname='${1}' "  | psql -d openride

echo "select cust_nickname,  'md5:',cust_passwd from customer where cust_nickname='${1}'  " | psql -d openride 	

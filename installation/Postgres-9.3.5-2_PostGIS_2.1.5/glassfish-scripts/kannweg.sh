#!/bin/sh
#
#
#

for i in md2  md4 md5  rmd160  sha sha1
do  
echo testing algoritm $i :
echo OR-EoSgd  | openssl $i
done 

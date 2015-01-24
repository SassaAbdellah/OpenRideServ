echo "create user openride with password 'openride' " | psql
echo "ALTER DATABASE openride OWNER TO    openride  " | psql
echo "grant all privileges on openride to openride  " | psql

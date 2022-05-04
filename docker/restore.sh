user="gildar"
password="gildar"
fileName="dbBackup.dmp"

echo "-------------"
echo "-- Restore --"
echo "-------------"
echo
echo "Parametri"
echo
echo "  user: $user"
echo "  password: $password"
echo "  fileName: $fileName"
echo

read -p "Continuare (S/N)?" choice
case "$choice" in
  s|S )
      echo "Lo schema esistente sarà eliminato!"
      echo "    Rimozione schema $user"
      docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'DROP SCHEMA IF EXISTS $user CASCADE'"
      docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'DROP ROLE IF EXISTS $user'"

      docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c \"CREATE ROLE $user WITH LOGIN SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION PASSWORD '$password' \""
      docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'ALTER ROLE $user SET search_path TO $user'"
      docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'ALTER ROLE $user IN DATABASE postgres SET search_path TO $user'"
      docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'CREATE SCHEMA $user AUTHORIZATION $user'"

      echo "    Import schema $user"
      docker cp "$fileName" "gildar-postgres:/opt/backup.dmp"
      docker exec -t gildar-postgres /bin/bash -c "pg_restore -h localhost -U postgres -d postgres -n $user < /opt/backup.dmp"
  ;;
  * )
  exit 1
esac


# Chiede la conferma per continuare l'elaborazione
function Utl-Continua {
    Write-Host
    $continua = Read-Host -Prompt 'Continuare (S/N)?'

    if ($continua -eq "s" -or $continua -eq "S") {
      Write-Host "  Elaborazione avviata"
    } else {
      Write-Host "  Elaborazione abortita"
      exit
    }
}

$user="gildar"
$password="gildar"
$fileName="dbBackup.dmp"

Write-Host "-------------"
Write-Host "-- Restore --"
Write-Host "-------------"
Write-Host
Write-Host "Parametri"
Write-Host
Write-Host "  type: postgresql"
Write-Host "  user: $user"
Write-Host "  password: $password"
Write-Host "  fileName: $fileName"

# Conferma prosecuzione
Utl-Continua

# Restore Postgresql
Write-Host  "  Restore Postgresql"

Write-Host "    Rimozione schema $user"
docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'DROP SCHEMA IF EXISTS $user CASCADE'"
docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'DROP ROLE IF EXISTS $user'"

docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c \`"CREATE ROLE $user WITH LOGIN SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION PASSWORD '$password' \`""
docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'ALTER ROLE $user SET search_path TO $user'"
docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'ALTER ROLE $user IN DATABASE postgres SET search_path TO $user'"
docker exec -t gildar-postgres /bin/bash -c "psql -U postgres -d postgres -c 'CREATE SCHEMA $user AUTHORIZATION $user'"

Write-Host "    Import schema $user"
docker cp "$fileName" "gildar-postgres:/opt/backup.dmp"
docker exec -t gildar-postgres /bin/bash -c "pg_restore -h localhost -U postgres -d postgres -n $user < /opt/backup.dmp"

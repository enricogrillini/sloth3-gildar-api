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


# Backup Postgresql
Write-Host  "  Backup Postgresql"

docker exec -t gildar-postgres /bin/bash -c "pg_dump -h localhost -U $user -n $password -Fc postgres > /opt/backup.dmp"
docker cp "gildar-postgres:/opt/backup.dmp" "$fileName"

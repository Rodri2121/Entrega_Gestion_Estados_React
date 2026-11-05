# Servidor PostgreSQL propio del proyecto (puerto 5433), independiente del servicio PostgreSQL de Windows.
# Usa los binarios de PostgreSQL 18 ya instalados; no requiere la contraseña del usuario postgres
# ni permisos de administrador. Usuario: admin / admin1234, base: universidad.
#
# Uso:
#   powershell -ExecutionPolicy Bypass -File scripts\base-datos-local.ps1 -Accion iniciar
#   powershell -ExecutionPolicy Bypass -File scripts\base-datos-local.ps1 -Accion detener
#   powershell -ExecutionPolicy Bypass -File scripts\base-datos-local.ps1 -Accion estado

param([ValidateSet('iniciar', 'detener', 'estado')][string]$Accion = 'iniciar')

$bin = 'C:\Program Files\PostgreSQL\18\bin'
$data = Join-Path $env:USERPROFILE 'dev-local\pgdata-gestion-academica'
$log = Join-Path $env:USERPROFILE 'dev-local\pgdata-gestion-academica.log'
$puerto = 5433

switch ($Accion) {
    'iniciar' {
        if (-not (Test-Path $data)) {
            Write-Host "Creando el servidor en $data ..."
            $pw = New-TemporaryFile
            Set-Content -Path $pw -Value 'admin1234' -NoNewline
            & "$bin\initdb.exe" -D $data -U admin --pwfile=$pw --auth=scram-sha-256 -E UTF8 --locale=C | Out-Null
            Remove-Item $pw
        }
        # pg_ctl se ejecuta en una ventana oculta y solo se espera a pg_ctl (no al servidor),
        # así la consola no queda bloqueada.
        $p = Start-Process -FilePath "$bin\pg_ctl.exe" -ArgumentList @('-D', "`"$data`"", '-o', "`"-p $puerto`"", '-l', "`"$log`"", 'start') -WindowStyle Hidden -PassThru
        $p.WaitForExit()
        Start-Sleep -Seconds 2
        $env:PGPASSWORD = 'admin1234'
        $existe = & "$bin\psql.exe" -h localhost -p $puerto -U admin -d postgres -tAc "select 1 from pg_database where datname='universidad'"
        if (-not $existe) { & "$bin\createdb.exe" -h localhost -p $puerto -U admin universidad; Write-Host 'Base universidad creada.' }
        & "$bin\pg_isready.exe" -h localhost -p $puerto
    }
    'detener' { & "$bin\pg_ctl.exe" -D $data stop }
    'estado'  { & "$bin\pg_isready.exe" -h localhost -p $puerto }
}

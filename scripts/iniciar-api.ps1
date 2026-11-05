# Inicia la API Spring Boot gestion-academica en http://localhost:9090
# Uso:
#   powershell -ExecutionPolicy Bypass -File scripts\iniciar-api.ps1              (PostgreSQL en 5432)
#   powershell -ExecutionPolicy Bypass -File scripts\iniciar-api.ps1 -PuertoBD 5433 (servidor local del proyecto)
#
# La API exige dos variables de entorno:
#   JWT_SECRET      clave en Base64 de al menos 32 bytes (si no existe se genera una aleatoria)
#   ADMIN_PASSWORD  contraseña del ADMIN inicial (mínimo 8 caracteres; solo se usa si aún no hay ADMIN)

param([int]$PuertoBD = 5432)

if (-not $env:JWT_SECRET) {
    $bytes = New-Object byte[] 32
    [System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($bytes)
    $env:JWT_SECRET = [Convert]::ToBase64String($bytes)
    Write-Host "JWT_SECRET generado para esta ejecucion (los tokens anteriores dejan de ser validos)."
}
if (-not $env:ADMIN_PASSWORD) { $env:ADMIN_PASSWORD = 'Admin12345' }   # solo para desarrollo

# Sobrescribe solo el puerto de la URL definida en application.properties.
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:$PuertoBD/universidad"
Write-Host "Base de datos: $env:SPRING_DATASOURCE_URL"

Set-Location (Join-Path $PSScriptRoot '..\gestion-academica')
mvn spring-boot:run

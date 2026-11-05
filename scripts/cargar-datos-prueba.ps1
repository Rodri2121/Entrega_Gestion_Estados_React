# Carga datos de prueba en la API (con la API ya en ejecución).
# Uso:  powershell -ExecutionPolicy Bypass -File scripts\cargar-datos-prueba.ps1
# Crea 2 carreras, 3 estudiantes y dos usuarios extra para probar permisos (DOCENTE y ESTUDIANTE).
# Si un registro ya existe, la API responde 409 y el script lo informa sin detenerse.

param(
    [string]$BaseUrl = 'http://localhost:9090/api/v1',
    [string]$AdminUser = 'admin',
    [string]$AdminPassword = $(if ($env:ADMIN_PASSWORD) { $env:ADMIN_PASSWORD } else { 'Admin12345' })
)

$login = Invoke-RestMethod -Method Post -Uri "$BaseUrl/auth/login" -ContentType 'application/json' `
    -Body (@{ username = $AdminUser; password = $AdminPassword } | ConvertTo-Json)
$headers = @{ Authorization = "Bearer $($login.accessToken)" }
Write-Host "Sesion iniciada como $($login.username) ($($login.rol))"

function Post-Json($path, $data) {
    try {
        $r = Invoke-RestMethod -Method Post -Uri "$BaseUrl$path" -Headers $headers -ContentType 'application/json; charset=utf-8' `
            -Body ([System.Text.Encoding]::UTF8.GetBytes(($data | ConvertTo-Json)))
        Write-Host "  creado $path -> id $($r.id)"
        return $r
    } catch {
        Write-Host "  omitido $path : $($_.Exception.Message)"
        return $null
    }
}

Write-Host 'Carreras:'
Post-Json '/carreras' @{ codigo = 'ISI'; nombre = 'Ingeniería en Sistemas de Información' } | Out-Null
Post-Json '/carreras' @{ codigo = 'ARQ'; nombre = 'Arquitectura' } | Out-Null
$carreras = Invoke-RestMethod -Uri "$BaseUrl/carreras" -Headers $headers
$isi = ($carreras | Where-Object codigo -eq 'ISI').id
$arq = ($carreras | Where-Object codigo -eq 'ARQ').id

Write-Host 'Estudiantes:'
Post-Json '/estudiantes' @{ nombre = 'Ana López';     correo = 'ana.lopez@uam.edu.ni';     edad = 20; carrera = $isi } | Out-Null
Post-Json '/estudiantes' @{ nombre = 'Carlos Pérez';  correo = 'carlos.perez@uam.edu.ni';  edad = 22; carrera = $isi } | Out-Null
Post-Json '/estudiantes' @{ nombre = 'María Gómez';   correo = 'maria.gomez@uam.edu.ni';   edad = 19; carrera = $arq } | Out-Null

Write-Host 'Usuarios para probar roles:'
Post-Json '/usuarios' @{ username = 'docente1';    password = 'Docente123';    nombre = 'Docente de prueba';    rol = 'DOCENTE' } | Out-Null
Post-Json '/usuarios' @{ username = 'estudiante1'; password = 'Estudiante123'; nombre = 'Estudiante de prueba'; rol = 'ESTUDIANTE' } | Out-Null

Write-Host 'Listo.'

# =====================================================================
# Combined: MENU + DEPLOY (PowerShell 5/7)
# - Muestra el menú primero y espera tu elección en bucle.
# - Lógica de deploy encapsulada en funciones.
# =====================================================================

# ------------------------------
# Configuración por defecto
# ------------------------------
$DefaultUrl     = "https://github.com/2DM3A-PROYECTOS/RETO1/releases/download/v1.1.0/MercadoRibera.apk"
$DefaultSha256  = "57f603afa5c3e5dd679373a221ffde042bb59169cc02b67a3432a531046889f1"
$DefaultPackage = "com.example.reto1_dam_2025_26"

# ------------------------------
# Globals / config
# ------------------------------
$ErrorActionPreference = "Stop"
try { [Console]::OutputEncoding = [System.Text.Encoding]::UTF8 } catch {}
$TempApk = Join-Path $env:TEMP "MercadoRibera.apk"

# ------------------------------
# Logging helpers (ASCII-only)
# ------------------------------
function Info($m){ Write-Host "[*] $m" }
function Ok($m){ Write-Host " + $m" -ForegroundColor Green }
function Warn($m){ Write-Host "[WARN] $m" -ForegroundColor Yellow }
function Fail($m){ Write-Host "[ERR] $m" -ForegroundColor Red; exit 1 }

# ------------------------------
# TLS 1.2 para descargas
# ------------------------------
function Use-Tls12 { try { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12 } catch {} }

# ------------------------------
# Barra de progreso de descarga (HTTP)
# ------------------------------
function Download-FileWithProgress {
  param(
    [Parameter(Mandatory=$true)][string]$Url,
    [Parameter(Mandatory=$true)][string]$Destination
  )
  Use-Tls12
  $req = [System.Net.HttpWebRequest]::Create($Url)
  $req.Method = "GET"
  $resp = $req.GetResponse()
  try {
    $total = $resp.ContentLength
    $inStream  = $resp.GetResponseStream()
    $outStream = New-Object System.IO.FileStream($Destination, [System.IO.FileMode]::Create)
    try {
      $buffer = New-Object byte[] 65536  # 64KB
      $read = 0; $acc = 0L; $id = Get-Random
      do {
        $read = $inStream.Read($buffer, 0, $buffer.Length)
        if ($read -gt 0) {
          $outStream.Write($buffer, 0, $read)
          $acc += $read
          if ($total -gt 0) {
            $pct = [int](($acc * 100) / $total)
            Write-Progress -Id $id -Activity "Descargando APK" -Status "$pct% ($([Math]::Round($acc/1MB,2)) MB de $([Math]::Round($total/1MB,2)) MB)" -PercentComplete $pct
          } else {
            Write-Progress -Id $id -Activity "Descargando APK" -Status "$([Math]::Round($acc/1MB,2)) MB" -PercentComplete -1
          }
        }
      } while ($read -gt 0)
      Write-Progress -Id $id -Activity "Descargando APK" -Completed
    } finally {
      $outStream.Dispose()
      $inStream.Dispose()
    }
  } finally {
    $resp.Dispose()
  }
}

# ------------------------------
# Spinner para procesos externos
# ------------------------------
function Invoke-ProcessWithSpinner {
  param(
    [Parameter(Mandatory=$true)][string]$File,
    [Parameter(Mandatory=$true)][string]$Args,
    [string]$Message = "Procesando"
  )
  $tmpOut = [System.IO.Path]::GetTempFileName()
  $tmpErr = [System.IO.Path]::GetTempFileName()

  $p = Start-Process -FilePath $File -ArgumentList $Args `
        -RedirectStandardOutput $tmpOut -RedirectStandardError $tmpErr `
        -PassThru -WindowStyle Hidden

  $spinner = @('|','/','-','\')
  $i = 0
  while (-not $p.HasExited) {
    $c = $spinner[$i % $spinner.Length]
    Write-Host ("`r[$c] $Message   ") -NoNewline
    Start-Sleep -Milliseconds 120
    $i++
  }

  $out = ""; $err = ""
  try { $out = Get-Content -Raw -ErrorAction SilentlyContinue $tmpOut } catch {}
  try { $err = Get-Content -Raw -ErrorAction SilentlyContinue $tmpErr } catch {}
  Remove-Item -ErrorAction SilentlyContinue $tmpOut, $tmpErr

  Write-Host ("`r[*] $Message            ")

  return [pscustomobject]@{
    ExitCode = $p.ExitCode
    Output   = ($out + "`n" + $err)
  }
}

# ------------------------------
# Asegurar Platform-Tools (adb)
# ------------------------------
function Ensure-PlatformTools {
  param([switch]$PersistPathIn)

  function Add-ToPathIfNeeded([string]$dir) {
    if ([string]::IsNullOrWhiteSpace($dir)) { return $false }
    $adbExe = Join-Path $dir "adb.exe"
    if (-not (Test-Path $adbExe)) { return $false }

    if ($env:Path -notlike "*$dir*") { $env:Path = "$env:Path;$dir" }
    if ($PersistPathIn) {
      try {
        $userPath = [Environment]::GetEnvironmentVariable("Path","User")
        if ($userPath -notlike "*$dir*") {
          [Environment]::SetEnvironmentVariable("Path", "$userPath;$dir", "User")
        }
      } catch { }
    }
    Ok ("ADB disponible en: " + $dir)
    return $true
  }

  if (Get-Command adb -ErrorAction SilentlyContinue) {
    Ok "ADB encontrado en PATH"
    return
  }

  $candidates = @()
  if ($env:LOCALAPPDATA) {
    $candidates += (Join-Path $env:LOCALAPPDATA "Android\platform-tools")
    $candidates += (Join-Path $env:LOCALAPPDATA "Android\Sdk\platform-tools")
  }
  if ($env:ProgramFiles) {
    $candidates += (Join-Path $env:ProgramFiles "Android\platform-tools")
  }
  if ($env:ANDROID_HOME)     { $candidates += (Join-Path $env:ANDROID_HOME "platform-tools") }
  if ($env:ANDROID_SDK_ROOT) { $candidates += (Join-Path $env:ANDROID_SDK_ROOT "platform-tools") }

  foreach ($dir in $candidates) {
    if (Test-Path (Join-Path $dir "adb.exe")) {
      if ($env:Path -notlike "*$dir*") { $env:Path = "$env:Path;$dir" }
      if ($PersistPathIn) {
        try {
          $userPath = [Environment]::GetEnvironmentVariable("Path","User")
          if ($userPath -notlike "*$dir*") {
            [Environment]::SetEnvironmentVariable("Path", "$userPath;$dir", "User")
          }
        } catch {}
      }
      Ok ("ADB disponible en: " + $dir)
      return
    }
  }

  Info "ADB no encontrado. Descargando Android Platform-Tools..."
  $baseDir = Join-Path $env:LOCALAPPDATA "Android"
  $ptDir   = Join-Path $baseDir "platform-tools"
  $zipPath = Join-Path $env:TEMP "platform-tools-latest-windows.zip"
  $urlPT   = "https://dl.google.com/android/repository/platform-tools-latest-windows.zip"

  if (-not (Test-Path $baseDir)) { New-Item -ItemType Directory -Path $baseDir | Out-Null }

  try {
    Download-FileWithProgress -Url $urlPT -Destination $zipPath
  } catch {
    Fail ("No se pudo descargar Platform-Tools: " + $_.Exception.Message)
  }

  if (Test-Path (Join-Path $ptDir "adb.exe")) {
    if (Add-ToPathIfNeeded $ptDir) { return }
  }

  if (Test-Path $ptDir) { try { Remove-Item -Recurse -Force $ptDir } catch {} }
  try {
    Invoke-ProcessWithSpinner -File "powershell" -Args "-NoLogo -NoProfile -Command Expand-Archive -LiteralPath `"$zipPath`" -DestinationPath `"$baseDir`" -Force" -Message "Descomprimiendo Platform-Tools" | Out-Null
  } catch {
    Fail ("No se pudo descomprimir Platform-Tools: " + $_.Exception.Message)
  }

  if (-not (Test-Path (Join-Path $ptDir "adb.exe"))) {
    Fail "Instalacion de Platform-Tools invalida (no existe adb.exe)"
  }

  if (-not (Add-ToPathIfNeeded $ptDir)) {
    Fail "No se pudo agregar Platform-Tools al PATH"
  }

  Ok ("Platform-Tools listo en: " + $ptDir)
}

# ------------------------------
# Helpers ADB
# ------------------------------
function Is-AdbSuccess([string]$text) { return ($text -match "(?im)^\s*Success\s*$") }

function Get-SingleOnlineDeviceId {
  & adb start-server | Out-Null
  $lines = (& adb devices) -split "`n"
  $online = @()
  foreach($ln in $lines){
    if (($ln -match "device$") -and ($ln -notmatch "^List")) { $online += (($ln -split "`t")[0]) }
  }
  if ($online.Count -eq 0) { Fail "No hay emuladores/dispositivos online. Inicia un AVD y reintenta." }
  if ($online.Count -gt 1) { & adb devices; Fail "Multiples dispositivos online. Deja solo uno o especifica uno." }
  return $online[0]
}

function Get-DeviceLabel([string]$id) {
  try {
    $name = (& adb -s $id emu avd name 2>$null).Trim()
    if (-not [string]::IsNullOrWhiteSpace($name)) { return "$name ($id)" }
  } catch {}
  return $id
}

# ------------------------------
# MAIN empaquetado como funcion invocable
# ------------------------------
function Invoke-DeployApk {
<#
.SYNOPSIS
  Instala (y opcionalmente lanza) una APK en un unico dispositivo/emulador online.
.PARAMETER Url
  URL de descarga de la APK.
.PARAMETER Sha256
  Hash SHA-256 esperado para validar la descarga.
.PARAMETER Launch
  Si se especifica, lanza la app tras instalarla.
.PARAMETER Package
  Nombre de paquete (para desinstalar en caso de firma incompatible y para lanzar).
.PARAMETER PersistPath
  Agrega platform-tools al PATH de usuario de forma persistente.
#>
  param(
    [string]$Url    = $DefaultUrl,
    [string]$Sha256 = $DefaultSha256,
    [switch]$Launch,
    [string]$Package = $DefaultPackage,
    [switch]$PersistPath
  )

  Ensure-PlatformTools -PersistPathIn:$PersistPath
  if (-not (Get-Command adb -ErrorAction SilentlyContinue)) {
    Fail "ADB sigue sin estar disponible tras la instalacion."
  }

  $needDownload = $true
  if (Test-Path $TempApk) {
    Info ("APK encontrada localmente: " + $TempApk)
    try {
      $localHash = (Get-FileHash -Path $TempApk -Algorithm SHA256).Hash.ToLower()
      if ($localHash -eq $Sha256.ToLower()) { Ok "Hash coincide. Se omite descarga."; $needDownload = $false }
      else { Warn "Hash distinto. Se descargara nuevamente." }
    } catch { Warn "No se pudo verificar hash local. Se descargara de nuevo." }
  }
  if ($needDownload) {
    Info "Descargando APK desde GitHub..."
    try { Download-FileWithProgress -Url $Url -Destination $TempApk }
    catch { Fail ("Fallo descargando la APK: " + $_.Exception.Message) }
    Ok ("Descarga completa: " + $TempApk)
  }

  Info "Verificando SHA-256..."
  try { $calc = (Get-FileHash -Path $TempApk -Algorithm SHA256).Hash.ToLower() }
  catch { Fail ("No se pudo calcular SHA-256: " + $_.Exception.Message) }
  if ($calc -ne $Sha256.ToLower()) {
    Write-Host ("Esperado : " + $Sha256)
    Write-Host ("Calculado: " + $calc)
    Fail "SHA-256 no coincide"
  }
  Ok "SHA-256 correcto"

  Info "Comprobando emulador/dispositivo con adb..."
  $deviceId = Get-SingleOnlineDeviceId
  $label = Get-DeviceLabel $deviceId
  Ok ("Dispositivo detectado: " + $label)

  Info "Instalando APK..."
  $res = Invoke-ProcessWithSpinner -File "adb" -Args ("install -t -r -d `"$TempApk`"") -Message "adb install"

  if (Is-AdbSuccess $res.Output) {
    Ok "Instalacion completada"
  }
  elseif ($res.Output -match "INSTALL_FAILED_UPDATE_INCOMPATIBLE") {
    if ([string]::IsNullOrWhiteSpace($Package)) {
      Fail "Firma incompatible. Ejecuta con -Package com.tu.app para desinstalar y reintentar, o desinstala manualmente."
    }
    Warn "Firma incompatible detectada. Desinstalando $Package y reintentando..."
    & adb uninstall $Package | Out-Null
    $res2 = Invoke-ProcessWithSpinner -File "adb" -Args ("install -t -r -d `"$TempApk`"") -Message "adb install (reintento)"
    if (Is-AdbSuccess $res2.Output -or $res2.ExitCode -eq 0) { Ok "Reinstalacion completada tras desinstalar la version previa." }
    else { Fail ("Fallo tras desinstalar y reintentar. Salida:`n" + $res2.Output) }
  }
  elseif ($res.ExitCode -eq 0) {
    Ok "Instalacion completada (exit code 0, sin 'Success' en salida)."
  }
  else {
    Fail ("Fallo instalando APK. Salida:`n" + $res.Output)
  }

  if ($Launch) {
    if ([string]::IsNullOrWhiteSpace($Package)) {
      Warn "No se especifico Package. Usa -Package com.tu.app para lanzar tras la instalacion."
    } else {
      Info ("Lanzando app: " + $Package)
      $m1 = Invoke-ProcessWithSpinner -File "adb" -Args ("shell monkey -p $Package -c android.intent.category.LAUNCHER 1") -Message "Lanzando app"
      if ($m1.Output -match "(?i)Events injected:\s*1") { Ok "App lanzada" }
      else {
        Warn "No se confirmo el lanzamiento con monkey. Probando am start..."
        $m2 = Invoke-ProcessWithSpinner -File "adb" -Args ("shell am start -a android.intent.action.MAIN -c android.intent.category.LAUNCHER $Package") -Message "am start"
        if ($m2.Output -match "(?i)Starting:" -or $m2.ExitCode -eq 0) { Ok "App lanzada con am start" }
        else { Warn ("No se pudo confirmar el lanzamiento. Salida:`n" + $m1.Output + "`n---`n" + $m2.Output) }
      }
    }
  }

  Ok "Proceso completado."
}

# ------------------------------
# UI (banner, menu, pausa)
# ------------------------------
function Banner {
@'
    __  ___                         __      ____  _ __                   
   /  |/  /__  ______________ _____/ /___  / __ \(_) /_  ___  _________ _
  / /|_/ / _ \/ ___/ ___/ __ `/ __  / __ \/ /_/ / / __ \/ _ \/ ___/ __ `/
 / /  / /  __/ /  / /__/ /_/ / /_/ / /_/ / _, _/ / /_/ /  __/ /  / /_/ / 
/_/  /_/\___/_/   \___/\__,_/\__,_/\____/_/ |_/_/_.___/\___/_/   \__,_/  
                       _____           _       __                        
                      / ___/__________(_)___  / /_                       
 ______    ______     \__ \/ ___/ ___/ / __ \/ __/  ______    ______     
/_____/   /_____/    ___/ / /__/ /  / / /_/ / /_   /_____/   /_____/     
                    /____/\___/_/  /_/ .___/\__/                         
                                    /_/                                  
'@ | Write-Host -ForegroundColor Red
}

function Pause([string]$msg="Presiona ENTER para continuar...") {
  Write-Host ""
  Read-Host $msg | Out-Null
}

function Show-Menu {
  Clear-Host
  Banner
  Write-Host "1) Instalar APK y desplegarla"
  Write-Host "2) Desinstalar (EN DESARROLLO)"
  Write-Host "3) Ingresar version a desplegar (EN DESARROLLO)"
  Write-Host "4) Salir"
  Write-Host ""
}

# ------------------------------
# Arranque del MENÚ (se muestra primero y queda esperando)
# ------------------------------
# BaseDir por si necesitas rutas relativas en el futuro
$BaseDir = $PSScriptRoot
if ([string]::IsNullOrEmpty($BaseDir)) { $BaseDir = Split-Path -Parent $PSCommandPath }
if ([string]::IsNullOrEmpty($BaseDir)) { $BaseDir = [System.AppDomain]::CurrentDomain.BaseDirectory }
Set-Location $BaseDir

# Vars del menú (puedes modificarlas antes de elegir 1)
$package = $DefaultPackage
$apkUrl  = $DefaultUrl
$apkSha  = $DefaultSha256

while ($true) {
  Show-Menu
  $opt = Read-Host "Elige una opcion (1-4)"
  switch ($opt) {
    '1' {
      try {
        Invoke-DeployApk -Url $apkUrl -Sha256 $apkSha -Package $package -Launch
      } catch {
        Write-Host "[ERR] Error ejecutando despliegue: $($_.Exception.Message)" -ForegroundColor Red
      }
      Pause
    }
    '2' {
      Write-Host "pronta funcionalidad" -ForegroundColor Yellow
      Pause
    }
    '3' {
      Write-Host "pronta funcionalidad" -ForegroundColor Yellow
      Pause
    }
    '4' {
      Write-Host "Cerrando aplicacion..." -ForegroundColor Cyan
      Start-Sleep -Milliseconds 600
      break
    }
    default {
      Write-Host "[WARN] Opcion invalida." -ForegroundColor Yellow
      Start-Sleep -Milliseconds 900
    }
  }
}

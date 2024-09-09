@VDS
@VDSLogin
Feature: Ingreso de Usuario
  ##Pre condicion comun a todos los casos
  Background: Usuario en la pagina de Login
    Given Me encuentro en la pagina de login VDS con el navegador "chrome"

  @VDS1
    ##Login con credenciles validas obtenidas desde archivo txt
  Scenario: Ingreso exitoso con credenciales válidas
    When ingreso credenciales validas
      And hago clic en el botón de inicio de sesión
    Then debería ser redirigido a la página de inicio

  @VDS2
    ##Escenario outline, nos permite realizar varias veces el caso con diferente parametrizacion
  Scenario Outline: Ingreso no exitoso con credenciales no válidas
    When ingreso mi nombre de usuario "<Usuario>" y mi contraseña "<Contraseña>"
      And hago clic en el botón de inicio de sesión
    Then debería visualizarse alerta "Login Failure: The account password is incorrect, the verification code is incorrect, the account is locked, the account is not valid, or the account is a machine-machine user."
    Examples:
      | Usuario    | Contraseña   |
      | VDS005     | Admin12      |
      | VDS0005    | CaNoB12#$%   |

  @VDS3
    ##Login con ingresos de texto con delay
  Scenario: Ingreso exitoso con credenciales válidas
    When ingreso credenciales con delay
      And hago clic en el botón de inicio de sesión
    Then debería ser redirigido a la página de inicio





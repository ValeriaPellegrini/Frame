@EjemplosX
@Orange
Feature: Ingreso de Usuario
  ##Pre condicion comun a todos los casos
  Background: Usuario en la pagina de Login
    Given Me encuentro en la pagina de login Orange con el navegador "chrome"

  @Orange1
    ##Login con credenciles validas obtenidas desde archivo txt
  Scenario: Ingreso exitoso con credenciales válidas
    When ingreso credenciales validas
      And hago clic en el botón de inicio de sesión
    Then debería ser redirigido a la página del tablero de mi cuenta

  @Orange2
    ##Escenario outline, nos permite realizar varias veces el caso con diferente parametrizacion
  Scenario Outline: Ingreso exitoso con credenciales válidas
    When ingreso mi nombre de usuario "<Usuario>" y mi contraseña "<Contraseña>"
      And hago clic en el botón de inicio de sesión
    Then debería visualizarse alerta "Invalid credentials"
    Examples:
      | Usuario | Contraseña |
      | Admin   | admin12    |
      | Admi    | admin123   |

  @Orange3
    ##Login con ingresos de texto con delay
  Scenario: Ingreso exitoso con credenciales válidas
    When ingreso credenciales con delay
      And hago clic en el botón de inicio de sesión
    Then debería ser redirigido a la página del tablero de mi cuenta






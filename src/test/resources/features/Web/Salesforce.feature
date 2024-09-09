  @Salesforce
Feature: Ejemplos de ingreso de archivos y iframes

  Background: Abrir navegador
    Given Me encuentro en la pagina de salesforce con el navegador "Chrome"

  @LoginOk
  Scenario: Login por el ingreso normal del sitio
    When Ingreso al sitio con credenciales validas
    Then Visualizo la Home

  @LoginOk
  Scenario: Login por el ingreso Login Interno
    When Ingreso al sitio de ingreso con credenciales internas
      And Ingreso con credenciales de tu id
    Then Visualizo la Home

  @LoginFail
  Scenario Outline: Intento de login invalido
    When Ingreso al sitio con "<usuario>" y "<contraseña>"
    Then Visualizo mensaje "Please check your username and password. If you still can't log in, contact your Salesforce administrator."
    Examples:
      | usuario                       | contraseña |
      | uat592149@telecom.com.ar.uat2 | error      |
      | uat592149@telecom.com.ar      | error      |

  @Formulario
  Scenario: Completo formulario de gestion de cliente con dirección valida
    Given Ingreso al sitio con credenciales validas
      And Ingreso a las gestion de clientes
      And Busco cliente
        | TIPO DE DOCUMENTO   | DNI       |
        | NUMERO DE DOCUMENTO | 41689360  |
        | Género              | Masculino |
      And Ingreso a crear nuevo cliente
      And Completo formulario
        | Provincia           | CAPITAL FEDERAL |
        | Localidad           | CAPITAL FEDERAL |
        | Calle               | TERRADA         |
        | Altura              | 5404            |
        | Tipo de zona        | URBANA          |
        | Tipo de domicilio   | CASA            |
      Then  Se visaliza " Dirección Validada"
      # Se toma con un espeacio inicial ya que asi se encuentra en el sitio. Para estos casos igual se puede
      # tratar mediante la aplicación de un .trim() al string para sacar espacios anteriores o postiores al texto.
      # Para el caso se dejo el espacio para que a la hora de comparar con el sitio se considere OK.

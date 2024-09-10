#Tag de feature
@Ejemplos
Feature: Ejemplos de las diferentes herramientas y posibilidades del framework

  #Precondición de todos los casos
  Background: Abrir navegador
    Given Abro el navegador "Chrome"

  #Tag de caso
  @Ejemplo1
  Scenario: Completar un formulario con multiples parametros ingresados
    Given Me encuentro en el sitio de pruebas del formulario
    When completo el formulario
        |Cantidad de dinero       | 1000    |
        |Tasa de interes          | 10      |
        |Periodo                  | Mensual |
        |Tiempo                   | 365     |
        |Medida de tiempo         | Año(s)  |
      And calculo el interes
    Then el resultado es "438000"


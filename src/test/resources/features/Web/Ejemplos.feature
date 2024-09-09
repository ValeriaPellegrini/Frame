#Tag de feature
@Ejemplos
Feature: Ejemplos de las diferentes herramientas y posibilidades del framework

  #Precondición de todos los casos
  Background: Abrir navegador
    Given Abro el navegador "Chrome"

  #Tag de caso
  @EjemploCanales
  Scenario: Ingresar al Servicio Canales Digitales
    Given Me encuentro en el sitio de itickets
    When hago click en Apps
      And selecciono la opción Negocio - G. Técnica
    Then ingreso a Apps Negocio Gestión Técnica

#        |Cantidad de dinero       | 1000    |
#        |Tasa de interes          | 10      |
#        |Periodo                  | Mensual |
#        |Tiempo                   | 365     |
#        |Medida de tiempo         | Año(s)  |

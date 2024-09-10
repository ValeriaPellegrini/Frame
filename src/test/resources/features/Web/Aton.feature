
@Aton
Feature: Ingreso de Usuario
  ##Pre condicion comun a todos los casos
  Background: Usuario en la pagina de Login
    Given Me encuentro en el sitio de iTickets con el navegador "chrome"

  @CanalesDigitales1

  Scenario: Ingresar a Apps Negocio Gestión Técnica
    When hago click en Apps
      And selecciono la opción Negocio - G. Técnica
    Then ingreso a Apps Negocio Gestión Técnica

  @CanalesDigitales2
   
  Scenario: Ingresar al Servicio Canales Digitales
    Given Me encuentro en la sección de Apps Negocio Gestión Técnica
    When hago click en Canales Digitales
      And hago click en el botón Pedir Ahora
    Then ingreso al formulario de Canales Digitales
  
  @CanalesDigitales3
  @CanalesDigitales3_1
   
  Scenario: Realizar Petición SalesForce
    Given Me encuentro en la sección de Peticiones de Canales Digitales
    When hago click en desplegable Seleccionar
      And selecciono "Salesforce"
      And presiono Siguiente
    Then ingreso al formulario de peticiones "Salesforce" de Canales Digitales

  @CanalesDigitales3_2

  Scenario: Realizar Petición Open
    Given Me encuentro en la sección de Peticiones de Canales Digitales
    When hago click en desplegable Seleccionar
      And selecciono "Open"
      And presiono Siguiente
    Then ingreso al formulario de peticiones "Open" de Canales Digitales

  @CanalesDigitales3_3

  Scenario: Realizar Petición Siebel
    Given Me encuentro en la sección de Peticiones de Canales Digitales
    When hago click en desplegable Seleccionar
      And selecciono "Siebel"
      And presiono Siguiente
    Then ingreso al formulario de peticiones "Siebel" de Canales Digitales
  
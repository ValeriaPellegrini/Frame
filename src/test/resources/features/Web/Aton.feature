
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

  @CanalesDigitales3_4

  Scenario: Realizar Petición Ninguno
    Given Me encuentro en la sección de Peticiones de Canales Digitales
    When hago click en desplegable Seleccionar
      And selecciono "Ninguno"
    Then visualizo mensaje de "Realice su selección entre los valores disponibles" 
  
  @CanalesDigitales4

  Scenario: Salesforce- Cargar Formulario con valores válidos para canal Mi Personal
    Given Me encuentro en la sección de Peticiones "Salesforce" de Canales Digitales
    When se despliegan opciones del botón Seleccionar
      And selecciono el canal "Mi Personal"
      And completo el formulario del canal seleccionado
        |¿Dónde se presenta el error?                                         | Mi Personal App        |
        |¿Qué tipo de error necesitás reportar?                               | De Acceso              |
        |Indicanos el Número de Identificación(DNI/CUIL/CUIT)                 | 00012345678            |
        |Indicanos el número de contrato / Cuenta                             | 365                    |
        |Indicanos el Correo Electrónico del Cliente                          | prueba@prueba.com      |
        |Detallá el código del error                                          | ER123456               |
        |Contanos algo más que nos pueda servir con tu pedido                 | PRUEBA                 |
        |Acordate de adjuntar la imagen del Error                             | Files/DNI-pruebas.jpg  |
      And selecciono Enviar petición
    Then se verifica la creación del ticket
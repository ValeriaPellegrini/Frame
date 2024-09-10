#Tag de feature
@Ejemplos
Feature: Ejemplos de ingreso de archivos y iframes

  #Precondición de todos los casos
  Background: Abrir navegador
    Given Me encuentro en la pagina de html con el navegador "Chrome"

    @Archivo
      @Frame
  Scenario: Prueba
    When Cargo imagen en el ultimo ejemplo de formulario
      And Visualizo la informacion de la imagen "File name DNI-pruebas.jpg, file size 45.1 KB."
      And Presiono cargar
    Then Recibo mensaje "Image uploaded!"


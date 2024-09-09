
<hr style="border:2px solid blue">
<h1>&nbsp;Framework de Automatización con Cucumber (AKA FAC) Versión 2.0</h1>

<hr style="border:2px solid blue">
<h4>Punto de partida:</h4>
		
&nbsp;&nbsp;&nbsp;Para una ejecución local, el punto de partida es un comando de Maven con atributos relativos. Los atributos podrían ser URL del entorno, test a ejecutar, browser, etc.<br>

Para facilitar el proceso se puede generar un batch local. La imagen del repositorio debería ejecutarse de manera similar, por línea de comando en un servidor de automatización como Jenkins.<br>

**Contenido del batch de ejemplo:**

`mvn clean test -Dgroups="LoginOk"`
	
- **mvn** *Comando de ejecución de Maven (Apache Maven es una herramienta de comprensión y gestión de proyectos de software)*
- **clean** *El complemento de Maven Clean intenta limpiar los archivos y directorios generados por Maven durante su compilación.*
- **test** *El complemento Maven Surefire se utiliza durante la fase de prueba del ciclo de vida de la compilación, para ejecutar las pruebas unitarias de una aplicación.*
- **-Dgroups** *Representa los test a ejecutar identificados por tags. Los tags se generan con una anotación de Cucumber, arriba de la línea de título de un escenario, en un archivo de features (Ej: @Smoke, @LoginOk, @LoginFailByWebElement, etc.). Si se envía con un valor vacío (""), se ejecutarán todos los test disponibles.*
<br>

Parámetros adicionales que se envían a la línea de comando, pueden ser utilizados en el código por medio de la instrucción "System.getProperty("nombreParametro")".

Los parámetros sensibles requeridos por los casos de prueba (usuarios, contraseñas, urls, ips, etc), deben ser agregados al archivo "TestValues.txt", en la ruta "src\test\resources\TestValues" dentro del proyecto. El archivo debe ser creado localmente cuando se descarga el proyecto la primera vez. Este archivo está configurado en el git ignore dado que podría contener información sensible que no debe ser expuesta, por lo que no se lo debe subir al repositorio.

Ejemplo del contenido:
```
user=standard_user
pass=secret_sauce
URL=https://www.saucedemo.com/
```

Los parametros sensibles se pueden utilizar en el codigo con el llamado:<br>
`AutoTool.testValues().getValue("nombreParametro");`.<br>

*Se debe utilizar Java ver 11, dado que versiones posteriores presentan un bug con Junit (Junit 5.9 debería solucionarlo, se probará en próximas versiones).*<br>
<br>
<hr style="border:2px solid blue">

<h4>Setup del Framework:</h4>
		
<h4>Dependencias:</h4>
	
- **Cucumber**
- **Junit**
- **Selenium**
- **Rest-assured**
<br>

------------


&nbsp;&nbsp;&nbsp;La configuración del framework se encuentra principalmente en los siguientes archivos:

&nbsp;&nbsp;&nbsp;1) Clase runner de Cucumber:<br>&ensp; "/FAC/src/test/java/com/telecom/runner/RunCucumberTest.java"

&nbsp;&nbsp;&nbsp;Se listan los parámetros y su función:
			
			A) "pretty,html:target/cucumber-reports.html,json:target/cucumber.json"
				  Estos parámetros se aplican para generar dos reportes sobre la ejecución, uno con formato HTML y otro Json.
				
			B) Cucumber hook BeforeAll.
					
				//Se ejecuta una única vez antes de todos los escenarios
				@BeforeAll
				public static void frameworkSetup() {
					//Crea una instancia de variables del sistema para cada thread, se utiliza para logs estrictos
					System.setProperties(new ThreadLocalProperties(System.getProperties()));
					//Gestiona los valores del archivo config.properties para su posterior utilización
					AutoTool.loadSetupValues();
					//Carga el contenido del archivo“TestValues.txt”
					AutoTool.testValues().loadMainTestValues();
					//Configura el número máximo de threads concurrentes que el semáforo debe permitir en la ejecución
					SemaphoreControl.setupSemaphore();
					//Crea un archivo para logs estrictos si la función esta activa
					LogHandler.startSuiteLogFile();
				}

			C) Cucumber hook Before.
			
				//Se ejecuta una vez antes de cada escenario
				@Before(order=0)
				public void scenarioSetup(Scenario scenario) {
					//Controla la cantidad de threads que Junit puede utilizar, se aplica limitación por dinámica actual de Junit
					SemaphoreControl.getThreadToWork();
					//Carga los valores del escenario de manera segura para poder ser consultados por múltiples threads
					AutoTool.scenarioHandler().setThreadScenario(scenario);
					//Crea un log para este caso puntual de manera segura para poder ser utilizado por múltiples threads si la función esta activa
					LogHandler.startTestCaseLogFile();
					//Inicializa un objeto Map para almacenar variables temporales de test
					AutoTool.testValues().startThreadTestValues();
				}

			D) Cucumber hook After.
			
				//Se ejecuta una vez después de cada escenario
				@After(order=100)
				public void scenarioTearDown(Scenario scenario) {
					//Valor de configuración para verificar si se debe tomar una captura de pantalla en caso de error
					boolean takeScreenshotOnError = AutoTool.getSetupValue("takeBrowserScreenshotOnError").equals("true");
					//Compruebe si el webdriver sigue funcionando
					boolean driverStillOn=AutoTool.getDriver()!=null;
					//Si se cumplen las condiciones dadas, toma una captura de pantalla
					if (driverStillOn&&scenario.isFailed()&&takeScreenshotOnError) {
						AutoTool.addScreenshotToCucumberExecutionReport("Before Error Screenshot.");
					}
					//Cierra el archivo de log estricto para este escenario si la función esta activa
					LogHandler.writeTestCaseLog();
					//Cierra todas las tareas relacionadas con el controlador si es necesario
					AutoTool.clearDriver();
					//Borra valores de escenario del thread actual
					AutoTool.scenarioHandler().clearScenario();
					//Borra los valores de SOAP del thread actual si es necesario
					SoapHandler.clearSoapValues();
					//Borra valores de prueba del thread actual
					AutoTool.testValues().removeThreadTestValues();
					//Libera la thread en uso para que otro escenario pueda utilizarla
					SemaphoreControl.releaseThreadToWork();
					//Llamada preventiva al garbage collector de Java
					System.gc();					
				}

			E) Cucumber hook AfterAll.
			
				//Se ejecuta una única vez después de todos los escenarios
				@AfterAll
				public static void frameworkFinalTasks() {
					//Cierra el archivo de log estricto para toda la ejecución si la función esta activa
					LogHandler.writeSuiteLog();
				}
			
&nbsp;&nbsp;&nbsp;2) Archivo de configuración de Junit:<br>&ensp; "/FAC/src/test/resources/junit-platform.properties"

&nbsp;&nbsp;&nbsp;Se listan los parámetros y su función:
				
				//Activa la ejecución de casos en paralelo
				cucumber.execution.parallel.enabled=true
				//Define que tipo de estrategia de paralelismo se va a aplicar
				cucumber.execution.parallel.config.strategy=fixed
				//Define la cantidad de threads que van a ejecutar de forma constante
				cucumber.execution.parallel.config.fixed.parallelism=4
				//Genera que Cucumber no muestre un recuadro con información sobre configuración en el log de consola
				cucumber.publish.quiet=true
		
&nbsp;&nbsp;&nbsp;3) Archivo de configuración para parámetros adicionales:<br>&ensp; "/FAC/src/test/resources/config.properties"

&nbsp;&nbsp;&nbsp;Se listan los parámetros y su función:
			
				//Tiempo máximo de espera para que cargue una pagina
				waitPageLoadTimeOut=60
				//Tiempo de espera máximo para la espera implícita de selenium
				implicitWaitTimeOutSeconds=30
				//Tiempo de espera máximo para fluent wait de selenium
				fluentWaitTimeoutSeconds=60
				//Tiempo entre cada validación por si la condición se cumple en el fluent wait
				fluentWaitPollingEveryMillis=500
				//Valor del proxy requerido para el servidor de automatización
				virtualProxy=10.255.10.33:8080
				//Parámetro que indica donde se encuentran los pageObjects en el proyecto, se utiliza en la clase "ScenarioContext"
				PageObjectsPackage=com.telecom.pages.XXX.
				//Switch para generar archivos de log estrictos, que pueden registrar detalles de la ejecución y del driver
				writeTxtLogs=false
				//Switch para adicionar el log de Selenium al log estricto
				writeDriverLogs=false
				//Switch para definir si se toma un screenshot ante un error
				takeBrowserScreenshotOnError=true
				//Switch para ejecutar el driver en modo headless
				driverHeadless=true
<br>
<hr style="border:2px solid blue">
<h4>Driver</h4>
		
&nbsp;&nbsp;&nbsp;La gestión del driver se realiza en las clases del package "com.telecom.webDriver", y los ejecutables relativos están localizados en la carpeta "/FAC/Drivers".<br>
Se debe indicar en el código el tipo de driver a utilizar, en el ejemplo (disponible en el repositorio) ocurre en el primer step del "Background" de Cucumber, otra opcion es instanciarlo en la primer línea del scenario previa al login con la instrucción "AutoTool.setupDriver(browser);". La instrucción mencionada recibe (actualmente) los parámetros "Chrome" y "Firefox", para representar el tipo de driver a utilizar, y se pueden implementar otros browsers con la misma formula.<br>
Para acceder al driver, desde el código la clase utilitaria "/FAC/src/test/java/com/telecom/utils/AutoTool.java" expone el método "getDriver()", el cual se puede acceder de forma directa desde el código sin requerir instanciar el mismo.<br>
Ej.:<br>
`AutoTool.getDriver().findElement(By.xpath("//span[text()='Consola FAN NPLAY']"));`

<br>
<hr style="border:2px solid blue">
<h4>Ejecuciones Paralelas</h4>
		
&nbsp;&nbsp;&nbsp;El framework soporta ejecuciones paralelas, y por defecto está configurado para ejecutar 4 threads simultaneas.
Para configurar la cantidad de threads en paralelo a utilizar el archivo a modificar es: "/FAC/src/test/resources/junit-platform.properties".<br>
Junit controla el paralelismo y aplica una dinámica que tiene un conflicto con la manera en la que Selenium funciona. La explicación completa aquí: https://github.com/junit-team/junit5/issues/1858 .<br>
Para lograr un "control" y poder solucionar el problema, hasta que la versión de Junit 5.9 esté disponible, se aplica una mecánica de semáforos para controlar la cantidad de threads que Junit puede iniciar.<br>
El código que soluciona el problema está distribuido en el flujo de la ejecución, y lo controla distintos métodos de la clase:<br>&ensp; "/FAC/src/test/java/com/telecom/utils/SemaphoreControl.java".

<br>
<hr style="border:2px solid blue">
<h4>Logs:</h4>
		
&nbsp;&nbsp;&nbsp;Actualmente el framework puede generar tres tipos de logs.
<br>
1. <h4>Reporte de ejecución formato HTML.</h4>
&ensp;Es un reporte único por ejecución, que se sobrescribe ante cada nueva ejecución, y queda almacenado en el path "/FAC/target/cucumber-reports.html".
En el documento se reportan detalles básicos de los escenarios ejecutados, con sus respectivos steps.
Se puede enviar líneas de texto al mismo con el método "addLogToCucumberExecutionReport" y capturas de pantalla con el método "addScreenshotToCucumberExecutionReport", ambos proporcionados por la clase  "/FAC/src/test/java/com/telecom/utils/AutoTool.java".
<br>
2. <h4>Reporte de ejecución formato JSON.</h4>
&ensp;Similar al reporte HTML, pero con formato JSON.
<br>
3. <h4>Reporte de texto estricto.</h4>
&ensp;Es un reporte de la ejecución que puede almacenar información enviada por el usuario con el método "addLog" expuesto con la clase utilitaria "/FAC/src/test/java/com/telecom/utils/LogHandler.java", y también puede almacenar todas las interacciones que el driver de Selenium realizo en el browser. Se puede configurar lo anterior mencionado desde el archivo "/FAC/src/test/resources/config.properties".
La forma de utilizarlo es similar a las librerías de logs de Java, donde se debe indicar el nivel de importancia del log, y el mensaje a registrar. El log se almacena en la carpeta "/FAC/Logs" con fecha y hora de ejecución. Es un log que suele ser extenso, se recomienda utilizarlo para casos puntuales donde es requería información extra para el debug, para un commit al repositorio debería estar desactivado.<br>
Ej. de uso:<br>
`LogHandler.addLog(Level.INFO, "Contenido del Given al log, paso 1: I open sauce URL on Chrome.");`

<br>

<hr style="border:2px solid blue">

<h4>Compartir TestValues entre steps:</h4>
		
&nbsp;&nbsp;&nbsp;En el uso diario del framework surge la necesidad de utilizar valores de prueba sensibles y de compartir valores de la ejecución actual entre steps.<br>
Para tal fin se desarrolla la clase utilitaria "/FAC/src/test/java/com/telecom/utils/TestValuesHandler.java" que permite utilizar los valores almacenados previamente en el archivo "TestValues.txt" en la ubicación  "src\test\resources\TestValues" y adicionalmente generar valores propios de una ejecución y compartirlos entre steps.
Los valores son almacenados con una estructura de clave-valor. <br>
Para generar nuevos valores durante la ejecución:<br>
`AutoTool.testValues().setValue("user", "standard_user");`

Para consultar valores durante la ejecucion (tanto los almacenados en "TestValues.txt", como valores cargados durante un paso previo):<br>
`AutoTool.testValues().getValue("user")`

La clase permite almacenar objetos de cualquier tipo durante la ejecución, no solo Strings. Para almacenar otro tipo de objetos, el llamado es el mismo que para un string. Para obtener ese resultado como el objeto que fue almacenado se crea el método:<br>
`AutoTool.testValues().getTestValueAsObject("panqueque")`<br>

Para obtener la totalidad de los valores almacenados en la clase:<br>
`AutoTool.testValues().getAllTestValues();`

<br>
<hr style="border:2px solid blue">

<h4>Compartir PageObjects entre features:</h4>
		
&nbsp;&nbsp;&nbsp;Dado que en el uso diario de Cucumber, seguramente los steps de un mismo escenario estén distribuidos entre varias clases, es necesario aplicar una dinámica accesible para poder compartir los pageObjects entre steps.<br>
Para tal fin se desarrolla la clase utilitaria "/FAC/src/test/java/com/telecom/utils/PageObjectHandler.java" que permite llamar a los pageObjects registrados en la misma desde cualquier step. Se define la propiedad "PageObjectsPackage" para dicha clase en el archivo "config.properties", que define la ubicacion de los PageObjects en el proyecto.<br>
Al generar un nuevo pageObject se debe adicionar en la clase mencionada un método como el siguiente ejemplo:

	public SauceLogin sauceLogin() {
		return (SauceLogin) getPageObjectByClassName("SauceLogin");
	}
	
Repetir estas líneas con cada nuevo pageObject parece repetitivo, pero se hace por lo siguiente(hasta encontrar una mejor solución):<br>

	A) Otra forma de hacerlo sería en cada línea de los step hacer un casteo y un llamado que queda extenso y complicado.
	B) Los tipos de retorno de Java tienen que ser un tipo fundamental fijo o una clase de objeto.
	C) La validación de sintaxis en tiempo de compilación y el análisis estático son herramientas muy poderosas y útiles, cuando usas la API reflection de Java pierdes todos los beneficios que brindan. No sabrá si el código funciona hasta que lo ejecute. No será posible encontrar o acceder de manera simple a los métodos que llame a través de la API reflection. Además, el IDE informará que los métodos generados en el código no se utilizan, lo que no será cierto.

Cuando se deba utilizar los pageObjects se debe realizar el llamado al mismo desde el código de la siguiente manera:<br>
`AutoTool.pageObject().sauceLogin()...`<br>

<br>
<hr style="border:2px solid blue">
<h4>Uso de esperas y manipulación de WebElements:</h4>
		
&nbsp;&nbsp;&nbsp;En el uso del framework es requerido la gestión de esperas por tiempos de carga, presencia de elementos y la manipulación de los mismos en las distintas páginas web que recorren los test cases.<br>
Para tal fin se desarrolla la clase utilitaria "/FAC/src/test/java/com/telecom/utils/WebElementHandler.java" que permite utilizar los siguientes métodos:
- waitForElement
- waitWebPageLoad
- getFluentWait (retorna un objeto de tipo "Wait", utilizado en conjunto con las "ExpectedConditions" de Selenium)
- scrollToElement
- switchToFrameByWebElement
- sendKeysWithDelay
- selectDropdownOptions
- highLightElement
> etc etc etc

Cuando se deba utilizar los métodos mencionados, se debe realizar el llamado al mismo desde el código de la siguiente manera:<br>
`AutoTool.webHandler()...`<br>
<br>
<hr style="border:2px solid blue">

<h4>Pruebas de servicios:</h4>
		
&nbsp;&nbsp;&nbsp;El framework soporta pruebas de servicios Rest y Soap. Las pruebas de servicios Rest se implementan con Rest-assured, y los servicios Soap, con una serie de librerías que son dependencias transitivas de Rest-assured y código realizado por el equipo de arquitectura. Para los dos casos se generan ejemplos que muestran como implementar el código para que pueda realizar ejecuciones paralelas.
<br>
<hr style="border:2px solid blue">
<br>
<center>(┛❍ᴥ❍ )┛彡┻━┻
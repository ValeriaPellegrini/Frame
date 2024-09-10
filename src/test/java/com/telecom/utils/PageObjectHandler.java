package com.telecom.utils;
/**Class that implements singleton design pattern (creational pattern) and the eager initialization concept. It's main use is the handling pageObjects in a multithread safe manner. The class is declared public and final, and should have a private constructor to prevent instantiation if needed. The final keyword prevents sub-classing and can improve efficiency at runtime.
 * @author Marcelo Luna.
 * @version 2.0
 * @since 2.0
 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.telecom.pages.ejemplo.*;
import org.openqa.selenium.WebElement;


public final class PageObjectHandler {
	private static PageObjectHandler instance = new PageObjectHandler();
	private ThreadLocal <Map<String, Object>> threadPagesContainer = new ThreadLocal <Map<String, Object>>();
	private final String packageName = AutoTool.getSetupValue("PageObjectsPackage");
	
    private PageObjectHandler() {
    }

    public static PageObjectHandler getInstance() {
        return instance;
    }
    
	/**Creates a map to store the pageObjects that the scenarios will use in a multithread safe manner.
	 * 
	 */
	public void setThreadPagesContainer() {
		threadPagesContainer.set(new HashMap<String, Object>());
	}
	
	/**Clears the calling thread map of pageObjects.
	 * 
	 */
	public void removeThreadPagesContainer() {
		threadPagesContainer.remove();
	}
	
	/**Returns the requested pageObject by class name.
	 * 
	 * @param pageObjectType The pageObject class name
	 * @return pageObject Related PageObject.
	 */
	private Object getPageObjectByClassName(String pageObjectType) {
		String classCanonicalName = packageName+pageObjectType;
		try {
			Class<?> cls = Class.forName(classCanonicalName);
			threadPagesContainer.get().putIfAbsent(pageObjectType, cls.getDeclaredConstructor().newInstance());
		} catch (Exception exp) {
			System.out.println("There is no page object defined with class "+classCanonicalName);
			exp.printStackTrace();
		}
		return threadPagesContainer.get().get(pageObjectType);
	}
	
	/**Returns a WebElement from it's PageObject by it's name as a String. It could be used to retrieve the WebElement to the steps classes in order to apply asserts over their status or values. Should be useful when applied in DDT statements.
	 * 
	 * @param page The page that contains the WebElement.
	 * @param fieldName The WebElement name.
	 * @return The request WebElement.
	 */
	public WebElement getWebElementFromPage (Object page, String fieldName) {
		try {
			
			Field f = page.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return (WebElement) f.get(page);
		
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		return null;
	}
	
	/**Invokes a PageObject's method by it's name as a String. It could be used in steps classes in order to apply asserts over it's return type. Should be useful when applied in DDT statements.
	 * 
	 * @param page Related PageObject.
	 * @param methodName The requested method name.
	 * @return
	 */
	public Object getMethodFromPage(Object page, String methodName) {
		try {
			
			Method m = page.getClass().getDeclaredMethod(methodName);
			m.setAccessible(true);
			return m.invoke(page);
		
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		return null;
	}

    public HomePage homePage() {
        return (HomePage) getPageObjectByClassName("HomePage");
    }
    public LoginPage loginPage() {
        return (LoginPage) getPageObjectByClassName("LoginPage");
    }

	public FormularioPage formularioPage() {
        return (FormularioPage) getPageObjectByClassName("FormularioPage");
    }
    public HtmlPage htmlPage() {
        return (HtmlPage) getPageObjectByClassName("HtmlPage");
    }
	public IticketsPage IticketsPage() {
        return (IticketsPage) getPageObjectByClassName("IticketsPage");
    }

	public AppsGTecnicaPage AppsGTecnicaPage() {
        return (AppsGTecnicaPage) getPageObjectByClassName("AppsGTecnicaPage");
    }

	public CanalesPage CanalesPage() {
        return (CanalesPage) getPageObjectByClassName("CanalesPage");
    }



}
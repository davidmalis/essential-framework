package org.essentialframework.core.web.databind;

public final class WebContextParameterBinderChainFactory {
	
	public static WebContextParameterBinder createDefaultChain() {
		return createChain(
			new GeneralWebContextParameterBinder(), 
			new BeanFactoryWebContextParameterBinder(), 
			new RequestParameterWebContextParameterBinder(),
			new RequestBodyWebContextParameterBinder());
	}
	
	
	public static <T extends AbstractWebContextParameterBinderChainlink> 
		WebContextParameterBinder createChain(T first, T... more) {
		
		if(first != null) {
			AbstractWebContextParameterBinderChainlink current = first;
			for(AbstractWebContextParameterBinderChainlink chainlink : more) {
				current = current.addNextBinder(chainlink);
			}
		}
		
		return first;
	}
	
	/**
	 * No instantiation
	 */
	private WebContextParameterBinderChainFactory() {}
	
}

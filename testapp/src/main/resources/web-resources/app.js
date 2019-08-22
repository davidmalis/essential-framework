/*
 * 
 */
const ApiModule = function($, baseUrl){

	const _baseUrl = baseUrl;
	
	console.log("Initializing Api module with base.url=" + _baseUrl);
	
	const getBaseUrl = function(){
		return baseUrl;
	}
	
	const doRequest = function(method, uri, params, body){
		return $.ajax({
			type : method,
			url : _baseUrl + uri,
			data : params,
			contentType:"application/json",
		}).fail(function(xhr){
			alert("Error while making API request. See console for more details.")
			console.log("API request failed:")
			console.log(xhr);
		})
	} 
	
	const doGet = function(url, params){
		return doRequest("get", url, params);
	}
	
	const doPost = function(url, params, body){
		return doRequest("post", url, params, body);
	}
	
	//Module API
	return {
		
		getBaseUrl : getBaseUrl,
		get : doGet,
		post : doPost,
		
	}
}(jQuery, "${base.url}");

/*
 * 
 */
const ApplicationEvents = function($){
	
	const Events = {
	}
	
	const doPublishEvent = function(e, params){
		$('body').trigger(e, params);
	}
	
	const doBind = function(e, f){
		$('body').on(e, f);
	}
	
	//Module API
	return {
		Events : Events,
		publishEvent : doPublishEvent,
		bind : doBind
	}	
}(jQuery);

/*
 * 
 */
const Module = function($){
	
	console.log("Initializing xxx module");

	var _currentRealm;
	
	var _elements = {
		$parent : function(){return $('#current-realm')},
	}
	
	
	//Module API
	return {
	}
}(jQuery);


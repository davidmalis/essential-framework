//$('.show-task-modal').click(function(event) {
//    event.preventDefault();
//    $('#task-modal').modal('show');
//});

(function ($) {
    $.fn.serializeObject = function () {

        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
})(jQuery);

/**
 * Application API Module functions.
 */
const ApiModule = function($, baseUrl){

	const _baseUrl = baseUrl;
	
	console.log("Initializing Api module with base.url=" + _baseUrl);
	
	const getBaseUrl = function(){
		return _baseUrl;
	}
	
	const doRequest = function(method, uri, params){
		return $.ajax({
			type : method,
			url : _baseUrl + uri,
			data : params,
			contentType: 'application/json'
		}).fail(function(xhr){
			alert("Error while making API request. See console for more details.")
			console.log("API request failed. Here is the XHR response object:")
			console.log(xhr);
		})
	} 
	
	const doGet = function(url, params){
		return doRequest("get", url, params);
	}
	const doPost = function(url, params){
		return doRequest("post", url, JSON.stringify(params));
	}
	const doPut = function(url, params){
		return doRequest("put", url, params);
	}
	const doDelete = function(url, params){
		return doRequest("delete", url, params);
	}
	
	//Module API
	return {
		getBaseUrl : getBaseUrl,
		get : doGet,
		post : doPost,
		put : doPut,
		delete : doDelete,
	}
}(jQuery, "${base.url}");


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


const ItemListsModule = function($){
	
	console.log("Initializing ItemListsModule module");

	const _elements = {
		$parent: function(){
			return $('#item-lists');
		},
		$newItemList : function(id, name, description){
			return $(
				'<li class="list-group-item" data-id='+id+'>'
                    +'<h4 class="list-group-item-heading">'+name+' <span class="badge">0 tasks</span></h4>'
                    +'<p class="list-group-item-text">'+(description||'')+'</p>'
                    +'<div class="buttons">'
                        +'<a href="#" class="btn btn-default show-todolist-modal btn-xs" title="Edit">'
                            +'<i class="fa fa-pencil-square-o" aria-hidden="true"></i>'
                        +'</a>'
                        +'<a href="#" class="btn btn-danger btn-xs delete" title="Delete">'
                           +'<i class="fa fa-times" aria-hidden="true"></i>'
                        +'</a>'
                    +'</div>'
                +'</li>'
			); 
		},
		$newItemListModal: function(){
			return $('#todolist-modal .save').closest('#todolist-modal');
		},
		$newItemListSaveButton: function(){
			return $('#todolist-modal .save');
		},
	};
	
	const addItemListToGui = function( id, name, description ){
		var $parent = _elements.$parent();
		
		var $itemList = _elements.$newItemList(id, name, description);
		$parent.find('ul.list-group').append($itemList);
		
		var count = _elements.$parent().find('ul.list-group > li').size();
		$parent.find('div.panel-footer > small').html((count+' item list'+(count>1?'s':'')));
	};
	
	const removeAllItemLists = function(){
		_elements.$parent().find('ul.list-group > li').remove();
		_elements.$parent().find('div.panel-footer > small').html('0 item lists');
	};
	
	const doRefresh = function(){
		removeAllItemLists();
		ApiModule
			.get("/lists")
			.done(function(lists){
				$.each(lists, function(i, list){
					addItemListToGui(list.id, list.name, list.description);
				});
		});
	};
	
	const saveNewItemList = function($form){
		ApiModule
			.post("/lists/add", $form.serializeObject())
			.done(function(list){
				doRefresh();
		});
	};
	
	const deleteItemList = function(id){
		ApiModule
			.delete("/list/"+id)
			.done(function(){
				doRefresh();
			})
	}
	
	const bindEvents = function(){
		
		//perform save submit operation when 'Save' is clicked
		_elements.$newItemListSaveButton().on('click', function(e){
			var $form = $(this).closest('.modal').find('form');
			_elements.$newItemListModal().modal('hide');
			saveNewItemList($form);
		});
		
		//perform delete operation when 'Delete' is clicked
		_elements.$parent().on('click', 'li.list-group-item .delete', function(e){
			var itemListId = $(this).closest('li.list-group-item').data("id");
			deleteItemList(itemListId);
		});
		
		//reset modals on dismiss
		$('.modal').on('hidden.bs.modal', function () {
		    $(this).find('form').trigger('reset');
		    //blabla
		});
		
	}();
	
	doRefresh();
	
	
	//Module API
	return {
		refresh : doRefresh,
	}
}(jQuery);





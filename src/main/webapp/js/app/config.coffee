class Model
	constructor:->
		@list=ko.observableArray()
		@selectedIndex=ko.observable()
		@newDir=@createNewDir()
		
	createNewDir:->
		ko.mapping.fromJS(path:'')
	selectIndex:(itm)=>
		@selectedIndex Utils.clone(itm)
		_l ko.mapping.toJSON @selectedIndex,'selectedIndex'
	saveIndex:(itm)=>
		_l 'saveIndex'
		_l ko.mapping.toJS(@selectedIndex),'selectedIndex'
		idx=ko.mapping.toJS(@selectedIndex)
		delete idx.dirs
		
		$.jsonRPC.request('updateIndex',{
			params:[idx],
		})
	addNewDir:=>
		_l 'newDir'
		@selectedIndex().dirs.push(ko.mapping.fromJS(path:@newDir.path()))
		@newDir.path('')
	deleteDir:(dir)=>
		@selectedIndex().dirs.remove(dir)
		
class App
	constructor:->
		$('#test').click @test
		@model=ko.mapping.fromJS(new Model)
		ko.applyBindings(@model);
		$.jsonRPC.request('indexList',{params:null,success:@onIndexList})
		
		
	onIndexList:(resp)=>
		_l resp,'resp'
		ko.mapping.fromJS({list:resp.result}, @model);
		_l @model,'model'
		
	handleError:(resp)=>
		alert resp.error.errorMsg
	
	test:=>
		#$.jsonRPC.setup({endPoint: 'rpc',namespace: ''})
		###
		params=null
		$.jsonRPC.request('indexList',{
			params: params
			success:(res)->
				_l res,'res'
			error:(res)->
				_l res,'res'
		});
		###
		###
		params=[{id:1,name:'xxxxx'}]
		$.jsonRPC.request('updateIndex',{
			params: params
			success:(res)->
				_l res,'res'
			error:(res)->
				_l res,'res'
		});
		###
		
		#_l SERVICE.call('updateIndex',[{id:1,name:'foo',classHintAttribute:'org.syfsyf.search.model.Index'}])
		#_l SERVICE.call('configModel',[])
		##$.post('rpc')
		
		#params={params:{id:1,name:'foo'}}
		#$.getJSON("#{SETTINGS.serviceURL}/updateIndex",params,@onIndexList)
$(document).ready -> 
	new App
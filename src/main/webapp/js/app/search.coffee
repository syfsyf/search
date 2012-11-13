_l=(a,b)->
	console.log a,b
	
#class Model
	
class App
	constructor:->
		$('#test').click(@test)
	onSuccess:(resp)=>
		_l resp
	test:=>
		
		$.getJSON("#{SETTINGS.serviceURL}/indexList",null,@onSuccess)
		d={params:'1'}
		$.getJSON('JSONRR/searchService/getDirs',{params:1},@onSuccess)
		$.getJSON('JSONRR/searchService/indexList',null,@onSuccess)
		$.getJSON('JSONRR/searchService/indexList',null,@onSuccess)
		
$(document).ready -> 
	new App
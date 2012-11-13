window._l=(a,b)->
	console.log a,b

class Settings
	constructor:->
		$.jsonRPC.setup({endPoint: 'rpc',namespace: ''})
class Utils
	@clone:(obj)->
		return ko.mapping.fromJS(ko.toJS(obj));
				
window.SETTINGS=new Settings
window.Utils=Utils
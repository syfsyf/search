// Generated by CoffeeScript 1.3.3
(function() {
  var Settings, Utils;

  window._l = function(a, b) {
    return console.log(a, b);
  };

  Settings = (function() {

    function Settings() {
      $.jsonRPC.setup({
        endPoint: 'rpc',
        namespace: ''
      });
    }

    return Settings;

  })();

  Utils = (function() {

    function Utils() {}

    Utils.clone = function(obj) {
      return ko.mapping.fromJS(ko.toJS(obj));
    };

    return Utils;

  })();

  window.SETTINGS = new Settings;

  window.Utils = Utils;

}).call(this);
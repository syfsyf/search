// Generated by CoffeeScript 1.3.3
(function() {
  var App, _l,
    __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };

  _l = function(a, b) {
    return console.log(a, b);
  };

  App = (function() {

    function App() {
      this.test = __bind(this.test, this);

      this.onSuccess = __bind(this.onSuccess, this);
      $('#test').click(this.test);
    }

    App.prototype.onSuccess = function(resp) {
      return _l(resp);
    };

    App.prototype.test = function() {
      var d;
      $.getJSON("" + SETTINGS.serviceURL + "/indexList", null, this.onSuccess);
      d = {
        params: '1'
      };
      $.getJSON('JSONRR/searchService/getDirs', {
        params: 1
      }, this.onSuccess);
      $.getJSON('JSONRR/searchService/indexList', null, this.onSuccess);
      return $.getJSON('JSONRR/searchService/indexList', null, this.onSuccess);
    };

    return App;

  })();

  $(document).ready(function() {
    return new App;
  });

}).call(this);
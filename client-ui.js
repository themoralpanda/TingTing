var ws;
var start = function start(callback){
  if ("WebSocket" in window)
  {
    ws = new WebSocket("ws://192.168.114.163:42737");
    ws.onopen = function() {
      ws.send("Wazzzzzzzzzzzzup!");
      console.log("Socket opened");
    };
    ws.onmessage = function (evt) { 
      var data = evt.data;
      console.log("sound @", data);
      callback && callback(data);
    };
    ws.onerror = function() {
      console.log('ada poda...');
    };
    ws.onclose = function() { 
      console.log("Connection is closed..."); 
    };
  } else {
    alert("WebSocket NOT supported by your Browser!");
  }
};
var stop = function stop() {
  ws && ws.close();
};
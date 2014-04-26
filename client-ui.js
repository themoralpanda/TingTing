var ws;
var start = function start(){
  if ("WebSocket" in window)
  {
    ws = new WebSocket("wss://192.168.115.142:42737");
    ws.onopen = function() {
      ws.send("Wazzzzzzzzzzzzup!");
      console.log("Socket opened");
    };
    ws.onmessage = function (evt) { 
      var data = evt.data;
      console.log("sound @", data);
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
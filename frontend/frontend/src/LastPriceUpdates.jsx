
function getLastPriceUpdates()
{
   const socket = new WebSocket('wss://ws.finnhub.io?token=d28sfvpr01qle9gsjho0d28sfvpr01qle9gsjhog');

   // Connection opened -> Subscribe
   socket.addEventListener('open', function (event) {
       socket.send(JSON.stringify({'type':'subscribe', 'symbol': 'AAPL'}))
       socket.send(JSON.stringify({'type':'subscribe', 'symbol': 'BINANCE:BTCUSDT'}))
       socket.send(JSON.stringify({'type':'subscribe', 'symbol': 'IC MARKETS:1'}))
   });

   // Listen for messages
   socket.addEventListener('message', function (event) {
       console.log('Message from server ', event.data);
   });

   // Unsubscribe
    var unsubscribe = function(symbol) {
       socket.send(JSON.stringify({'type':'unsubscribe','symbol': symbol}))
   }
   return event.data;

}
export  {getLastPriceUpdates}
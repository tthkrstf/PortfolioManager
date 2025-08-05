import { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import {getLastPriceUpdates} from './LastPriceUpdates.jsx'
/*import {AgGridReact} from 'ag-grid-react'
import {AllCommunityModule, ModuleRegistry} from 'ag-grid-community'*/
//ModuleRegistry.registerModules([AllCommunityModule]);
function App() {

  const [data, setData] = useState("");
  //useEffect(() =>{setData(getLastPriceUpdates())});

  //const colDefs = gridOptions.api.getColumnDefs();
 // colDefs.length=0;
  //const keys = Object.keys(data[0])

    //keys.forEach(key => colDefs.push({field : key}));
    //gridOptions.api.setGridOption('columnDefs', colDefs);

    // add the data to the grid
    //gridOptions.api.setRowData('rowData', data);
    fetch("http://localhost:8080/tasks/greeting").then(asd => asd.json()).then(data => setData(data.message));
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
        socket.close();
        console.log(data);
       return event.data;


}

export default App

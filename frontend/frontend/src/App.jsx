import { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg';
import viteLogo from '/vite.svg';
import './App.css';
import {getLastPriceUpdates} from './LastPriceUpdates.jsx';
import Button from '@mui/material/Button';
import {Charts} from './Charts.jsx';
import {FrontPage} from './FrontPage.jsx';
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import {Layout} from './Layout';
//import {AddAsset} from './AddAsset'

function getSymbolInfo( setData){
  fetch("http://localhost:8080/quotes").then((res) => res.json().then((data) =>{setData(data);
    
  }).catch((err) => console.error(err)));
}
function App() {
  const [data, setData] = useState([]);
  
    
    
    useEffect( () => getSymbolInfo( setData), []);
    
   
    let displayData;
    
    
    let dataArray = [];

    if(data){
        for(let i = 0; i < data.length; i++){
          const quoteObj = {id: i, value: data[i].highPriceOfDay +2, value2: data[i].lowPriceOfDay +4, label: data[i].symbol}
          dataArray.push(quoteObj);
        }
         /*let jsonString = JSON.stringify(data,null,2);
         console.log(data)
         obj = JSON.parse(jsonString);
         displayData = {id:0, value: obj.highPriceOfDay, label: obj.symbol, value2:obj.lowPriceOfDay};*/
         
        
         
    }
    else{
       displayData = "No data yet. Cocaine maybe?";
   }
   
  
    return (
        <BrowserRouter>
              <Routes>
                <Route path="/" element={<Layout />}>
                  <Route index element={<Charts mockData={dataArray} />} />
                  <Route path="assettable" element={<FrontPage/>} />
                  <Route path="frontPage" element={<FrontPage/>}>
                  </Route>
                </Route>
              </Routes>
            </BrowserRouter>
    );
}




  //useEffect(() =>{setData(getLastPriceUpdates())});

  //const colDefs = gridOptions.api.getColumnDefs();
 // colDefs.length=0;
  //const keys = Object.keys(data[0])

    //keys.forEach(key => colDefs.push({field : key}));
    //gridOptions.api.setGridOption('columnDefs', colDefs);

    // add the data to the grid
    //gridOptions.api.setRowData('rowData', data);
    //fetch("http://localhost:8080/tasks/greeting").then(asd => asd.json()).then(data => setData(data.message));
    /*const socket = new WebSocket('wss://ws.finnhub.io?token=d28sfvpr01qle9gsjho0d28sfvpr01qle9gsjhog');

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
       return event.data;*/





export default App
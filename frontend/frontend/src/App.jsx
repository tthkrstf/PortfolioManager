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
import {AddAsset} from './AddAsset';

function getSymbolInfo( setData){
  fetch("http://localhost:8080/quotes?date=2025-08-08").then((res) => res.json().then((data) =>{setData(data);

  }).catch((err) => console.error(err)));
}
function getPortfolioInfo(setPortfolioData){
  fetch("http://localhost:8080/portfolio").then((res) => res.json().then((portfolioData) =>{setPortfolioData(portfolioData)}).catch((err) => console.error(err)));
}

function App() {
  const [data, setData] = useState([]);
  const [portfolioData, setPortfolioData] = useState([]);


    useEffect( () => getSymbolInfo( setData), []);
    useEffect(() => getPortfolioInfo(setPortfolioData),[] );
    
    let displayData;
      
    const [rowData, setRowData] = useState([]);
    const colDefs = [
            {headerName: "Company", field : "company"},
            {headerName: "Quote", field : "quote"},
            {headerName: "News", field : "news"},
            ];
    
    const companyDummyData = {
        Apple: [{
            company: "Apple",
            quote: 90,
            news: "hello apple"
            }],
        Microsoft: [{
                    company: "Microsoft",
                    quote: 83,
                    news: "hello microsoft"
                    }],
        };
//
//     if(data){
//
//         let jsonString = JSON.stringify(data,null,2);
//         obj = JSON.parse(jsonString);
//         displayData = `${obj.symbol} \n ${obj.currentPrice} \n ${obj.changes} \n ${obj.percentChange} \n ${obj.highPriceOfDay} \n ${obj.openPriceOfDay} \n ${obj.percentChange} \n ${obj.prevClosePrice} `
//     }
//     else{
//         displayData = "No data yet. Cocaine maybe?";
//     }


    const dataArray = [];
    const portfolioDataArray = [];
    if(data){
        for(let i = 0; i < data.length; i++){
          const quoteObj = {id: i, value: data[i].highPriceOfDay, value2: data[i].lowPriceOfDay, label: data[i].symbol}
          dataArray.push(quoteObj);
        }
        
         /*let jsonString = JSON.stringify(data,null,2);
         console.log(data)
         obj = JSON.parse(jsonString);
         displayData = {id:0, value: obj.highPriceOfDay, label: obj.symbol, value2:obj.lowPriceOfDay};*/



    }
    else{
       displayData = " ";
   }
   if (portfolioData){
      for(let i = 0; i < portfolioData.length; i++){
        const portfolioObj = {id: i, value:portfolioData[i].shares+1, label:portfolioData[i].symbol};
        portfolioDataArray.push(portfolioObj);
      }
   }
   
   const dataToPass = {symbolData: dataArray, portfolioData: portfolioDataArray};
   
  
    return (
        <BrowserRouter>
              <Routes>
                <Route path="/" element={<Layout />}>
                  <Route index element={<Charts mockData={dataToPass} />} />
                  <Route path="assettable" element={<FrontPage data={companyDummyData} rowData={rowData} colDefs={colDefs} />} />
                  <Route path="addasset" element={<AddAsset data={companyDummyData} rowData={rowData} colDefs={colDefs}/>} />
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
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

function getSymbolInfo(setData) {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth()+1).padStart(2,'0');
  const day = String(today.getDate()).padStart(2,'0');
  const formattedDate = `${year}-${month}-${day}`;
  return fetch(`http://localhost:8080/quotes?date=${formattedDate}`)
    .then(res => res.json())
    .then(data => setData(data))
    .catch(err => console.error(err));
}

function getPortfolioInfo(setPortfolioData) {
  return fetch("http://localhost:8080/portfolio")
    .then(res => res.json())
    .then(portfolioData => setPortfolioData(portfolioData))
    .catch(err => console.error(err));
}

function setUpdateQuotes() {
  return fetch("http://localhost:8080/quote", { method: "PUT" })
    .catch(err => console.error("Request failed:", err));
}

function App() {
    const [data, setData] = useState([]);
    const [portfolioData, setPortfolioData] = useState([]);


    useEffect(() => {
      (async () => {
        await getSymbolInfo(setData);
        await getPortfolioInfo(setPortfolioData);
        await setUpdateQuotes();
      })();
    }, []);
    
    let displayData;
      
    const [rowData, setRowData] = useState([]);
    const colDefs = [
            {headerName: "Company", field : "company"},
            {headerName: "Quote", field : "quote"},
            {headerName: "News", field : "news"},
            {headerName: "Symbol", field : "symbol"},
            ];



    const companyDummyData = {
        Apple: [{
            company: "Apple",
            quote: 90,
            news: "hello apple",
            symbol: "AAPL"
            }],
        Microsoft: [{
            company: "Microsoft",
            quote: 83,
            news: "hello microsoft",
            symbol: "MSFT"
            }],
        Volkswagen: [{
            company: "Volkswagen",
            quote: 83,
            news: "hello volkswagen",
            symbol: "TVE"
            }],
        };


    const dataArray = [];
    const portfolioDataArray = [];
    if(data){
        for(let i = 0; i < data.length; i++){
          console.log(data[i].currentPrice);
          const quoteObj = {id: i, value: data[i].highPriceOfDay, value2: data[i].lowPriceOfDay, segg: data[i].currentPrice, label: data[i].symbol }
          dataArray.push(quoteObj);
        }
        




    }
    else{
       displayData = " ";
   }
   if (portfolioData){
      for(let i = 0; i < portfolioData.length; i++){
        const portfolioObj = {id: i, value:portfolioData[i].shares, label:portfolioData[i].symbol};
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
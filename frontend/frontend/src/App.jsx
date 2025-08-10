import { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg';
import viteLogos from '/vite.svg';
import './style.css';
import {getLastPriceUpdates} from './LastPriceUpdates.jsx';
import Button from '@mui/material/Button';
import {Charts} from './Charts.jsx';
import {AssetTable} from './AssetTable.jsx';
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import {Layout} from './Layout';
import {AddAsset} from './AddAsset';
import {SellAsset} from './SellAsset';

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

export function getPortfolioInfo(setPortfolioData) {
  return fetch("http://localhost:8080/portfolio")
    .then(res => res.json())
    .then(portfolioData => setPortfolioData(portfolioData))
    .catch(err => console.error(err));
}

export  function getPortfolioHoldings(setHoldingsData) {
  console.log("dsa");
  return fetch("http://localhost:8080/portfolio/holdings")
    .then(res => res.json())
    .then(holdingsData => setHoldingsData(holdingsData))
    .catch(err => console.error(err));
}

function setUpdateQuotes() {
  return fetch("http://localhost:8080/quote", { method: "PUT" })
    .catch(err => console.error("Request failed:", err));
}


 function getAdditionalPortfolioInfo(setNetPaid){
  console.log("asd");
  return fetch("http://localhost:8080/portfolio/netPaid").then(res => res.json()).then(netPaid => setNetPaid(netPaid)).catch(err => console.error(err));

}
function App() {
    const [data, setData] = useState([]);
    const [portfolioData, setPortfolioData] = useState([]);
    const [holdingsData, setHoldingsData] = useState([]);
  const [netPaid, setNetPaid] = useState([]);
    
    useEffect(() => {
      (async () => {
        await getSymbolInfo(setData);
        await getPortfolioInfo(setPortfolioData);
        await setUpdateQuotes();
        await getPortfolioHoldings(setHoldingsData);
        await getAdditionalPortfolioInfo(setNetPaid);
      })();
    }, []);
   
    let displayData;
      
    const [rowData, setRowData] = useState([]);
    const colDefs = [
            {headerName: "Company", field : "company"},
            {headerName: "Current Price", field : "currentPrice"},
            {headerName: "News", field : "news"},
            {headerName: "Symbol", field : "symbol"},
            ];
    const colDefsForSell = [
            {headerName: "Company", field : "company"},
            {headerName: "Current Price", field : "currentPrice"},
            {headerName: "News", field : "news"},
            {headerName: "Symbol", field : "symbol"},
            {headerName: "Shares", field : "shares"},
            ];
    const colDefsForAssetTable = [
            {headerName: "Company", field : "company"},
            {headerName: "Current Price", field : "currentPrice"},
            {headerName: "Shares", field : "shares"},
            {headerName: "Total Worth", field : "totalWorth"},
            {headerName: "Paid Amount", field : "paidAmount"},
            {headerName: "Profit", field : "profit"},
            {headerName: "Net Worth", field : "netWorth"},
            ];

    const dataArray = [];
    const portfolioDataArray = [];
    const holdingsDataArray = [];
    let netPaidArray = [];
    console.log(netPaid);
    if(data){
        for(let i = 0; i < data.length; i++){
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
   if(netPaid){
      netPaidArray = [{id:0, value:netPaid.netWorth, label: "Net Worth"}, {id:1, value: netPaid.paidAmount, label: "Paid Amount"}]
   }
   const dataToPass = {symbolData: dataArray, portfolioData: portfolioDataArray, netPaidData: netPaidArray, holdingsData: holdingsDataArray };

    return (
        <BrowserRouter>
              <Routes>
                <Route path="/" element={<Layout />}>
                  <Route index element={<Charts mockData={dataToPass} />} />
                  <Route path="assettable" element={<AssetTable colDefs={colDefsForAssetTable} />} />
                  <Route path="addasset" element={<AddAsset colDefs={colDefs}/>} />
                  <Route path="sellasset" element={<SellAsset colDefs={colDefsForSell}/>} />
                </Route>
              </Routes>
            </BrowserRouter>
    );
}

export default App
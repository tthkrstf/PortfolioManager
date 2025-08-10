import { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg';
import viteLogo from '/vite.svg';
import './App.css';
import {getLastPriceUpdates} from './LastPriceUpdates.jsx';
import Button from '@mui/material/Button';
import {Charts} from './Charts.jsx';
import {AssetTable} from './AssetTable.jsx';
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

export function getPortfolioHoldings(setPortfolioData) {
  return fetch("http://localhost:8080/portfolio/holdings")
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
            {headerName: "Current Price", field : "currentPrice"},
            {headerName: "News", field : "news"},
            {headerName: "Symbol", field : "symbol"},
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
                  <Route path="assettable" element={<AssetTable data={companyDummyData} rowData={rowData} colDefs={colDefsForAssetTable} />} />
                  <Route path="addasset" element={<AddAsset rowData={rowData} colDefs={colDefs}/>} />
                </Route>
              </Routes>
            </BrowserRouter>
    );
}

export default App
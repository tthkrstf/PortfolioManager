import { useEffect, useState } from 'react';
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

export function getSymbolInfo(setData) {
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

export function getPortfolioHoldings(setHoldingsData) {
  return fetch("http://localhost:8080/portfolio/holdings")
    .then(res => res.json())
    .then(holdingsData => setHoldingsData(holdingsData))
    .catch(err => console.error(err));
}

export function setUpdateQuotes() {
  return fetch("http://localhost:8080/quote", { method: "PUT" })
    .catch(err => console.error("Request failed:", err));
}


export function getAdditionalPortfolioInfo(setNetPaid){
  return fetch("http://localhost:8080/portfolio/netPaid").then(res => res.json()).then(netPaid => setNetPaid(netPaid)).catch(err => console.error(err));

}
function App() {
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

    
    return (
        <BrowserRouter>
              <Routes>
                <Route path="/" element={<Layout />}>
                  <Route index element={<Charts />} />
                  <Route path="assettable" element={<AssetTable colDefs={colDefsForAssetTable} />} />
                  <Route path="addasset" element={<AddAsset colDefs={colDefs}/>} />
                  <Route path="sellasset" element={<SellAsset colDefs={colDefsForSell}/>} />
                </Route>
              </Routes>
            </BrowserRouter>
    );
}

export default App
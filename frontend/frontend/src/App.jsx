import { useEffect, useState } from 'react'
import reactLogo from './assets/react.svg';
import viteLogo from '/vite.svg';
import './App.css';
import {getLastPriceUpdates} from './LastPriceUpdates.jsx';
import Button from '@mui/material/Button';
import {pieArcLabelClasses, PieChart} from '@mui/x-charts/PieChart';
import { BarChart } from '@mui/x-charts/BarChart';
import'./barChartStyle.css'
/*import {AgGridReact} from 'ag-grid-react'
import {AllCommunityModule, ModuleRegistry} from 'ag-grid-community'*/
//ModuleRegistry.registerModules([AllCommunityModule]);

function App() {
    const [data, setData] = useState(null);
    const [pie, showPie] = useState(null);
    //Click event for getting the data
    const handleClick = () => {
      fetch("http://localhost:8080/quote_example/AAPL")
        .then((res) => res.json())
        .then((data) => {
          setData(data); 
          console.log(data); 
        })
        .catch((err) => console.error(err));
    };
    
   
    let displayData;
    let obj;
    //Checking if we have the date
    const bier = {id:0, value: 100, label: "Beer"};
    const meth = {id:1,value: 50, label:"Vodka "};
    const whiskey = {id:2,value: 25 ,label: "Whiskey"};
    const mead = {id:3, value: 75, label: "Mead"}
    const mockData = [bier, meth, whiskey, mead];
    
    
    if(data){
        
        let jsonString = JSON.stringify(data,null,2);
        obj = JSON.parse(jsonString);
        displayData = `${obj.symbol} \n ${obj.currentPrice} \n ${obj.changes} \n ${obj.percentChange} \n ${obj.highPriceOfDay} \n ${obj.openPriceOfDay} \n ${obj.percentChange} \n ${obj.prevClosePrice} `
    }
    else{
        displayData = "No data yet. Cocaine maybe?";
    }
   
  
    return (
      <div class="site-container">
      <div class="pie-container">
        {/*<Button variant="outlined" onClick={handleClick}>Click this if you want to snort cocaine</Button> */}
        <p class="title-style">Current stock holdings</p>
         <PieChart series={[{data:mockData}]} width={200} height={200} sx={{
          [`& .${pieArcLabelClasses.root}`]:{filter:'drop-shadow(1px 1px 2px black', animationName: 'animate-pie-arc-label', animationTimingFunction: 'linear', animationIterationCount: 'infinite', 
            animationDirection: 'alternate'}, [`& .${pieArcLabelClasses.root}.${pieArcLabelClasses.animate}`]: {animationDuration: '5s'}

         }} slotProps={{legend: {direction: 'horizontal', position:{vertical: 'bottom', horizontal: 'center'} }}} />
          
       {/*<pre>{displayData}</pre>*/ } 
        
      </div>
      
      <div class="bar-container">
      <p class="title-style">Quotes</p>
      <BarChart
          yAxis={[{ scaleType: 'band', data: ['Bier', 'Vodka B', 'Whiskey'] }]}
          series={[{ data: [4, 3, 5] }, { data: [1, 6, 3] }, { data: [2, 5, 6] }]}
          height={300}
          width={700}
          layout="horizontal"
          grid={{vertical: true}}
          
          
/>
         </div>
      </div>
    );
  



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


}

export default App
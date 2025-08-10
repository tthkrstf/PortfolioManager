import {pieArcLabelClasses, PieChart} from '@mui/x-charts/PieChart';
import { useEffect, useState } from 'react'
import { BarChart } from '@mui/x-charts/BarChart';
import'./style.css'
import {getSymbolInfo, getPortfolioInfo, getPortfolioHoldings, setUpdateQuotes, getAdditionalPortfolioInfo} from "./App"

function Charts(props) {
    const [data, setData] = useState([]);
    const [portfolioData, setPortfolioData] = useState([]);
    const [holdingsData, setHoldingsData] = useState([]);
  const [netPaid, setNetPaid] = useState([]);

  useEffect(() => {
  const fetchAll = async () => {
    setUpdateQuotes().catch(() => {});

    await Promise.all([
      getSymbolInfo(setData),
      getPortfolioInfo(setPortfolioData),
      getPortfolioHoldings(setHoldingsData),
      getAdditionalPortfolioInfo(setNetPaid),
    ]);
  };

  fetchAll();
  const id = setInterval(fetchAll, 5000);
  return () => { clearInterval(id); };
}, []);

  const dataArray = [];
  const portfolioDataArray = [];
  const holdingsDataArray = [];
  let netPaidArray = [];
  if(holdingsData){
    for (let i = 0; i < holdingsData.length; i++) {
      const element = {id:i, value: holdingsData[i].totalWorth, label:holdingsData[i].symbol}
      holdingsDataArray.push(element);

    }

  }
  if(data){
      for(let i = 0; i < data.length; i++){
        const quoteObj = {id: i, value: data[i].highPriceOfDay, value2: data[i].lowPriceOfDay, value3: data[i].currentPrice, label: data[i].symbol }
        dataArray.push(quoteObj);
      }
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



  let colorArray = [
    "rgb(192, 229, 45)",
    "rgb(229, 45, 222)",
    "rgb(45, 229, 97)",
    "rgb(229, 45, 67)",
    "rgb(45, 229, 207)",
    "rgb(148, 229, 45)",
    "rgb(229, 45, 200)",
    "rgb(104, 45, 229)",
    "rgb(45, 97, 229)",
    "rgb(229, 67, 45)",
    "rgb(45, 53, 229)",
    "rgb(45, 185, 229)",
    "rgb(82, 229, 45)",
    "rgb(229, 156, 45)",
    "rgb(45, 229, 141)",
    "rgb(170, 45, 229)",
    "rgb(60, 45, 229)",
    "rgb(192, 45, 229)",
    "rgb(214, 45, 229)",
    "rgb(170, 229, 45)",
    "rgb(229, 45, 156)",
    "rgb(60, 229, 45)",
    "rgb(45, 75, 229)",
    "rgb(45, 229, 53)",
    "rgb(126, 229, 45)",
    "rgb(229, 45, 45)",
    "rgb(45, 229, 185)",
    "rgb(229, 89, 45)",
    "rgb(45, 141, 229)",
    "rgb(104, 229, 45)",
    "rgb(229, 134, 45)",
    "rgb(229, 200, 45)",
    "rgb(229, 178, 45)",
    "rgb(229, 45, 119)",
    "rgb(82, 45, 229)",
    "rgb(229, 111, 45)",
    "rgb(229, 45, 178)",
    "rgb(214, 229, 45)",
    "rgb(45, 163, 229)",
    "rgb(45, 229, 229)",
    "rgb(45, 207, 229)",
    "rgb(45, 119, 229)",
    "rgb(45, 229, 75)",
    "rgb(148, 45, 229)",
    "rgb(126, 45, 229)",
    "rgb(229, 45, 89)",
    "rgb(229, 45, 134)",
    "rgb(45, 229, 163)",
    "rgb(229, 222, 45)",
    "rgb(229, 45, 111)"
  ];
  
  function getLabels(){
    let labelArray  = [];
    for(let i = 0; i<dataToPass.symbolData.length; i++){
        labelArray.push(dataToPass.symbolData[i].label);
    }
    return labelArray;
  }
 
  const series = [{
    data: dataToPass.symbolData.map(item => item.value),
    label: 'Highest Price Today',
    color: "rgb(42, 166, 248)"
  },
  
  {
    data:dataToPass.symbolData.map(item => item.value2),
    label: 'Lowest Price Today',
    color: "rgb(218, 70, 70)"
  },
  {
    data:dataToPass.symbolData.map(item => item.value3),
    label: 'Current Price',
    color: "rgb(0, 255, 13)"
  }
]
  getLabels();
  
    return (
          <div class="site-container">
          <div class="pie-container">
            {/*<Button variant="outlined" onClick={handleClick}>Click this if you want to snort cocaine</Button> */}
            <p class="title-style">Current stock holdings</p>
            <p class="additional-label">{`Net Worth: ${dataToPass.netPaidData[0].value}`}</p>
            <p class="additional-label">{`Amount Paid: ${dataToPass.netPaidData[1].value}`}</p>
             <PieChart colors={colorArray} series={[{data:dataToPass.portfolioData, innerRadius:50, outerRadius:100, }]}  width={250} height={250} sx={{ '& .MuiPieArc-root': {stroke: 'none'},
              [`& .${pieArcLabelClasses.root}`]:{filter:'drop-shadow(1px 1px 2px black', animationName: 'animate-pie-arc-label', animationTimingFunction: 'linear', animationIterationCount: 'infinite',
                animationDirection: 'alternate'}, [`& .${pieArcLabelClasses.root}.${pieArcLabelClasses.animate}`]: {animationDuration: '5s'}

             }} slotProps={{legend: {direction: 'horizontal', position:{vertical: 'bottom', horizontal: 'center'} }}} />
            

           {/*<pre>{displayData}</pre>*/ }

          </div>
          <div id="two-part-pie" class="pie-container">
            <br/>

            {/*<Button variant="outlined" onClick={handleClick}>Click this if you want to snort cocaine</Button> */}
            <p class="title-style">Current stock values</p>
             <PieChart colors={colorArray} series={[{data:dataToPass.holdingsData, innerRadius:50, outerRadius:100, }]}  width={250} height={250} sx={{ '& .MuiPieArc-root': {stroke: 'none'},
              [`& .${pieArcLabelClasses.root}`]:{filter:'drop-shadow(1px 1px 2px black', animationName: 'animate-pie-arc-label', animationTimingFunction: 'linear', animationIterationCount: 'infinite',
                animationDirection: 'alternate'}, [`& .${pieArcLabelClasses.root}.${pieArcLabelClasses.animate}`]: {animationDuration: '5s'}

             }} slotProps={{legend: {direction: 'horizontal', position:{vertical: 'bottom', horizontal: 'center'} }}} />
            

           {/*<pre>{displayData}</pre>*/ }
             <br/>
          </div>

          <div class="bar-container">

          <p class="title-style">Daily pricings</p>
          <BarChart
              yAxis={[{ scaleType: 'band', data: getLabels() }]}
              series={series}
              height={500}
              width={700}
              layout="horizontal"
              grid={{vertical: true}}
              sx={{
                '& .MuiPieArc-root': {stroke: 'none'},
              }}
              borderRadius={5}
              slotProps={{tooltip: {trigger: 'axis'}, axisTickLabel:{style:{color: 'rgb(255,255,255)'}}, axisLine:{style:{color: 'rgb(255,255,255)', stroke:'rgb(212, 200, 200)', strokeWidth:2}}, axisTick:{style:{stroke:'rgb(173, 164, 164)', strokeWidth:2}}}}
              


    />
             </div>

          </div>
    );
    }
export {Charts}

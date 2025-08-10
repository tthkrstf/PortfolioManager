import {pieArcLabelClasses, PieChart} from '@mui/x-charts/PieChart';
import { BarChart } from '@mui/x-charts/BarChart';
import'./style.css'
function Charts(props) {
  console.log(props.mockData.portfolioData);
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
    for(let i = 0; i<props.mockData.symbolData.length; i++){
      
      labelArray.push(props.mockData.symbolData[i].label);
    }
    return labelArray;
  }
 
  const series = [{
    data: props.mockData.symbolData.map(item => item.value),
    label: 'Highest Price Today',
    color: "rgb(42, 166, 248)"
  },
  
  {
    data:props.mockData.symbolData.map(item => item.value2),
    label: 'Lowest Price Today',
    color: "rgb(218, 70, 70)"
  },
  {
    data:props.mockData.symbolData.map(item => item.segg),
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
             <PieChart colors={colorArray} series={[{data:props.mockData.portfolioData, innerRadius:50, outerRadius:100, }]}  width={250} height={250} sx={{ '& .MuiPieArc-root': {stroke: 'none'},
              [`& .${pieArcLabelClasses.root}`]:{filter:'drop-shadow(1px 1px 2px black', animationName: 'animate-pie-arc-label', animationTimingFunction: 'linear', animationIterationCount: 'infinite',
                animationDirection: 'alternate'}, [`& .${pieArcLabelClasses.root}.${pieArcLabelClasses.animate}`]: {animationDuration: '5s'}

             }} slotProps={{legend: {direction: 'horizontal', position:{vertical: 'bottom', horizontal: 'center'} }}} />
            

           {/*<pre>{displayData}</pre>*/ }

          </div>
          <div id="two-part-pie" class="pie-container">
            {/*<Button variant="outlined" onClick={handleClick}>Click this if you want to snort cocaine</Button> */}
            <p class="title-style">Current stock holdings</p>
             <PieChart colors={colorArray} series={[{data:props.mockData.portfolioData, innerRadius:50, outerRadius:100, }]}  width={250} height={250} sx={{ '& .MuiPieArc-root': {stroke: 'none'},
              [`& .${pieArcLabelClasses.root}`]:{filter:'drop-shadow(1px 1px 2px black', animationName: 'animate-pie-arc-label', animationTimingFunction: 'linear', animationIterationCount: 'infinite',
                animationDirection: 'alternate'}, [`& .${pieArcLabelClasses.root}.${pieArcLabelClasses.animate}`]: {animationDuration: '5s'}

             }} slotProps={{legend: {direction: 'horizontal', position:{vertical: 'bottom', horizontal: 'center'} }}} />
            

           {/*<pre>{displayData}</pre>*/ }

          </div>

          <div class="bar-container">
          <p class="title-style bar-title">Quotes</p>
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
              slotProps={{tooltip: {trigger: 'axis'}, axisLine:{style:{stroke:'rgb(212, 200, 200)', strokeWidth:2}}, axisTick:{style:{stroke:'rgb(173, 164, 164)', strokeWidth:2}}}}
              


    />
             </div>
          </div>
    );
    }
export {Charts}

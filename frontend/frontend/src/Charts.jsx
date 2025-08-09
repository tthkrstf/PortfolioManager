import {pieArcLabelClasses, PieChart} from '@mui/x-charts/PieChart';
import { BarChart } from '@mui/x-charts/BarChart';
import'./barChartStyle.css'
function Charts(props) {
  console.log(props.mockData.portfolioData);
  function getLabels(){
    let labelArray  = [];
    for(let i = 0; i<props.mockData.symbolData.length; i++){
      
      labelArray.push(props.mockData.symbolData[i].label);
    }
    return labelArray;
  }
 
  const series = [{
    data: props.mockData.symbolData.map(item => item.value)
  },
  
  {
    data:props.mockData.symbolData.map(item => item.value2)
  }
]
  getLabels();
    return (
          <div class="site-container">
          <div class="pie-container">
            {/*<Button variant="outlined" onClick={handleClick}>Click this if you want to snort cocaine</Button> */}
            <p class="title-style">Current stock holdings</p>
             <PieChart series={[{data:props.mockData.portfolioData}]} width={250} height={250} sx={{
              [`& .${pieArcLabelClasses.root}`]:{filter:'drop-shadow(1px 1px 2px black', animationName: 'animate-pie-arc-label', animationTimingFunction: 'linear', animationIterationCount: 'infinite',
                animationDirection: 'alternate'}, [`& .${pieArcLabelClasses.root}.${pieArcLabelClasses.animate}`]: {animationDuration: '5s'}

             }} slotProps={{legend: {direction: 'horizontal', position:{vertical: 'bottom', horizontal: 'center'} }}} />

           {/*<pre>{displayData}</pre>*/ }

          </div>

          <div class="bar-container">
          <p class="title-style">Quotes</p>
          <BarChart
              yAxis={[{ scaleType: 'band', data: getLabels() }]}
              series={series}
              height={300}
              width={700}
              layout="horizontal"
              grid={{vertical: true}}
              borderRadius={5}


    />
             </div>
          </div>
    );
    }
export {Charts}

import {pieArcLabelClasses, PieChart} from '@mui/x-charts/PieChart';
import { BarChart } from '@mui/x-charts/BarChart';
import'./barChartStyle.css'
function Charts(props) {
    return (
          <div class="site-container">
          <div class="pie-container">
            {/*<Button variant="outlined" onClick={handleClick}>Click this if you want to snort cocaine</Button> */}
            <p class="title-style">Current stock holdings</p>
             <PieChart series={[{data:props.mockData}]} width={200} height={200} sx={{
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
              borderRadius={5}


    />
             </div>
          </div>
    );
    }
export {Charts}

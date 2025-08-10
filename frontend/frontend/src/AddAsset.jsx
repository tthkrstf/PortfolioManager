// AddAsset.jsx
import React, {useEffect, useState, useMemo, useCallback, forwardRef, Children} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Autocomplete from '@mui/material/Autocomplete';
import { FixedSizeList as List } from 'react-window';

import { AllCommunityModule, ModuleRegistry} from 'ag-grid-community';
ModuleRegistry.registerModules([AllCommunityModule]);
import {AgGridReact} from 'ag-grid-react';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import './style.css'

function getStocks(setStocks) {
  return fetch("http://localhost:8080/stock", { method: "GET" })
    .then(res => res.json())
    .then(data => setStocks(data))
    .catch(err => console.error("Request failed:", err));
}

const ITEM_SIZE = 36;
const MAX_VISIBLE = 8;

// ref points to the list that has the drop down
const InnerElementType = forwardRef(function InnerElementType(props, ref) {
  return <ul ref={ref} {...props} />;
});

// Children are the li-s for the dropdown list.
const ListboxComponent = forwardRef(function ListboxComponent(props, ref) {
  const { children, onLoadMore, ...other } = props;
  const items = Children.toArray(children);
  const height = Math.min(MAX_VISIBLE, items.length) * ITEM_SIZE;

  // li element, we inject style into it, because elements are immutable.
  const Row = ({ index, style, data }) => React.cloneElement(data[index], { style });

  // onItemsRendered: when you scroll it calls it.
  // visibleStopIndex: index of the last visible item
  // visibleStopIndex >= items.length - 2: if we are seeing like 48 out of 50 of the items, then we load in more
  return (
    <div ref={ref} {...other}>
      <List
        height={height}
        itemCount={items.length}
        itemSize={ITEM_SIZE}
        width="100%"
        itemData={items}
        innerElementType={InnerElementType}
        onItemsRendered={({ visibleStopIndex }) => {
          if (visibleStopIndex >= items.length - 2) {
            onLoadMore?.();
          }
        }}
      >
        {Row}
      </List>
    </div>
  );
});

function AddAsset(props) {
  // UseMemo runs first on the empty stocks list
  // Loads the stocks with useState after first render -> useMemo picks up changes ->  runs the mapping
  const [stocks, setStocks] = useState([]);
  useEffect(() => { (async () => { await getStocks(setStocks); })(); }, []);

  const companyNames = useMemo(
    () => [...new Set(stocks.map(item => item.description))],
    [stocks]
  );

  const companyDummyData = props.data;
  const [selectedAssetCompany, setSelectedAssetCompany] = useState("");
  const [selectedCompanyData, setSelectedCompanyData] = useState({});
  const gridRows = selectedCompanyData.company ? [selectedCompanyData] : [];
  const [rowData, setRowData] = useState([props.rowData]);
  const [inputValue, setInputValue] = useState(""); // shares

  const [companyInput, setCompanyInput] = useState("");
  const [debouncedInput, setDebouncedInput] = useState("");

  const [selectedSymbol, setSelectedSymbol] = useState("");
  const [quote, setQuote] = useState(null);
  const [news, setNews] = useState([]);

  // Only saves the input from search bar, when 250ms is passed. After every stroke it reset the timer with the cleanup
  // function - clearTimeout(t). Reason is we don't need to update the companyInput only when the user stops typing.
  useEffect(() => {
    const t = setTimeout(() => setDebouncedInput(companyInput.trim().toLowerCase()), 250);
    return () => clearTimeout(t);
  }, [companyInput]);

  // Have to useMemo so it updates during render time, rather then after render.
  const filteredAll = useMemo(() => {
    if (!debouncedInput) return companyNames;
    const searchInput = debouncedInput;
    const out = [];
    for (let i = 0; i < companyNames.length; i++) {
      const name = companyNames[i];
      if (name && name.toLowerCase().includes(searchInput)) out.push(name);
    }
    return out;
  }, [companyNames, debouncedInput]);

  // 200 items loaded initially in the drop down list
  const PAGE = 200;
  const [maxToShow, setMaxToShow] = useState(PAGE);

  // Resets the maximum item to be shown in the dropdown lists when the search field changes and on the initial start.
  useEffect(() => { setMaxToShow(PAGE); }, [debouncedInput, companyNames]);

  // Changes when filterAll was applied and got the new company name list, or when the maxToShow changes, then we display
  // more company names.
  const options = useMemo(
    () => filteredAll.slice(0, Math.min(filteredAll.length, maxToShow)),
    [filteredAll, maxToShow]
  );

  // Needs to be useCallback so the function only gets a new reference, when the filteredAll length changes
  // This prevents unnecessary renders for the dropdown list, as react-window re-renders when it sees a new reference.
  const loadMore = useCallback(() => {
    setMaxToShow(m => (m < filteredAll.length ? Math.min(m + PAGE, filteredAll.length) : m));
  }, [filteredAll.length]);

  // ?? checks if its undefined/null, this is for the table below. Setting the selected company and the info of said company.
  const handleChangeCompany = async (_event, value) => {
    if (!value) {
      setSelectedAssetCompany("");
      setSelectedCompanyData({});
      return;
    }
    setSelectedAssetCompany(value);
    const symbol = stocks.find(s => s.description === value)?.symbol || "";
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth()+1).padStart(2,'0');
    const day = String(today.getDate()).padStart(2,'0');
    const formattedDate = `${year}-${month}-${day}`;

    try {
      const formattedDate = new Date().toISOString().split("T")[0];

      const [quoteRes, newsRes] = await Promise.all([
        fetch(`http://localhost:8080/quote/${encodeURIComponent(symbol)}?date=${formattedDate}`),
        fetch(`http://localhost:8080/company_news/${encodeURIComponent(symbol)}`)
      ]);

      if (!quoteRes.ok) throw new Error(`Quote HTTP ${quoteRes.status}`);
      if (!newsRes.ok) throw new Error(`News HTTP ${newsRes.status}`);

      const [quote, news] = await Promise.all([
        quoteRes.json(),
        newsRes.json()
      ]);

      const firstNews = Array.isArray(news) ? (news[0].summary ?? null) : null;

      setSelectedCompanyData({
        company: value,
        symbol: symbol,
        currentPrice: quote?.currentPrice ?? null,
        news: firstNews
      });

    } catch (err) {
      console.error("Error fetching quote/news:", err);
      setSelectedCompanyData({
        company: value,
        symbol,
        quote: null,
        news: []
      });
    };
  }
  // On the shares text field change, it sets the value for it
  const handleInput = (event) => setInputValue(event.target.value);

  // ? at the end mean, that if its undefined/null it will skip over it and not throw an error.
  const handleClick = () => {
    const payload = {
      symbol: selectedCompanyData?.symbol || "",
      shares: inputValue
    };

    fetch("http://localhost:8080/portfolio", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    })
    .catch(err => console.error("Request failed:", err));
  };

  // Autocomplete is for the search/dropdown list.
  //    Options: slice of the filtered company names, that is showing the amount of company names what MaxPage is set to
  //    Value: what is currently selected, if its "" then its setting it to null
  //    onChange: when user selects a company name it runs and sets the selectedCompany and the data for that company to be
  //                shown in the table below it.
  //    inputValue: currently typed into the search box
  //    onInputChange: set's the company input variable -> causes the setDebouncedInput to happen if no stroke -> causes
  //                   the filtering to happen for the filteredAll -> causes the slice to happen for options
  //    filterOptions: disabling this as we are already done with the filtering -> have x and just pass it as is, no filtering
  //    ListboxComponent: replaces listboxcomponent with our custom one
  //    ListboxProps: extra function passed, so it can call the loadMore when the function gets re-initialized
  //    disableListWrap: disables looping so if you press down on the last element it won't go back to the top
  //    disablePortal: needed for react-window so it can reach and interact with it
  //    noOptionsText: when no suitable company was found for the search
  //    getOptionLabel: how to write out the object the list has, in this case if its a string (which everything should be)
  //                    then just show it as is
  //    renderInput: params here just mean for the styling, so just pass every styling option, value it has etc.

  return (
    <div >
    <div class="add-table" >
      <h1> Add new asset </h1>
      <Box 
        
        component="form"
        sx={{ '& .MuiTextField-root': { m: 1, width: '50rem' } }}
        noValidate
        autoComplete="off"
      >
        <div>
          <Autocomplete
            options={options}
            value={selectedAssetCompany || null}
            onChange={handleChangeCompany}
            inputValue={companyInput}
            onInputChange={(_e, v) => setCompanyInput(v)}
            filterOptions={(x) => x}
            ListboxComponent={ListboxComponent}
            ListboxProps={{ onLoadMore: loadMore }}
            disableListWrap
            disablePortal
            noOptionsText={companyInput ? 'No matches' : 'Type to search'}
            getOptionLabel={(opt) => (typeof opt === 'string' ? opt : '')}
            renderInput={(params) => (
              <TextField {...params} required label="Select company asset" />
            )}
          />


          <div className="ag-theme-alpine" style={{width:"50rem", height: "20rem"}} >
            <AgGridReact
              rowData={gridRows}
              columnDefs={props.colDefs}
              defaultColDef={{flex : 1, minWidth : 100, resizable: true}}
            />
            <div>
              <TextField
                required
                id="outlined-required"
                label="Number of shares"
                value={inputValue}
                onChange={handleInput}
              />
              <Button variant="outlined" onClick={handleClick}>Buy</Button>
            </div>
          </div>
        </div>
      </Box>
    </div>
    </div>
  );
}

export { AddAsset }
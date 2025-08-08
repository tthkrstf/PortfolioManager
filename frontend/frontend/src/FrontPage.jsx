import React, {useState} from 'react';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Dialog from '@mui/material/Dialog';
import { styled } from '@mui/material/styles';
import { AllCommunityModule, ModuleRegistry} from 'ag-grid-community';
ModuleRegistry.registerModules([AllCommunityModule]);
import {AgGridReact} from 'ag-grid-react';
import {AssetTable} from './AssetTable.jsx';
import {Link} from "react-router";


function FrontPage() {

    const [tableOpen, setTableOpen] = useState(false);
    const [selectedAsset, setSelectedAsset] = useState({});
    const [rowData, setRowData] = useState([]);

    const colDefs = [
        {headerName: "Company", field : "company"},
        {headerName: "Quote", field : "quote"},
        {headerName: "News", field : "news"},
        ]
    const companyDummyData = {
        Apple: [{
            company: "Apple",
            quote: 90,
            news: "hello apple"
            }],
        Microsoft: [{
                    company: "Microsoft",
                    quote: 83,
                    news: "hello microsoft"
                    }],
        };

    const handleAssetClick = (company) => {
        console.log("Clicked company: ", company);
        const data = companyDummyData[company];
        setSelectedAsset(data[0]);
        setRowData(data);
        setTableOpen(true);
        };

    const handleTableClose = () => {
        setTableOpen(false);
        setSelectedAsset({});
        setRowData([]);
        };

    const companyNames = Object.keys(companyDummyData);

  return (
    <Box display="flex" height="100vh">
        <AssetTable companyNames={companyNames} handleAssetClick={handleAssetClick}
        onAddAssetClick={onAddAssetClick} />
    <Box width="70%" p={2}>
        <div className="ag-theme-alpine" style={{width:"50rem", height: "30rem"}} >
        <AgGridReact rowData={rowData} columnDefs={colDefs} defaultColDef={{flex : 1, minWidth : 100, resizable: true}} />
        </div>

    </Box>
    </Box>
  );
}
export {FrontPage}
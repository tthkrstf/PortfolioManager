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


function FrontPage(props) {

    const [tableOpen, setTableOpen] = useState(false);
    const [selectedAsset, setSelectedAsset] = useState({});
    const [rowData, setRowData] = useState([props.rowData]);
    const companyDummyData = props.data;


    const handleAssetClick = (company) => {
        console.log("Clicked company: ", company);
        const data = companyDummyData[company];
        setSelectedAsset(data[0]);
        setRowData(data);
        console.log(rowData);
        setTableOpen(true);
        };

    const handleTableClose = () => {
        setTableOpen(false);
        setSelectedAsset({});
        setRowData([]);
        };
    const onAddAssetClick = () => {
            console.log("hello");
            };

    const companyNames = Object.keys(companyDummyData);

  return (
    <Box display="flex" height="100vh">
        <AssetTable companyNames={companyNames} handleAssetClick={handleAssetClick}
        onAddAssetClick={onAddAssetClick} />
    <Box width="70%" p={2}>
        <div className="ag-theme-alpine" style={{width:"50rem", height: "30rem"}} >
        <AgGridReact rowData={rowData} columnDefs={props.colDefs} defaultColDef={{flex : 1, minWidth : 100, resizable: true}} />
        </div>

    </Box>
    </Box>
  );
}
export {FrontPage}
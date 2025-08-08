import React, {useState} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import { AllCommunityModule, ModuleRegistry} from 'ag-grid-community';
ModuleRegistry.registerModules([AllCommunityModule]);
import {AgGridReact} from 'ag-grid-react';


function AddAsset(props) {
    const handleChange = (event) => {
            setSelectedAssetCompany(event.target.value);
            setSelectedCompanyData(props.data[event.target.value]);

        }
    const companyNames = Object.keys(props.data);
    const companyDummyData = props.data;
    const [selectedAssetCompany, setSelectedAssetCompany] = useState("");
    const [selectedCompanyData, setSelectedCompanyData] = useState({});
    const [rowData, setRowData] = useState([props.rowData]);

    return (
        <div>
            <h1> Add new asset </h1>
             <Box
                  component="form"
                  sx={{ '& .MuiTextField-root': { m: 1, width: '25ch' } }}
                  noValidate
                  autoComplete="off"
                >
                  <div>
                    <TextField
                      required
                      id="outlined-required"
                      select
                      label="Select company asset"
                      defaultValue={companyNames[0]}
                      value={selectedAssetCompany}
                      onChange={handleChange}
                    >
                    {companyNames.map((name) => (
                        <MenuItem key={name} value={name}>
                            {name}
                        </MenuItem>
                        ))}
                    </TextField>
                    <div className="ag-theme-alpine" style={{width:"30rem", height: "20rem"}} >
                    <AgGridReact rowData={selectedCompanyData} columnDefs={props.colDefs} defaultColDef={{flex : 1, minWidth : 100, resizable: true}} />
                    </div>
                    </div>
                </Box>
        </div>
        );
    }

export {AddAsset}
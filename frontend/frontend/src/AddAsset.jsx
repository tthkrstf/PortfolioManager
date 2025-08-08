import React, {useState} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import { AllCommunityModule, ModuleRegistry} from 'ag-grid-community';
ModuleRegistry.registerModules([AllCommunityModule]);
import {AgGridReact} from 'ag-grid-react';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';


function AddAsset(props) {
    const handleChangeCompany = (event) => {
            setSelectedAssetCompany(event.target.value);
            setSelectedCompanyData(props.data[event.target.value]);

        }
    const companyNames = Object.keys(props.data);
    const companyDummyData = props.data;
    const [selectedAssetCompany, setSelectedAssetCompany] = useState("");
    const [selectedCompanyData, setSelectedCompanyData] = useState({});
    const [rowData, setRowData] = useState([props.rowData]);
    const[inputValue, setInputValue] = useState('');
    const [shares, setShares] = useState(0);

    const handleInput = (event) => {
        setInputValue(event.target.value);
        };
    const handleClick = () => {
        setShares(inputValue);
        };

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
                      onChange={handleChangeCompany}
                    >
                    {companyNames.map((name) => (
                        <MenuItem key={name} value={name}>
                            {name}
                        </MenuItem>
                        ))}
                    </TextField>
                    <div className="ag-theme-alpine" style={{width:"30rem", height: "20rem"}} >
                    <AgGridReact rowData={selectedCompanyData} columnDefs={props.colDefs} defaultColDef={{flex : 1, minWidth : 100, resizable: true}} />
                     <div>
                     <TextField
                      required
                      id="outlined-required"
                      label="Number of shares"
                      value={inputValue}
                      onChange={handleInput}
                    >
                    </TextField>
                    <Button variant="outlined" onClick={handleClick}>Buy</Button>
                    </div>
                    </div>
                    </div>
                </Box>
        </div>
        );
    }

export {AddAsset}
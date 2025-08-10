import React, {useEffect, useState} from 'react';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import { AllCommunityModule, ModuleRegistry } from "ag-grid-community";
ModuleRegistry.registerModules([AllCommunityModule]);
import { AgGridReact } from "ag-grid-react";
import { getPortfolioHoldings } from "./App.jsx";

function AssetTable({ colDefs }) {
      const [rows, setRows] = useState([]);

  useEffect(() => {
    getPortfolioHoldings((data) => {
      setRows(data);
    });
  }, []);

  return (
    <div class="add-table">
      <h1>Asset Table</h1>
      <div
        className="ag-theme-alpine"
        style={{ width: "70rem", height: "32rem" }}
      >
        <AgGridReact
          rowData={rows}
          columnDefs={colDefs}
          defaultColDef={{
            minWidth: 80,
            maxWidth: 155,
            resizable: true,
            cellStyle: { padding: "4px" }
          }}
        />
      </div>
    </div>
  );
}

export {AssetTable}
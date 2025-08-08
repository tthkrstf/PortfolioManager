import React, {useState} from 'react';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: (theme.vars ?? theme).palette.text.secondary,
  ...theme.applyStyles('dark', {
    backgroundColor: '#1A2027',
  }),
}));

function AssetTable(props) {

    const onAddAssetClick = () => {
            console.log("hey");
        }

    return (
        <div>
            <h1> Asset Table </h1>
            <Button onClick={props.onAddAssetClick}> Add </Button>
            <Stack> {props.companyNames.map((company) => (
                <Item key={company} onClick={() => props.handleAssetClick(company)}> {company}</Item>))}
            </Stack>
        </div>
        );
    }

export {AssetTable}
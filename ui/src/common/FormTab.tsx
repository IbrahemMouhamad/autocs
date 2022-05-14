// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { makeStyles } from '@mui/styles';
import { FormTab as RAFormTab } from 'react-admin';

export const useFormTabStyles = makeStyles(
    () => ({
        root: {
            flexDirection: 'row',
            '& svg': {
                margin: '0 8px !important',
            },
        },
    }),
    {
        name: 'FormTab',
    },
);

const FormTab = ({ children, label, ...rest }): JSX.Element => {
    const classes = useFormTabStyles();

    return (
        <RAFormTab
            {...rest}
            label={label}
            className={classes.root}
        >
            {children}
        </RAFormTab>
    );
};

export default FormTab;

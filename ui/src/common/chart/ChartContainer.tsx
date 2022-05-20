// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { makeStyles } from '@mui/styles';
import Box from '@mui/material/Box';
import { useTranslate } from 'react-admin';
import StackedLineChartIcon from '@mui/icons-material/StackedLineChart';

import Accordion from '../Accordion';

export const useStyles = makeStyles(
    () => ({
        AccordionDetails: {
            display: 'block',
            alignItems: 'center',
        },
    }),
    {
        name: 'ChartContainer',
    },
);

const ChartContainer = (props): JSX.Element => {
    const translate = useTranslate();
    const { children, headingText, icon } = props;
    const classes = useStyles();

    return (
        <Accordion
            icon={icon || StackedLineChartIcon}
            headingText={translate(headingText)}
            detailsClass={classes.AccordionDetails}
        >
            <Box
                sx={{
                    height: 400,
                    display: 'block',
                }}
            >
                {children}
            </Box>
        </Accordion>
    );
};

export default ChartContainer;

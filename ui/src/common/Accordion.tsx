// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { makeStyles } from '@mui/styles';
import { Accordion as MUIAccordion } from '@mui/material';
import AccordionDetails from '@mui/material/AccordionDetails';
import AccordionSummary from '@mui/material/AccordionSummary';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

export const useAccordionStyles = makeStyles(
    (theme) => ({
        root: {
            width: '100%',
        },
        heading: {
            fontSize: theme.typography.pxToRem(15),
        },
        details: {
            display: 'flex',
            alignItems: 'center',
        },
        column: {
            flexBasis: '33.33%',
            marginRight: 30,
        },
    }),
    {
        name: 'Accordion',
    },
);

const Accordion = ({
    headingText,
    children,
    icon: Icon,
    ...rest
}): JSX.Element => {
    const classes = useAccordionStyles();

    return (
        <MUIAccordion
            {...rest}
            className={classes.root}
        >
            <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
                aria-controls={`${headingText}-content`}
                id={`${headingText}-header`}
            >
                <Icon />
                <div className={classes.column} style={{ marginLeft: '10px' }}>
                    <Typography className={classes.heading}>{headingText}</Typography>
                </div>
            </AccordionSummary>
            <AccordionDetails className={classes.details}>
                {children}
            </AccordionDetails>
        </MUIAccordion>
    );
};

export default Accordion;

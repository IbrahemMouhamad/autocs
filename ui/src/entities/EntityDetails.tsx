// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import Grid from '@mui/material/Grid';
import Divider from '@mui/material/Divider';
import {
    SimpleShowLayout,
    TextField,
    useRecordContext,
    CardContentInner,
    useTranslate,
    useResourceContext,
} from 'react-admin';

import icons from '../icons';

const EntityDetails = (): JSX.Element | null => {
    const record = useRecordContext();
    const resource = useResourceContext();
    const translate = useTranslate();
    const { properties } = record;

    return properties ? (
        <Card>
            <CardHeader
                avatar={React.createElement(icons[resource])}
                title={
                    // eslint-disable-next-line max-len
                    `${translate(`resources.${resource}.name`, 1)} ${translate(`resources.${resource}.fields.properties`)}`
                }
                titleTypographyProps={{
                    variant: 'button',
                }}
            />
            <CardContentInner>
                <Grid container spacing={2}>
                    {
                        Object.keys(properties).map((key) => (
                            <Grid key={key} item md={4} xs={12}>
                                <SimpleShowLayout record={properties}>
                                    <TextField source={key} />
                                </SimpleShowLayout>
                                <Divider />
                            </Grid>
                        ))
                    }
                </Grid>
            </CardContentInner>
        </Card>
    ) : null;
};

export default EntityDetails;

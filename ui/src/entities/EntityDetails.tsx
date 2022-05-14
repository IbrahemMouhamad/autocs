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
import InfoIcon from '@mui/icons-material/Info';
import AnalyticsIcon from '@mui/icons-material/Analytics';

import icons from '../icons';

const EntityDetailsSection = ({ data, title, icon }): JSX.Element => (
    <Card>
        <CardHeader
            avatar={React.createElement(icon)}
            title={title}
            titleTypographyProps={{
                variant: 'button',
            }}
        />
        <CardContentInner>
            <Grid container spacing={2}>
                {
                    Object.keys(data).map((key) => (
                        <Grid key={key} item md={4} xs={12}>
                            <SimpleShowLayout record={data}>
                                <TextField source={key} />
                            </SimpleShowLayout>
                            <Divider />
                        </Grid>
                    ))
                }
            </Grid>
        </CardContentInner>
    </Card>
);

const EntityDetails = (): JSX.Element | null => {
    const record = useRecordContext();
    const resource = useResourceContext();
    const translate = useTranslate();
    const { properties, statistics } = record;

    return (properties || statistics) ? (
        <Card>
            <CardHeader
                avatar={React.createElement(icons[resource])}
                title={record.name}
                titleTypographyProps={{
                    variant: 'button',
                }}
            />
            <CardContentInner>
                {
                    properties && (
                        <EntityDetailsSection
                            data={properties}
                            title={
                                // eslint-disable-next-line max-len
                                `${translate(`resources.${resource}.fields.properties`)}`
                            }
                            icon={InfoIcon}
                        />
                    )
                }
                {
                    statistics && (
                        <EntityDetailsSection
                            data={statistics}
                            title={
                                // eslint-disable-next-line max-len
                                `${translate(`resources.${resource}.fields.statistics`)}`
                            }
                            icon={AnalyticsIcon}
                        />
                    )
                }
            </CardContentInner>
        </Card>
    ) : null;
};

export default EntityDetails;

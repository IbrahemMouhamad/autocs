// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardContent from '@mui/material/CardContent';
import { makeStyles } from '@mui/styles';
import {
    Datagrid,
    TextField,
    FunctionField,
} from 'react-admin';
import SettingsSuggestIcon from '@mui/icons-material/SettingsSuggest';
import SegmentIcon from '@mui/icons-material/Segment';

import { JsonFieldBox, JsonField } from '../common';

const useStyles = makeStyles(
    () => ({
        jsonField: {
            maxWidth: 'fit-content',
        },
    }),
    {
        name: 'ConfigurationItems',
    },
);

const DetailPanel = ({ record, id, itemText }): JSX.Element => {
    const classes = useStyles();

    const data = {};
    delete record.id;
    delete record.description;
    data[id] = record;

    return (
        <Card>
            <CardHeader
                avatar={
                    <SegmentIcon />
                }
                title={itemText}
                titleTypographyProps={{
                    variant: 'button',
                }}
            />
            <CardContent>
                <JsonField source={id} record={data} className={classes.jsonField} splitList />
            </CardContent>
        </Card>
    );
};

const ConfigurationItems = (props): JSX.Element => {
    const {
        source,
        headingText,
        ...rest
    } = props;

    return (
        <Card>
            <CardHeader
                avatar={
                    <SettingsSuggestIcon />
                }
                title={headingText}
                titleTypographyProps={{
                    variant: 'button',
                }}
            />
            <CardContent>
                <JsonFieldBox {...rest}>
                    <FunctionField
                        source={source}
                        render={(record) => {
                            const data = Object.keys(record[source]).map((id) => {
                                record[source][id].id = id;
                                return record[source][id];
                            });
                            return (
                                <Datagrid
                                    data={data}
                                    expand={<DetailPanel {...props} />}
                                    bulkActionButtons={false}
                                >
                                    <TextField source='id' label='resources.configurations.fields.id' sortable={false} />
                                    <TextField source='description' label='resources.configurations.fields.description' sortable={false} />
                                </Datagrid>
                            );
                        }}
                    />
                </JsonFieldBox>
            </CardContent>
        </Card>
    );
};

export default ConfigurationItems;

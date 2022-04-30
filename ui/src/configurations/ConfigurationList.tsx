// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    useRecordContext,
    useTranslate,
} from 'react-admin';

import ConfigurationItems from './ConfigurationItems';

const ConfigurationItemsPanel = (): JSX.Element => {
    const record = useRecordContext();
    const translate = useTranslate();
    return (
        <ConfigurationItems
            source='configuration'
            headingText={`${translate(`resources.${record.id}s.name`, 1)} ${translate('resources.configurations.name', 1)}`}
            itemText={translate('resources.configurations.fields.details')}
            record={record}
        />
    );
};

const ConfigurationList = (): JSX.Element => (
    <List
        exporter={false}
    >
        <Datagrid
            bulkActionButtons={false}
            expand={<ConfigurationItemsPanel />}
        >
            <TextField source='name' />
            <TextField source='description' />
        </Datagrid>
    </List>
);

export default ConfigurationList;

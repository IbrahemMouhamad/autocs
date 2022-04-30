// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    DateField,
    // BulkExportButton,
    BulkDeleteButton,
} from 'react-admin';

import EntityDetails from './EntityDetails';

const EntityBulkActionButtons = (): JSX.Element => (
    <>
        {/* <BulkExportButton /> */}
        <BulkDeleteButton mutationMode='optimistic' />
    </>
);

const EntityList = (): JSX.Element => (
    <List>
        <Datagrid
            rowClick='edit'
            expand={<EntityDetails />}
            bulkActionButtons={<EntityBulkActionButtons />}
        >
            <TextField source='id' />
            <TextField source='name' />
            <TextField source='description' />
            <DateField source='lastModified' />
        </Datagrid>
    </List>
);

export default EntityList;

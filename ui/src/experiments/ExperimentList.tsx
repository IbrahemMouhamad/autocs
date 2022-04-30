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

const ExperimentBulkActionButtons = (): JSX.Element => (
    <>
        {/* <BulkExportButton /> */}
        <BulkDeleteButton mutationMode='optimistic' />
    </>
);

const ExperimentList = (): JSX.Element => (
    <List>
        <Datagrid
            rowClick='edit'
            bulkActionButtons={<ExperimentBulkActionButtons />}
        >
            <TextField source='id' />
            <TextField source='name' />
            <TextField source='description' />
            <DateField source='createdDate' />
        </Datagrid>
    </List>
);

export default ExperimentList;

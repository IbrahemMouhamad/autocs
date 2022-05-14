// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    DateField,
} from 'react-admin';

import { ListPageToolbar, BulkActionToolbar } from '../toolbar';

const DefaultListPage = ({ children }): JSX.Element => (
    <List
        actions={<ListPageToolbar />}
    >
        <Datagrid
            bulkActionButtons={<BulkActionToolbar />}
        >
            <TextField source='id' />
            <TextField source='name' />
            <TextField source='description' />
            <DateField source='lastModified' />
            {children}
        </Datagrid>
    </List>
);

export default DefaultListPage;

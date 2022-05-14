// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { TopToolbar, CreateButton, ExportButton } from 'react-admin';

const ListPageToolbar = ({ children = undefined }): JSX.Element => (
    <TopToolbar>
        {children}
        <CreateButton label='action.new' />
        <ExportButton />
    </TopToolbar>
);

export default ListPageToolbar;

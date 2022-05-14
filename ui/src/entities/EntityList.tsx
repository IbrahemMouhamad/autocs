// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { DefaultListPage, GridActionToolbar } from '../common';

import EntityDetails from './EntityDetails';

const EntityList = (): JSX.Element => (
    <DefaultListPage>
        <GridActionToolbar DetailsComponent={EntityDetails} />
    </DefaultListPage>
);

export default EntityList;

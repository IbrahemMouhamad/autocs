// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { DefaultListPage, GridActionToolbar } from '../common';
import { EntityDetails } from '../entities';

const ProviderList = (): JSX.Element => (
    <DefaultListPage>
        <GridActionToolbar DetailsComponent={EntityDetails} />
    </DefaultListPage>
);

export default ProviderList;

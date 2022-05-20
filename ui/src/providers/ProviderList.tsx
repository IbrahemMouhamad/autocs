// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import {
    SingleFieldList,
    ChipField,
    ArrayField,
} from 'react-admin';
import { DefaultListPage, GridActionToolbar } from '../common';
import { EntityDetails } from '../entities';

const ProviderList = (): JSX.Element => (
    <DefaultListPage>
        <ArrayField source='datacenters'>
            <SingleFieldList resource='datacenters'>
                <ChipField source='name' />
            </SingleFieldList>
        </ArrayField>
        <GridActionToolbar DetailsComponent={EntityDetails} hasEdit />
    </DefaultListPage>
);

export default ProviderList;
